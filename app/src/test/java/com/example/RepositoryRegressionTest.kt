package com.example

import com.example.data.local.*
import com.example.data.model.*
import com.example.data.repository.HabitRepository
import com.example.data.repository.TaskRepository
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import java.lang.reflect.Proxy
import java.time.LocalDate

@org.junit.runner.RunWith(org.robolectric.RobolectricTestRunner::class)
@org.robolectric.annotation.Config(sdk = [34], application = android.app.Application::class)
class RepositoryRegressionTest {
  private inline fun <reified T> dao(crossinline call: (String, Array<out Any?>) -> Any?): T =
    Proxy.newProxyInstance(T::class.java.classLoader, arrayOf(T::class.java)) { _, method, args ->
      call(method.name, args ?: emptyArray())
    } as T

  private class Subtasks {
    val initial = Subtask(id = "child", parentTaskId = "parent", title = "Original")
    var stored = initial.toEntity()
    var writes = 0
  }

  private fun repository(state: Subtasks): TaskRepository {
    val tasks = dao<TaskDao> { name, _ ->
      when (name) {
        "getAllTasks" -> flowOf(emptyList<TaskEntity>())
        else -> error("Unexpected task access: $name")
      }
    }
    val subtasks = dao<SubtaskDao> { name, args ->
      when (name) {
        "getAllSubtasks" -> flowOf(listOf(state.stored))
        "getAllSubtasksSync", "getSubtasksForTaskSync" -> listOf(state.stored)
        "updateSubtask" -> { state.stored = args[0] as SubtaskEntity; state.writes++; Unit }
        else -> error("Unexpected subtask access: $name")
      }
    }
    return TaskRepository(tasks, subtasks)
  }

  @Test fun toggleByIdActuallyPersistsCompletion() = runBlocking {
    val state = Subtasks()
    repository(state).toggleSubtaskCompletion("child")
    assertTrue(state.stored.isCompleted)
    assertEquals(1, state.writes)
  }

  @Test fun repeatedToggleUsesPersistedStateAndPreservesEditedTitle() = runBlocking {
    val state = Subtasks()
    val repository = repository(state)
    state.stored = state.stored.copy(title = "Edited elsewhere")
    repository.toggleSubtask(state.initial)
    repository.toggleSubtask(state.initial)
    assertFalse(state.stored.isCompleted)
    assertEquals("Edited elsewhere", state.stored.title)
    assertEquals(2, state.writes)
  }

  @Test fun missingSubtaskDoesNotWrite() = runBlocking {
    val state = Subtasks()
    repository(state).toggleSubtaskCompletion("missing")
    assertEquals(0, state.writes)
  }

  @Test fun completingLastSubtaskDatesParentAndCreatesNextOccurrence() = runBlocking {
    // Attach a context without running the production application's startup/sync work.
    val application = HabitTaskApplication()
    org.robolectric.util.ReflectionHelpers.callInstanceMethod<Void>(
      application, "attachBaseContext",
      org.robolectric.util.ReflectionHelpers.ClassParameter.from(android.content.Context::class.java, org.robolectric.RuntimeEnvironment.getApplication())
    )
    val field = HabitTaskApplication::class.java.getDeclaredField("instance").apply { isAccessible = true }
    val previous = field.get(null)
    field.set(null, application)
    try {
      val original = Task(id = "parent", title = "Daily work", dueDate = "2026-11-17", repeatRule = RepeatRule.DAILY)
      val taskRows = mutableListOf(original.toEntity())
      val children = mutableListOf(Subtask(id = "child", parentTaskId = "parent", title = "Finish").toEntity())
      val tasks = dao<TaskDao> { name, args ->
        when (name) {
          "getAllTasks" -> flowOf(taskRows.toList())
          "getAllTasksSync" -> taskRows.toList()
          "getTaskByIdSync" -> taskRows.find { it.id == args[0] }
          "updateTask" -> { val row = args[0] as TaskEntity; taskRows[taskRows.indexOfFirst { it.id == row.id }] = row; Unit }
          "insertTask" -> { taskRows.add(args[0] as TaskEntity); Unit }
          else -> error("Unexpected task access: $name")
        }
      }
      val subtasks = dao<SubtaskDao> { name, args ->
        when (name) {
          "getAllSubtasks" -> flowOf(children.toList())
          "getAllSubtasksSync" -> children.toList()
          "getSubtasksForTaskSync" -> children.filter { it.parentTaskId == args[0] }
          "updateSubtask" -> { val row = args[0] as SubtaskEntity; children[children.indexOfFirst { it.id == row.id }] = row; Unit }
          "insertSubtasks" -> { @Suppress("UNCHECKED_CAST") val rows = args[0] as List<SubtaskEntity>; children.addAll(rows); Unit }
          else -> error("Unexpected subtask access: $name")
        }
      }
      TaskRepository(tasks, subtasks).toggleSubtaskCompletion("child", autoMarkParent = true)
      val parent = taskRows.single { it.id == "parent" }
      assertTrue(parent.isCompleted)
      assertEquals(LocalDate.now().toString(), parent.completedDate)
      val next = taskRows.single { it.id != "parent" }
      assertEquals("2026-11-18", next.dueDate)
      assertFalse(next.isCompleted)
      assertNull(next.completedDate)
      assertFalse(children.single { it.parentTaskId == next.id }.isCompleted)
    } finally {
      field.set(null, previous)
    }
  }

  @Test fun streakIgnoresOtherHabitsAndUsesNewestRecordForDate() {
    val habit = Habit(id = "mine", name = "Read", startDate = "2026-11-16")
    val records = listOf(
      HabitCompletionRecord(habitId = "mine", date = "2026-11-16", isCompleted = true, timestamp = 20),
      HabitCompletionRecord(habitId = "mine", date = "2026-11-16", isCompleted = false, timestamp = 10),
      HabitCompletionRecord(habitId = "other", date = "2026-11-17", isCompleted = true)
    )
    val result = HabitRepository.calculateStreak(habit, records, LocalDate.of(2026, 11, 17))
    assertEquals(1, result.totalCompletions)
    assertEquals(1, result.currentStreak)
    assertEquals(0, result.missedScheduledDaysCount)
  }
}
