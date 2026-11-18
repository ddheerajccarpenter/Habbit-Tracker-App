package com.example.ui.screens.home

import com.example.ui.theme.StrongFontFamily
import com.example.ui.theme.LocalAppColors
import com.example.ui.components.AnimatedIcon
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.example.ui.components.UndoConfirmationPill
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.material.icons.filled.DragHandle
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Insights
import androidx.compose.material.icons.rounded.Handyman
import com.example.util.DashboardLayoutManager
import com.example.util.DashboardSection
import com.example.ui.theme.OuterCornerRadius
import com.example.ui.theme.InnerCornerRadius
import com.example.ui.theme.OuterCornerShape
import com.example.ui.theme.InnerCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.Habit
import com.example.data.model.Task
import com.example.data.model.TaskPriority
import com.example.ui.components.AppEmptyState
import com.example.ui.components.DeleteConfirmationDialog
import com.example.ui.components.FloatingNavContentBottomPadding
import com.example.ui.components.AppleCircularCheckbox
import com.example.ui.components.GlassCheckbox
import com.example.ui.components.paperDotBackground
import com.example.ui.components.bounceClick
import com.example.ui.components.sectionEntrance
import com.example.ui.theme.InkMuted
import com.example.ui.theme.InkPrimary
import com.example.ui.theme.InkSecondary
import com.example.ui.theme.PrimaryAccentLime
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.DeleteOutline
import com.example.ui.viewmodel.MainViewModel
import com.example.util.AppPreferences
import com.example.ui.screens.calendar.formatTimeDisplay
import androidx.compose.ui.platform.LocalContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun HomeScreen(
  viewModel: MainViewModel,
  onNavigateToAddTask: () -> Unit,
  onNavigateToAddHabit: () -> Unit,
  onTaskClick: (Task) -> Unit,
  onHabitClick: (Habit) -> Unit,
  modifier: Modifier = Modifier,
  onNavigateToSettings: (() -> Unit)? = null,
  onNavigateToSearch: (() -> Unit)? = null,
  onNavigateToTools: (() -> Unit)? = null,
  onOpenFocusTimer: (() -> Unit)? = null,
  onQuickAdd: (() -> Unit)? = null
) {
  val context = LocalContext.current
  val settings by viewModel.settings.collectAsStateWithLifecycle()
  val todayTasksViewMode by AppPreferences.todayTasksViewMode.collectAsStateWithLifecycle()
  val todayHabitsViewMode by AppPreferences.todayHabitsViewMode.collectAsStateWithLifecycle()

  val tasks by viewModel.tasks.collectAsStateWithLifecycle()
  val habits by viewModel.habits.collectAsStateWithLifecycle()
  val completionRecords by viewModel.completionRecords.collectAsStateWithLifecycle()
  val summary by viewModel.dailySummary.collectAsStateWithLifecycle()

  var selectedTab by remember { mutableIntStateOf(0) } // 0: All, 1: Tasks, 2: Habits
  var isDashboardEditMode by remember { mutableStateOf(false) }
  var showHomeMenu by remember { mutableStateOf(false) }
  var showOverview by remember { mutableStateOf(false) }
  var taskToDelete by remember { mutableStateOf<Task?>(null) }
  var habitToDelete by remember { mutableStateOf<Habit?>(null) }

  val sectionOrder by DashboardLayoutManager.sectionOrder.collectAsStateWithLifecycle()
  val sectionVisibility by DashboardLayoutManager.sectionVisibility.collectAsStateWithLifecycle()
  var editOrder by remember(isDashboardEditMode, sectionOrder) { mutableStateOf(sectionOrder) }
  var editVisibility by remember(isDashboardEditMode, sectionVisibility) { mutableStateOf(sectionVisibility) }
  var draggedSectionId by remember { mutableStateOf<String?>(null) }
  var dragAccumulatedY by remember { mutableFloatStateOf(0f) }

  val today = LocalDate.now()
  val todayStr = today.format(DateTimeFormatter.ISO_LOCAL_DATE)
  val todayFormattedDate = today.format(DateTimeFormatter.ofPattern("EEE, MMM d", Locale.getDefault()))

  val todayTasks = remember(tasks, today) {
    tasks.filter { it.isDueToday(today) }
  }

  val todayHabits = remember(habits, today) {
    habits.filter { it.isActive && !it.isArchived && !it.isPaused && it.isScheduledOn(today) }
  }

  val isDark = com.example.ui.theme.LocalAppColors.current.isDark

  val scheduledTasks = remember(todayTasks) {
    todayTasks.filter { it.dueTime.isNotBlank() }
      .sortedBy { it.dueTime }
  }

  val todayHabitRecords by viewModel.todayHabitRecords.collectAsStateWithLifecycle()

  val completedHabitIds = remember(completionRecords, todayStr) {
    completionRecords
      .filter { it.date == todayStr && it.isCompleted }
      .map { it.habitId }
      .toSet()
  }

  val listState = rememberLazyListState()
  val coroutineScope = rememberCoroutineScope()
  var showUndoPill by remember { mutableStateOf(false) }
  var lastCompletedTaskId by remember { mutableStateOf<String?>(null) }
  var undoDismissJob by remember { mutableStateOf<Job?>(null) }

  val handleTaskToggle: (Task) -> Unit = { task ->
    val willBeCompleted = !task.isCompleted
    viewModel.toggleTaskCompletion(task.id)
    if (willBeCompleted) {
      lastCompletedTaskId = task.id
      showUndoPill = true
      com.example.util.SoundManager.playTaskComplete(context)
      com.example.util.HapticManager.taskCompleted(context)
      undoDismissJob?.cancel()
      undoDismissJob = coroutineScope.launch {
        delay(3500)
        showUndoPill = false
      }
    }
  }


  Box(
    modifier = modifier
      .fillMaxSize()
      .background(Color.Black)
  ) {
    LazyColumn(
      state = listState,
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 16.dp),
      contentPadding = PaddingValues(
        top = 10.dp,
        bottom = FloatingNavContentBottomPadding + 48.dp
      )
    ) {
      // ── Custom Minimal Header ──
      item {
        Column(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text(
                text = "Today",
                style = TextStyle(
                  fontFamily = StrongFontFamily,
                  fontSize = 32.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color(0xFFF2F2F2)
                )
              )
              Text(
                text = todayFormattedDate,
                style = TextStyle(
                  fontSize = 14.sp,
                  color = Color(0xFF929292)
                ),
                modifier = Modifier.padding(top = 2.dp)
              )
            }

            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
              if (onNavigateToSearch != null) {
                IconButton(
                  onClick = { onNavigateToSearch() },
                  modifier = Modifier.size(40.dp).testTag("home_search_button")
                ) {
                  Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color(0xFF929292),
                    modifier = Modifier.size(20.dp)
                  )
                }
              }

              Box {
                IconButton(
                  onClick = { showHomeMenu = true },
                  modifier = Modifier.size(40.dp).testTag("home_options_button")
                ) {
                  Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Home options",
                    tint = Color(0xFF929292),
                    modifier = Modifier.size(20.dp)
                  )
                }
                androidx.compose.material3.DropdownMenu(
                  expanded = showHomeMenu,
                  onDismissRequest = { showHomeMenu = false },
                  modifier = Modifier.background(Color(0xFF1A1A1A))
                ) {
                  androidx.compose.material3.DropdownMenuItem(
                    text = {
                      Text(
                        if (isDashboardEditMode) "Finish customization" else "Customize dashboard",
                        style = TextStyle(fontSize = 14.sp, color = InkPrimary)
                      )
                    },
                    onClick = {
                      showHomeMenu = false
                      if (isDashboardEditMode) {
                        DashboardLayoutManager.saveLayout(context, editOrder, editVisibility)
                      }
                      isDashboardEditMode = !isDashboardEditMode
                    },
                    modifier = Modifier.testTag("home_edit_mode_btn")
                  )
                  androidx.compose.material3.DropdownMenuItem(
                    text = {
                      Text(
                        if (showOverview) "Hide daily overview" else "Show daily overview",
                        style = TextStyle(fontSize = 14.sp, color = InkPrimary)
                      )
                    },
                    onClick = {
                      showOverview = !showOverview
                      showHomeMenu = false
                    }
                  )
                }
              }
            }
          }
        }
        Spacer(modifier = Modifier.height(14.dp))
      }

      if (isDashboardEditMode) {
        // 1. Layout Editor Header Card
        item {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .background(Color(0xFF1A1A1A), RoundedCornerShape(OuterCornerRadius))
              .padding(16.dp)
          ) {
            Column(modifier = Modifier.fillMaxWidth()) {
              Text(
                text = "DASHBOARD LAYOUT",
                style = TextStyle(
                  fontFamily = StrongFontFamily,
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  letterSpacing = 1.sp,
                  color = InkPrimary
                )
              )
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = "Reorder sections and choose what appears on Home.",
                style = TextStyle(
                  fontFamily = StrongFontFamily,
                  fontSize = 13.sp,
                  color = InkSecondary
                )
              )
            }
          }
          Spacer(modifier = Modifier.height(14.dp))
        }

        // 2. Sections Reordering & Visibility List
        itemsIndexed(editOrder, key = { _, s -> s.id }) { index, section ->
          val isVisible = editVisibility[section] != false
          val isFirst = index == 0
          val isLast = index == editOrder.size - 1
          val isBeingDragged = draggedSectionId == section.id
          val dragScale by animateFloatAsState(
            targetValue = if (isBeingDragged) 1.03f else 1.0f,
            animationSpec = spring(dampingRatio = 0.75f, stiffness = 400f),
            label = "dragCardScale"
          )

          Box(
            modifier = Modifier
              .fillMaxWidth()
              .graphicsLayer {
                scaleX = dragScale
                scaleY = dragScale
              }
              .animateItem()
              .background(Color(0xFF1A1A1A), RoundedCornerShape(InnerCornerRadius))
              .border(
                width = if (isBeingDragged) 1.5.dp else 1.dp,
                color = if (isBeingDragged) InkPrimary else if (isVisible) Color(0xFF292929) else Color(0xFF292929).copy(alpha = 0.4f),
                shape = RoundedCornerShape(InnerCornerRadius)
              )
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              // Touch Drag Handle
              Box(
                modifier = Modifier
                  .size(48.dp)
                  .clip(CircleShape)
                  .background(
                    if (isBeingDragged) Color(0x33FFFFFF)
                    else Color.Transparent
                  )
                  .pointerInput(editOrder) {
                    detectVerticalDragGestures(
                      onDragStart = {
                        draggedSectionId = section.id
                        dragAccumulatedY = 0f
                        com.example.util.VibrationHelper.tick(context)
                      },
                      onDragEnd = {
                        draggedSectionId = null
                        dragAccumulatedY = 0f
                      },
                      onDragCancel = {
                        draggedSectionId = null
                        dragAccumulatedY = 0f
                      },
                      onVerticalDrag = { change, dragAmount ->
                        change.consume()
                        dragAccumulatedY += dragAmount
                        val thresholdPx = 48.dp.toPx()
                        val curIdx = editOrder.indexOfFirst { it.id == section.id }
                        if (curIdx != -1) {
                          if (dragAccumulatedY > thresholdPx && curIdx < editOrder.size - 1) {
                            val newOrder = editOrder.toMutableList()
                            val temp = newOrder[curIdx]
                            newOrder[curIdx] = newOrder[curIdx + 1]
                            newOrder[curIdx + 1] = temp
                            editOrder = newOrder
                            dragAccumulatedY = 0f
                            com.example.util.VibrationHelper.tick(context)
                            com.example.util.SoundManager.playSwipeNav(context)
                          } else if (dragAccumulatedY < -thresholdPx && curIdx > 0) {
                            val newOrder = editOrder.toMutableList()
                            val temp = newOrder[curIdx]
                            newOrder[curIdx] = newOrder[curIdx - 1]
                            newOrder[curIdx - 1] = temp
                            editOrder = newOrder
                            dragAccumulatedY = 0f
                            com.example.util.VibrationHelper.tick(context)
                            com.example.util.SoundManager.playSwipeNav(context)
                          }
                        }
                      }
                    )
                  },
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.DragHandle,
                  contentDescription = "Drag to reorder",
                  tint = if (isBeingDragged) InkPrimary else InkSecondary.copy(alpha = 0.6f),
                  modifier = Modifier.size(20.dp)
                )
              }

              // Up / Down Reorder Arrows
              Column(
                verticalArrangement = Arrangement.spacedBy(2.dp),
                horizontalAlignment = Alignment.CenterHorizontally
              ) {
                Box(
                  modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(if (!isFirst) Color(0x18FFFFFF) else Color.Transparent)
                    .clickable(enabled = !isFirst) {
                      if (!isFirst) {
                        val newOrder = editOrder.toMutableList()
                        val temp = newOrder[index]
                        newOrder[index] = newOrder[index - 1]
                        newOrder[index - 1] = temp
                        editOrder = newOrder
                        com.example.util.SoundManager.playSwipeNav(context)
                      }
                    },
                  contentAlignment = Alignment.Center
                ) {
                  Icon(
                    imageVector = Icons.Default.KeyboardArrowUp,
                    contentDescription = "Move Up",
                    tint = if (!isFirst) InkPrimary else InkSecondary.copy(alpha = 0.3f),
                    modifier = Modifier.size(16.dp)
                  )
                }

                Box(
                  modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(if (!isLast) Color(0x18FFFFFF) else Color.Transparent)
                    .clickable(enabled = !isLast) {
                      if (!isLast) {
                        val newOrder = editOrder.toMutableList()
                        val temp = newOrder[index]
                        newOrder[index] = newOrder[index + 1]
                        newOrder[index + 1] = temp
                        editOrder = newOrder
                        com.example.util.SoundManager.playSwipeNav(context)
                      }
                    },
                  contentAlignment = Alignment.Center
                ) {
                  Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Move Down",
                    tint = if (!isLast) InkPrimary else InkSecondary.copy(alpha = 0.3f),
                    modifier = Modifier.size(16.dp)
                  )
                }
              }

              // Section Info
              Column(modifier = Modifier.weight(1f)) {
                Text(
                  text = section.title,
                  style = TextStyle(
                    fontFamily = StrongFontFamily,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isVisible) InkPrimary else InkSecondary
                  )
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                  text = section.subtitle,
                  style = TextStyle(
                    fontFamily = StrongFontFamily,
                    fontSize = 12.sp,
                    color = InkSecondary
                  ),
                  maxLines = 1
                )
              }

              // Visibility Eye Toggle
              Box(
                modifier = Modifier
                  .size(48.dp)
                  .clip(CircleShape)
                  .background(
                    if (isVisible) Color(0x22FFFFFF) else Color.Transparent
                  )
                  .clickable {
                    val map = editVisibility.toMutableMap()
                    map[section] = !isVisible
                    editVisibility = map
                  },
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = if (isVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                  contentDescription = if (isVisible) "Hide section" else "Show section",
                  tint = if (isVisible) InkPrimary else InkSecondary.copy(alpha = 0.5f),
                  modifier = Modifier.size(20.dp)
                )
              }
            }
          }
          Spacer(modifier = Modifier.height(8.dp))
        }

        // 3. Action Buttons Row (Reset / Cancel / Save)
        item {
          Spacer(modifier = Modifier.height(10.dp))
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            // Reset Button
            Box(
              modifier = Modifier
                .weight(1f)
                .height(48.dp)
                .background(Color(0xFF1A1A1A), InnerCornerShape)
                .border(1.dp, Color(0xFF292929), InnerCornerShape)
                .clip(InnerCornerShape)
                .clickable {
                  editOrder = DashboardSection.DEFAULT_ORDER
                  editVisibility = DashboardSection.entries.associateWith { true }
                },
              contentAlignment = Alignment.Center
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
              ) {
                Icon(
                  imageVector = Icons.Default.Refresh,
                  contentDescription = null,
                  tint = InkSecondary,
                  modifier = Modifier.size(16.dp)
                )
                Text("Reset", style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = InkSecondary))
              }
            }

            // Cancel Button
            Box(
              modifier = Modifier
                .weight(1f)
                .height(48.dp)
                .background(Color(0xFF1A1A1A), InnerCornerShape)
                .border(1.dp, Color(0xFF292929), InnerCornerShape)
                .clip(InnerCornerShape)
                .clickable {
                  editOrder = sectionOrder
                  editVisibility = sectionVisibility
                  isDashboardEditMode = false
                },
              contentAlignment = Alignment.Center
            ) {
              Text("Cancel", style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = InkSecondary))
            }

            // Save & Apply Button
            Box(
              modifier = Modifier
                .weight(1.2f)
                .height(48.dp)
                .background(Color(0xFF1A1A1A), InnerCornerShape)
                .border(1.dp, InkPrimary.copy(alpha = 0.4f), InnerCornerShape)
                .clip(InnerCornerShape)
                .clickable {
                  DashboardLayoutManager.saveLayout(context, editOrder, editVisibility)
                  isDashboardEditMode = false
                },
              contentAlignment = Alignment.Center
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
              ) {
                Icon(
                  imageVector = Icons.Default.Check,
                  contentDescription = null,
                  tint = InkPrimary,
                  modifier = Modifier.size(16.dp)
                )
                Text("Save", style = TextStyle(fontFamily = StrongFontFamily, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = InkPrimary))
              }
            }
          }
          Spacer(modifier = Modifier.height(24.dp))
        }
      } else {
        val overviewEnabled = sectionVisibility[DashboardSection.BENTO_HERO] != false ||
          sectionVisibility[DashboardSection.QUICK_METRICS] != false
        if (showOverview && overviewEnabled) {
          val completedTasksCount = todayTasks.count { it.isCompleted }
          val totalTasksCount = todayTasks.size
          val completedHabitsCount = todayHabits.count { completedHabitIds.contains(it.id) }
          val totalHabitsCount = todayHabits.size
          val totalItems = totalTasksCount + totalHabitsCount
          val completedItems = completedTasksCount + completedHabitsCount
          val progressRatio = if (totalItems > 0) completedItems.toFloat() / totalItems.toFloat() else 0f
          val progressPercent = (progressRatio * 100).toInt()

          item {
            Column(
              modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
            ) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = if (totalItems > 0) "$completedItems of $totalItems completed" else "All clear for today",
                  style = TextStyle(
                    fontSize = 13.sp,
                    color = Color(0xFF929292)
                  )
                )
                if (totalItems > 0) {
                  Text(
                    text = "$progressPercent%",
                    style = TextStyle(
                      fontSize = 13.sp,
                      fontWeight = FontWeight.SemiBold,
                      color = Color(0xFFF2F2F2)
                    )
                  )
                }
              }
              Spacer(modifier = Modifier.height(8.dp))
              Box(
                modifier = Modifier
                  .fillMaxWidth()
                  .height(4.dp)
                  .clip(RoundedCornerShape(2.dp))
                  .background(Color(0xFF1A1A1A))
              ) {
                val animatedRatio by animateFloatAsState(
                  targetValue = progressRatio,
                  animationSpec = spring(dampingRatio = 0.8f, stiffness = 400f),
                  label = "homeProgressRatio"
                )
                Box(
                  modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(animatedRatio)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Color(0xFFF2F2F2))
                )
              }
              Spacer(modifier = Modifier.height(10.dp))
              HorizontalDivider(thickness = 0.5.dp, color = Color(0xFF292929))
            }
            Spacer(modifier = Modifier.height(8.dp))
          }
        }

        if (sectionVisibility[DashboardSection.DAILY_PLANNER] != false) {

      // ── Refined Segmented Control ──
      item {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(38.dp)
            .clip(RoundedCornerShape(19.dp))
            .background(Color(0xFF1A1A1A))
            .border(1.dp, Color(0xFF292929), RoundedCornerShape(19.dp))
            .padding(3.dp)
        ) {
          Row(modifier = Modifier.fillMaxSize()) {
            listOf("All", "Habits", "Tasks").forEachIndexed { index, label ->
              val isSelected = selectedTab == index
              Box(
                modifier = Modifier
                  .weight(1f)
                  .fillMaxHeight()
                  .clip(RoundedCornerShape(16.dp))
                  .background(if (isSelected) Color(0xFF4E4E4E) else Color.Transparent)
                  .clickable {
                    com.example.util.HapticManager.lightTap(context)
                    selectedTab = index
                  },
                contentAlignment = Alignment.Center
              ) {
                Text(
                  text = label,
                  style = TextStyle(
                    fontSize = 13.sp,
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                    color = if (isSelected) Color(0xFFF2F2F2) else Color(0xFF929292)
                  )
                )
              }
            }
          }
        }
        Spacer(modifier = Modifier.height(14.dp))
      }

      // TODAY'S TASKS SECTION
      if (selectedTab == 0 || selectedTab == 2) {
        item {
          Text(
            text = "TASKS",
            style = TextStyle(
              fontFamily = StrongFontFamily,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              letterSpacing = 1.5.sp,
              color = Color(0xFF929292)
            ),
            modifier = Modifier
              .sectionEntrance(delayMs = 60)
              .padding(bottom = 10.dp)
          )
        }

        if (todayTasks.isEmpty()) {
          item {
            AppEmptyState(
              icon = Icons.Default.Check,
              title = "Nothing due today",
              subtitle = "Add a task for later or enjoy a clear day.",
              actionText = "Add Task",
              onAction = onNavigateToAddTask,
              modifier = Modifier.padding(vertical = 12.dp)
            )
          }
        } else {
          // List view only — flat rows with dividers
          itemsIndexed(todayTasks, key = { _, task -> task.id }) { index, task ->
            val subtasks = viewModel.getSubtasksForTask(task.id)
            val subtasksDone = subtasks.count { it.isCompleted }

            Box(modifier = Modifier.animateItem()) {
              TaskItemCard(
                task = task,
                subtasksCount = subtasks.size,
                subtasksDone = subtasksDone,
                isEditMode = isDashboardEditMode,
                is24Hour = settings.timeFormat24Hour,
                onToggle = { handleTaskToggle(task) },
                onClick = { onTaskClick(task) },
                onDelete = { taskToDelete = task }
              )
            }
            if (index < todayTasks.size - 1) {
              HorizontalDivider(
                thickness = 0.5.dp,
                color = Color(0xFF292929),
                modifier = Modifier.padding(start = 44.dp)
              )
            }
          }
        }

        item { Spacer(modifier = Modifier.height(18.dp)) }
      }
      } // end DAILY_PLANNER



      // TODAY'S HABITS SECTION
      if (sectionVisibility[DashboardSection.HABIT_STREAKS] != false) {
      if (selectedTab == 0 || selectedTab == 1) {
        item {
          Text(
            text = "HABITS",
            style = TextStyle(
              fontFamily = StrongFontFamily,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              letterSpacing = 1.5.sp,
              color = Color(0xFF929292)
            ),
            modifier = Modifier
              .sectionEntrance(delayMs = 80)
              .padding(bottom = 10.dp)
          )
        }

        if (todayHabits.isEmpty()) {
          item {
            AppEmptyState(
              icon = Icons.Default.Check,
              title = "No habits scheduled today",
              subtitle = "Build daily streaks by setting up recurring habits.",
              actionText = "Add Habit",
              onAction = onNavigateToAddHabit,
              modifier = Modifier.padding(vertical = 12.dp)
            )
          }
        } else {
          // List view only — flat rows with dividers
          itemsIndexed(todayHabits, key = { _, habit -> habit.id }) { index, habit ->
            val isCompleted = completedHabitIds.contains(habit.id)
            val currentSessions = todayHabitRecords[habit.id]?.currentValue?.toInt() ?: 0
            val streakInfo = viewModel.getHabitStreak(habit)

            Box(modifier = Modifier.animateItem()) {
              HabitItemCard(
                habit = habit,
                isCompleted = isCompleted,
                currentSessions = currentSessions,
                streakDays = streakInfo.currentStreak,
                isEditMode = isDashboardEditMode,
                onToggle = {
                  viewModel.toggleHabitToday(habit.id)
                  com.example.util.SoundManager.playHabitTick(context)
                  com.example.util.HapticManager.sliderSnap(context)
                },
                onClick = { onHabitClick(habit) },
                onDelete = { habitToDelete = habit }
              )
            }
            if (index < todayHabits.size - 1) {
              HorizontalDivider(
                thickness = 0.5.dp,
                color = Color(0xFF292929),
                modifier = Modifier.padding(start = 44.dp)
              )
            }
          }
        }
      }
      } // end HABIT_STREAKS
      if (sectionVisibility[DashboardSection.TIME_BLOCKS] != false && scheduledTasks.isNotEmpty()) {
        // TIME BLOCKS SECTION — flat list
        item {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 8.dp)
          ) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "TIME BLOCKS",
                style = TextStyle(
                  fontFamily = StrongFontFamily,
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  letterSpacing = 1.5.sp,
                  color = Color(0xFF929292)
                )
              )
              Text(
                text = "${scheduledTasks.size} Scheduled",
                style = TextStyle(fontSize = 12.sp, color = InkSecondary)
              )
            }
            Spacer(modifier = Modifier.height(10.dp))
            scheduledTasks.take(4).forEachIndexed { index, task ->
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .clickable { onTaskClick(task) }
                  .padding(vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
              ) {
                Text(
                  text = task.dueTime ?: "",
                  style = TextStyle(
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = InkSecondary,
                    fontFamily = StrongFontFamily
                  ),
                  modifier = Modifier.width(56.dp)
                )
                Text(
                  text = task.title,
                  style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (task.isCompleted) InkSecondary else InkPrimary,
                    textDecoration = if (task.isCompleted) TextDecoration.LineThrough else null
                  ),
                  maxLines = 1,
                  modifier = Modifier.weight(1f)
                )
              }
              if (index < scheduledTasks.take(4).size - 1) {
                HorizontalDivider(thickness = 0.5.dp, color = Color(0xFF292929))
              }
            }
          }
          Spacer(modifier = Modifier.height(18.dp))
        }
      }
      } // end else (!isDashboardEditMode)
    }

    // Task Delete Confirmation Dialog
    taskToDelete?.let { task ->
      DeleteConfirmationDialog(
        title = "Delete Task?",
        message = "This cannot be undone. Are you sure you want to delete \"${task.title}\"?",
        onConfirm = {
          viewModel.deleteTask(task.id)
          taskToDelete = null
        },
        onDismiss = { taskToDelete = null }
      )
    }

    // Habit Delete Confirmation Dialog
    habitToDelete?.let { habit ->
      DeleteConfirmationDialog(
        title = "Delete Habit?",
        message = "This cannot be undone. Are you sure you want to delete \"${habit.name}\"?",
        onConfirm = {
          viewModel.deleteHabit(habit.id)
          habitToDelete = null
        },
        onDismiss = { habitToDelete = null }
      )
    }

    // Floating Undo Confirmation Pill
    UndoConfirmationPill(
      visible = showUndoPill,
      message = "Task completed",
      onUndo = {
        lastCompletedTaskId?.let { id ->
          viewModel.toggleTaskCompletion(id)
        }
        showUndoPill = false
      },
      modifier = Modifier
        .align(Alignment.BottomCenter)
        .padding(bottom = FloatingNavContentBottomPadding + 8.dp)
    )
  }
}

@Composable
fun TaskItemCard(
  task: Task,
  subtasksCount: Int,
  subtasksDone: Int,
  onToggle: () -> Unit,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  isEditMode: Boolean = false,
  is24Hour: Boolean = true,
  onLongClick: (() -> Unit)? = null,
  onDelete: (() -> Unit)? = null
) {
  val textColor by animateColorAsState(
    targetValue = if (task.isCompleted) InkSecondary else InkPrimary,
    animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing),
    label = "taskTextCol"
  )

  Row(
    modifier = (modifier ?: Modifier)
      .fillMaxWidth()
      .clickable { onClick() }
      .padding(horizontal = 0.dp, vertical = 12.dp)
      .testTag("task_card_${task.id}"),
    verticalAlignment = Alignment.CenterVertically
  ) {
    // 1. Left: Apple Circular Checkbox
    if (!isEditMode) {
      AppleCircularCheckbox(
        checked = task.isCompleted,
        onCheckedChange = { onToggle() },
        modifier = Modifier.padding(end = 12.dp),
        contentDescription = "Complete ${task.title}"
      )
    }

    // 2. Center: Task Title & Metadata
    Column(modifier = Modifier.weight(1f).padding(end = 8.dp)) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        if (task.colorTag != null) {
          val tag = com.example.data.model.AppColorTag.fromId(task.colorTag)
          if (tag != null) {
            Box(
              modifier = Modifier
                .padding(end = 6.dp)
                .size(8.dp)
                .clip(CircleShape)
                .background(tag.color)
            )
          }
        }
        Text(
          text = task.title,
          style = TextStyle(
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            color = textColor,
            textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None
          )
        )
      }

      Spacer(modifier = Modifier.height(3.dp))

      Row(verticalAlignment = Alignment.CenterVertically) {
        val formattedTime = if (task.dueTime.isNotBlank()) formatTimeDisplay(task.dueTime, is24Hour) else ""
        val categoryAndTime = if (formattedTime.isNotBlank()) "${task.category} • $formattedTime" else task.category
        Text(
          text = categoryAndTime,
          style = TextStyle(
            fontSize = 12.sp,
            color = Color(0xFF929292)
          )
        )

        if (subtasksCount > 0) {
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "$subtasksDone/$subtasksCount",
            style = TextStyle(fontSize = 10.sp, fontWeight = FontWeight.Bold, color = InkSecondary)
          )
        }

        if (task.priority == TaskPriority.HIGH) {
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "HIGH",
            style = TextStyle(
              fontSize = 9.sp,
              fontWeight = FontWeight.Bold,
              letterSpacing = 0.5.sp,
              color = InkSecondary
            )
          )
        }

        if (task.isShortTerm) {
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "Scratch",
            style = TextStyle(fontSize = 9.sp, fontWeight = FontWeight.Medium, color = InkSecondary)
          )
        }
      }
    }

    // 3. Right: Edit Mode Actions
    if (isEditMode) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
          modifier = Modifier
            .size(40.dp)
            .border(1.dp, Color(0xFF292929), CircleShape)
            .clip(CircleShape)
            .clickable { onClick() },
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.Edit,
            contentDescription = "Edit Task",
            tint = InkPrimary,
            modifier = Modifier.size(15.dp)
          )
        }
        if (onDelete != null) {
          Spacer(modifier = Modifier.width(8.dp))
          Box(
            modifier = Modifier
              .size(40.dp)
              .background(InkSecondary.copy(alpha = 0.12f), CircleShape)
              .clip(CircleShape)
              .clickable { onDelete() },
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Outlined.DeleteOutline,
              contentDescription = "Delete Task",
              tint = InkSecondary,
              modifier = Modifier.size(16.dp)
            )
          }
        }
      }
    }
  }
}

@Composable
fun TaskGridCard(
  task: Task,
  subtasksCount: Int,
  subtasksDone: Int,
  onToggle: () -> Unit,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  isEditMode: Boolean = false,
  is24Hour: Boolean = true,
  onLongClick: (() -> Unit)? = null,
  onDelete: (() -> Unit)? = null
) {
  val textColor by animateColorAsState(
    targetValue = if (task.isCompleted) InkMuted else InkPrimary,
    animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing),
    label = "gridTaskTextCol"
  )
  val cardAlpha by animateFloatAsState(
    targetValue = if (task.isCompleted) 0.70f else 1.0f,
    animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing),
    label = "gridTaskAlpha"
  )

  Box(
    modifier = modifier
      .fillMaxWidth()
      .alpha(cardAlpha)
      .animateContentSize(animationSpec = spring(dampingRatio = 0.75f, stiffness = 420f))
      .background(Color(0xFF1A1A1A), RoundedCornerShape(14.dp))
      .border(0.75.dp, Color(0xFF292929), RoundedCornerShape(14.dp))
      .clip(RoundedCornerShape(14.dp))
      .clickable { onClick() }
      .testTag("task_grid_card_${task.id}")
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(12.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
      ) {
        Text(
          text = task.category,
          style = TextStyle(fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = InkSecondary)
        )

        if (isEditMode) {
          if (onDelete != null) {
            Box(
              modifier = Modifier
                .defaultMinSize(minWidth = 44.dp, minHeight = 44.dp)
                .clickable { onDelete() },
              contentAlignment = Alignment.Center
            ) {
              Box(
                modifier = Modifier
                  .size(24.dp)
                  .background(InkSecondary.copy(alpha = 0.15f), CircleShape)
                  .clip(CircleShape),
                contentAlignment = Alignment.Center
              ) {
                Icon(Icons.Outlined.DeleteOutline, "Delete", tint = InkSecondary, modifier = Modifier.size(14.dp))
              }
            }
          }
        } else {
          GlassCheckbox(
            checked = task.isCompleted,
            onCheckedChange = { onToggle() },
            sizeDp = 20.dp,
            contentDescription = "Complete ${task.title}"
          )
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = task.title,
        style = TextStyle(
          fontSize = 14.sp,
          fontWeight = FontWeight.SemiBold,
          color = textColor,
          textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None
        ),
        maxLines = 2
      )

      if (task.dueTime.isNotBlank()) {
        Spacer(modifier = Modifier.height(6.dp))
        Text(
          text = formatTimeDisplay(task.dueTime, is24Hour),
          style = TextStyle(fontSize = 11.sp, color = InkMuted)
        )
      }
    }
  }
}

@Composable
fun HabitGridCard(
  habit: Habit,
  isCompleted: Boolean,
  streakDays: Int,
  onToggle: () -> Unit,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  currentSessions: Int = 0,
  isEditMode: Boolean = false,
  onDelete: (() -> Unit)? = null
) {
  val cardAlpha by animateFloatAsState(
    targetValue = if (isCompleted) 0.70f else 1.0f,
    animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing),
    label = "gridHabitAlpha"
  )

  Box(
    modifier = modifier
      .fillMaxWidth()
      .alpha(cardAlpha)
      .background(Color(0xFF1A1A1A), RoundedCornerShape(14.dp))
      .border(0.75.dp, Color(0xFF292929), RoundedCornerShape(14.dp))
      .clip(RoundedCornerShape(14.dp))
      .clickable { onClick() }
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(12.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
      ) {
        if (streakDays > 0) {
          Text(
            text = "${streakDays}d",
            style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Bold, color = InkSecondary)
          )
        } else {
          Spacer(Modifier.size(1.dp))
        }

        if (isEditMode) {
          if (onDelete != null) {
            Box(
              modifier = Modifier
                .defaultMinSize(minWidth = 44.dp, minHeight = 44.dp)
                .clickable { onDelete() },
              contentAlignment = Alignment.Center
            ) {
              Box(
                modifier = Modifier
                  .size(24.dp)
                  .background(InkSecondary.copy(alpha = 0.15f), CircleShape)
                  .clip(CircleShape),
                contentAlignment = Alignment.Center
              ) {
                Icon(Icons.Outlined.DeleteOutline, "Delete", tint = InkSecondary, modifier = Modifier.size(14.dp))
              }
            }
          }
        } else {
          if (habit.targetValue > 1f && !isCompleted) {
            // Multi-session counter pill
            Box(
              modifier = Modifier
                .height(28.dp)
                .background(InkSecondary.copy(alpha = 0.10f), RoundedCornerShape(14.dp))
                .border(1.dp, Color(0xFF292929), RoundedCornerShape(14.dp))
                .clip(RoundedCornerShape(14.dp))
                .clickable { onToggle() }
                .padding(horizontal = 8.dp, vertical = 4.dp),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = "+$currentSessions/${habit.targetValue.toInt()}",
                style = TextStyle(
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  color = InkPrimary
                )
              )
            }
          } else {
            GlassCheckbox(
              checked = isCompleted,
              onCheckedChange = { onToggle() },
              sizeDp = 26.dp,
              contentDescription = "Complete ${habit.name}"
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = habit.name,
        style = TextStyle(
          fontSize = 14.sp,
          fontWeight = FontWeight.SemiBold,
          color = if (isCompleted) InkMuted else InkPrimary,
          textDecoration = if (isCompleted) TextDecoration.LineThrough else TextDecoration.None
        ),
        maxLines = 2
      )

      Spacer(modifier = Modifier.height(6.dp))
      Text(
        text = if (habit.targetValue > 1f) {
          if (isCompleted) "${habit.targetValue.toInt()}/${habit.targetValue.toInt()} done"
          else "$currentSessions/${habit.targetValue.toInt()} session"
        } else habit.category,
        style = TextStyle(fontSize = 11.sp, color = InkMuted)
      )
    }
  }
}

@Composable
fun HabitItemCard(
  habit: Habit,
  isCompleted: Boolean,
  streakDays: Int,
  onToggle: () -> Unit,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  currentSessions: Int = 0,
  isEditMode: Boolean = false,
  onDelete: (() -> Unit)? = null
) {
  val textColor by animateColorAsState(
    targetValue = if (isCompleted) InkSecondary else InkPrimary,
    animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing),
    label = "habitTextCol"
  )

  Row(
    modifier = (modifier ?: Modifier)
      .fillMaxWidth()
      .clickable { onClick() }
      .padding(horizontal = 0.dp, vertical = 12.dp)
      .testTag("habit_card_${habit.id}"),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Column(modifier = Modifier.weight(1f).padding(end = 8.dp)) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        if (habit.colorTag != null) {
          val tag = com.example.data.model.AppColorTag.fromId(habit.colorTag)
          if (tag != null) {
            Box(
              modifier = Modifier
                .padding(end = 6.dp)
                .size(10.dp)
                .clip(CircleShape)
                .background(tag.color)
            )
          }
        }
        Text(
          text = habit.name,
          style = TextStyle(
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = textColor,
            textDecoration = if (isCompleted) TextDecoration.LineThrough else TextDecoration.None
          )
        )

        if (streakDays > 0) {
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "${streakDays}d",
            style = TextStyle(
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold,
              color = InkSecondary
            )
          )
        }
      }

      Spacer(modifier = Modifier.height(4.dp))

      val subtitleText = if (habit.targetValue > 1f) {
        if (isCompleted) {
          "${habit.targetValue.toInt()}/${habit.targetValue.toInt()} sessions completed"
        } else if (currentSessions > 0) {
          "$currentSessions/${habit.targetValue.toInt()} sessions • ${(habit.targetValue.toInt() - currentSessions)} remaining"
        } else {
          "${habit.targetValue.toInt()} ${habit.targetUnit} daily"
        }
      } else {
        "${habit.category} • ${habit.frequency.displayName}"
      }

      Text(
        text = subtitleText,
        style = TextStyle(
          fontSize = 13.sp,
          color = Color(0xFF929292)
        )
      )
    }

    if (isEditMode) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
          modifier = Modifier
            .size(48.dp)
            .border(1.2.dp, Color(0xFF292929), CircleShape)
            .clip(CircleShape)
            .clickable { onClick() },
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.Edit,
            contentDescription = "Edit Habit",
            tint = InkPrimary,
            modifier = Modifier.size(16.dp)
          )
        }
        if (onDelete != null) {
          Spacer(modifier = Modifier.width(8.dp))
          Box(
            modifier = Modifier
              .size(48.dp)
              .background(InkSecondary.copy(alpha = 0.15f), CircleShape)
              .clip(CircleShape)
              .clickable { onDelete() },
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Outlined.DeleteOutline,
              contentDescription = "Delete Habit",
              tint = InkSecondary,
              modifier = Modifier.size(18.dp)
            )
          }
        }
      }
    } else {
      if (habit.targetValue > 1f && !isCompleted) {
        // Flat multi-session logging button
        Box(
          modifier = Modifier
            .height(48.dp)
            .background(InkSecondary.copy(alpha = 0.10f), RoundedCornerShape(17.dp))
            .border(1.2.dp, Color(0xFF292929), RoundedCornerShape(17.dp))
            .clip(RoundedCornerShape(17.dp))
            .clickable { onToggle() }
            .padding(horizontal = 10.dp, vertical = 6.dp),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = "$currentSessions/${habit.targetValue.toInt()} Session",
            style = TextStyle(
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              color = InkPrimary
            )
          )
        }
      } else {
        GlassCheckbox(
          checked = isCompleted,
          contentDescription = "Complete ${habit.name}",
          onCheckedChange = { onToggle() }
        )
      }
    }
  }
}
