package com.example

import com.example.ui.navigation.AppNavDestination
import com.example.ui.navigation.AppNavOverlay
import com.example.ui.navigation.PredictiveNavController
import com.example.ui.navigation.PredictiveBackTarget
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class PredictiveBackGestureTest {

  @Test
  fun rootDestinationHasNoBackTarget() {
    val nav = PredictiveNavController(AppNavDestination.Main(0))
    assertEquals(PredictiveBackTarget.NONE, nav.currentBackTarget)
    assertFalse(nav.canPredictiveBack)
    assertFalse(nav.canNavigateBack)
  }

  @Test
  fun secondaryTabTargetsTabRoot() {
    val nav = PredictiveNavController(AppNavDestination.Main(0))
    nav.switchTab(1) // Productivity
    assertEquals(PredictiveBackTarget.TAB_ROOT, nav.currentBackTarget)
    assertTrue(nav.canPredictiveBack)
    assertTrue(nav.canNavigateBack)

    nav.switchTab(2) // Habits
    assertEquals(PredictiveBackTarget.TAB_ROOT, nav.currentBackTarget)

    nav.switchTab(3) // Insights
    assertEquals(PredictiveBackTarget.TAB_ROOT, nav.currentBackTarget)

    nav.switchTab(0) // Today
    assertEquals(PredictiveBackTarget.NONE, nav.currentBackTarget)
  }

  @Test
  fun stackDestinationsTargetStack() {
    val nav = PredictiveNavController(AppNavDestination.Main(0))
    nav.navigate(AppNavDestination.Settings)
    assertEquals(PredictiveBackTarget.STACK_DESTINATION, nav.currentBackTarget)
    assertTrue(nav.canPredictiveBack)
    assertEquals(2, nav.backStack.size)

    nav.navigate(AppNavDestination.Diagnostics)
    assertEquals(PredictiveBackTarget.STACK_DESTINATION, nav.currentBackTarget)
    assertEquals(3, nav.backStack.size)

    nav.popBackStackDirect()
    assertEquals(2, nav.backStack.size)
    assertEquals(AppNavDestination.Settings, nav.currentDestination)

    nav.popBackStackDirect()
    assertEquals(1, nav.backStack.size)
    assertEquals(AppNavDestination.Main(0), nav.currentDestination)
    assertEquals(PredictiveBackTarget.NONE, nav.currentBackTarget)
  }

  @Test
  fun overlayTakesPrecedenceOverStackAndTabs() {
    val nav = PredictiveNavController(AppNavDestination.Main(0))
    nav.switchTab(2)
    nav.navigate(AppNavDestination.Settings)
    // Both tab != 0 and stack.size > 1, now open overlay
    nav.navigate(AppNavDestination.Search) // Maps to activeOverlay = Search
    assertEquals(PredictiveBackTarget.OVERLAY, nav.currentBackTarget)
    assertEquals(AppNavOverlay.Search, nav.activeOverlay)

    nav.dismissOverlay()
    assertNull(nav.activeOverlay)
    assertEquals(PredictiveBackTarget.STACK_DESTINATION, nav.currentBackTarget)

    nav.navigateBack()
    assertEquals(PredictiveBackTarget.TAB_ROOT, nav.currentBackTarget)

    nav.navigateBack()
    assertEquals(PredictiveBackTarget.NONE, nav.currentBackTarget)
  }

  @Test
  fun gestureStateTracksCommitmentAndEdge() {
    val nav = PredictiveNavController(AppNavDestination.Main(0))
    val state = nav.gestureState

    assertFalse(state.isPredictingBack)
    assertFalse(state.isCommitted)

    state.isPredictingBack = true
    state.isCommitted = true
    state.progress = 0.65f

    assertTrue(state.isPredictingBack)
    assertTrue(state.isCommitted)
    assertEquals(0.65f, state.progress, 0.001f)
  }
}

