package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.screens.CoachScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.MealsScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.RecoveryScreen
import com.example.ui.screens.WorkoutScreen
import com.example.ui.theme.CyberBlack
import com.example.ui.theme.CyberBorder
import com.example.ui.theme.CyberDarkSurface
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.NeonAmber
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.viewmodel.FitnessViewModel

enum class FitnessNavTab(val label: String, val icon: ImageVector, val accentColor: Color) {
    HOME("HUD", Icons.Default.Home, NeonGreen),
    MEALS("FUEL", Icons.Default.Restaurant, NeonAmber),
    WORKOUT("LIFT", Icons.Default.FitnessCenter, NeonCyan),
    RECOVERY("REST", Icons.Default.Bedtime, NeonPurple),
    COACH("TITAN", Icons.Default.AutoAwesome, NeonGreen),
    PROFILE("PROFILE", Icons.Default.Person, NeonCyan)
}

class MainActivity : ComponentActivity() {

    private val viewModel: FitnessViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                FitnessApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun FitnessApp(viewModel: FitnessViewModel) {
    var currentTab by remember { mutableStateOf(FitnessNavTab.HOME) }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(CyberBlack),
        bottomBar = {
            CyberBottomNavigation(
                currentTab = currentTab,
                onTabSelected = { currentTab = it }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Crossfade(targetState = currentTab, label = "tab_crossfade") { tab ->
                when (tab) {
                    FitnessNavTab.HOME -> HomeScreen(
                        viewModel = viewModel,
                        onNavigateToWorkouts = { currentTab = FitnessNavTab.WORKOUT },
                        onNavigateToMeals = { currentTab = FitnessNavTab.MEALS },
                        onNavigateToProfile = { currentTab = FitnessNavTab.PROFILE }
                    )
                    FitnessNavTab.MEALS -> MealsScreen(viewModel = viewModel)
                    FitnessNavTab.WORKOUT -> WorkoutScreen(viewModel = viewModel)
                    FitnessNavTab.RECOVERY -> RecoveryScreen(viewModel = viewModel)
                    FitnessNavTab.COACH -> CoachScreen(viewModel = viewModel)
                    FitnessNavTab.PROFILE -> ProfileScreen(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun CyberBottomNavigation(
    currentTab: FitnessNavTab,
    onTabSelected: (FitnessNavTab) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(CyberDarkSurface)
            .border(
                1.dp,
                Brush.horizontalGradient(listOf(NeonCyan.copy(alpha = 0.4f), NeonGreen.copy(alpha = 0.4f))),
                RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
            )
            .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
            .windowInsetsPadding(WindowInsets.navigationBars)
    ) {
        NavigationBar(
            containerColor = CyberDarkSurface,
            contentColor = TextPrimary,
            tonalElevation = 0.dp,
            modifier = Modifier.height(64.dp)
        ) {
            FitnessNavTab.entries.forEach { tab ->
                val isSelected = currentTab == tab
                val color = tab.accentColor

                NavigationBarItem(
                    selected = isSelected,
                    onClick = { onTabSelected(tab) },
                    icon = {
                        Icon(
                            imageVector = tab.icon,
                            contentDescription = tab.label,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    label = {
                        Text(
                            text = tab.label,
                            fontSize = 9.sp,
                            fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Normal,
                            fontFamily = FontFamily.Monospace,
                            letterSpacing = 0.8.sp
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = color,
                        selectedTextColor = color,
                        unselectedIconColor = TextMuted,
                        unselectedTextColor = TextMuted,
                        indicatorColor = color.copy(alpha = 0.18f)
                    ),
                    modifier = Modifier.testTag("nav_tab_${tab.name.lowercase()}")
                )
            }
        }
    }
}
