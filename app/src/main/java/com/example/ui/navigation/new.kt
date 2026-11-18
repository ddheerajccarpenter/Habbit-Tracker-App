package com.example.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.AppDestination

@Composable
fun NewNavigationBar(
    destinations: List<AppDestination>,
    selectedTab: Int,
    onDestinationSelected: (AppDestination) -> Unit,
    modifier: Modifier = Modifier
) {
    AppNavigationBar(
        destinations = destinations,
        selectedTab = selectedTab,
        onDestinationSelected = onDestinationSelected,
        modifier = modifier
    )
}

@Composable
fun NewFloatingAddButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    AppFloatingAddButton(
        onClick = onClick,
        modifier = modifier
    )
}
