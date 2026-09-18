package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.presets.BulkingPresets
import com.example.data.presets.RecoveryProtocol
import com.example.ui.components.CyberCard
import com.example.ui.theme.CyberBlack
import com.example.ui.theme.CyberBorder
import com.example.ui.theme.CyberCardBg
import com.example.ui.theme.NeonAmber
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.NeonPink
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.FitnessViewModel

@Composable
fun RecoveryScreen(
    viewModel: FitnessViewModel,
    modifier: Modifier = Modifier
) {
    val profile by viewModel.userProfile.collectAsState()
    val todayRecovery by viewModel.todayRecovery.collectAsState()

    var sleepHours by remember(todayRecovery.sleepHours) { mutableFloatStateOf(todayRecovery.sleepHours) }
    var sorenessLevel by remember(todayRecovery.sorenessLevel) { mutableIntStateOf(todayRecovery.sorenessLevel) }
    var isRestDay by remember(todayRecovery.isRestDay) { mutableStateOf(todayRecovery.isRestDay) }
    var expandedProtocolId by remember { mutableStateOf<String?>(null) }

    // Recovery Score Calculation
    val sleepScore = (sleepHours / 8.5f * 50f).coerceIn(10f, 50f)
    val sorenessScore = ((6 - sorenessLevel) * 10f).coerceIn(10f, 50f)
    val totalRecoveryScore = (sleepScore + sorenessScore).toInt()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(CyberBlack),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        item {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(NeonPurple, RoundedCornerShape(4.dp))
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "ANABOLIC SYNTHESIS HUB",
                        color = NeonPurple,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
                Text(
                    text = "RECOVERY & REST PROTOCOLS",
                    color = TextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.Monospace
                )
            }
        }

        // Rest Day Reminder & Alert Card
        item {
            CyberCard(borderColor = NeonCyan) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.NotificationsActive,
                                contentDescription = null,
                                tint = NeonCyan,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "REST DAY REMINDERS",
                                    color = NeonCyan,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace
                                )
                                Text(
                                    text = if (profile.restDayRemindersEnabled) "Growth alerts enabled" else "Reminders muted",
                                    color = TextMuted,
                                    fontSize = 10.sp
                                )
                            }
                        }

                        Switch(
                            checked = profile.restDayRemindersEnabled,
                            onCheckedChange = { viewModel.toggleRestDayReminders(it) },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = NeonCyan,
                                checkedTrackColor = NeonCyan.copy(alpha = 0.3f),
                                uncheckedThumbColor = TextMuted,
                                uncheckedTrackColor = CyberCardBg
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Mark today as Rest Day toggle
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(CyberCardBg)
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "TODAY'S STATUS",
                                color = TextMuted,
                                fontSize = 9.sp,
                                fontFamily = FontFamily.Monospace
                            )
                            Text(
                                text = if (isRestDay) "🔥 ACTIVE REST & MUSCLE REBUILD" else "⚡ GYM TRAINING DAY",
                                color = if (isRestDay) NeonPurple else NeonGreen,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                        }

                        Switch(
                            checked = isRestDay,
                            onCheckedChange = {
                                isRestDay = it
                                viewModel.updateRecovery(sleepHours, sorenessLevel, isRestDay)
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = NeonPurple,
                                checkedTrackColor = NeonPurple.copy(alpha = 0.3f)
                            )
                        )
                    }
                }
            }
        }

        // Deep Sleep Tracker & HGH Score
        item {
            CyberCard(borderColor = NeonPurple) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Bedtime, contentDescription = null, tint = NeonPurple, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "DEEP SLEEP GROWTH DURATION",
                                color = NeonPurple,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                        }

                        Text(
                            text = String.format("%.1f HOURS", sleepHours),
                            color = TextPrimary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Slider(
                        value = sleepHours,
                        onValueChange = {
                            sleepHours = it
                            viewModel.updateRecovery(sleepHours, sorenessLevel, isRestDay)
                        },
                        valueRange = 4f..11f,
                        steps = 13,
                        colors = SliderDefaults.colors(
                            thumbColor = NeonPurple,
                            activeTrackColor = NeonPurple,
                            inactiveTrackColor = CyberCardBg
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("4h (Catabolic)", color = TextMuted, fontSize = 9.sp)
                        Text("8.5h (Optimal Anabolic)", color = NeonGreen, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        Text("11h (Max)", color = TextMuted, fontSize = 9.sp)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = if (sleepHours >= 8.0f) "✅ Optimum Natural Growth Hormone release threshold attained."
                        else "⚠️ Sleep under 7 hours reduces testosterone by up to 15% and increases muscle catabolism.",
                        color = if (sleepHours >= 8.0f) NeonGreen else NeonPink,
                        fontSize = 11.sp,
                        lineHeight = 15.sp
                    )
                }
            }
        }

        // Muscle Soreness / DOMS Indicator
        item {
            CyberCard(borderColor = NeonPink) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "MUSCLE FIBER SORENESS (DOMS)",
                            color = NeonPink,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = when (sorenessLevel) {
                                1 -> "1/5: FRESH"
                                2 -> "2/5: MINOR"
                                3 -> "3/5: MODERATE"
                                4 -> "4/5: HEAVY"
                                else -> "5/5: MAX DOMS"
                            },
                            color = when (sorenessLevel) {
                                1, 2 -> NeonGreen
                                3 -> NeonAmber
                                else -> NeonPink
                            },
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        (1..5).forEach { level ->
                            val isSelected = sorenessLevel == level
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(
                                        if (isSelected) {
                                            when (level) {
                                                1, 2 -> NeonGreen.copy(alpha = 0.3f)
                                                3 -> NeonAmber.copy(alpha = 0.3f)
                                                else -> NeonPink.copy(alpha = 0.3f)
                                            }
                                        } else CyberCardBg
                                    )
                                    .border(
                                        1.dp,
                                        if (isSelected) {
                                            when (level) {
                                                1, 2 -> NeonGreen
                                                3 -> NeonAmber
                                                else -> NeonPink
                                            }
                                        } else CyberBorder,
                                        RoundedCornerShape(8.dp)
                                    )
                                    .clickable {
                                        sorenessLevel = level
                                        viewModel.updateRecovery(sleepHours, sorenessLevel, isRestDay)
                                    }
                                    .padding(vertical = 10.dp)
                            ) {
                                Text(
                                    text = "$level",
                                    color = if (isSelected) TextPrimary else TextMuted,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        }
                    }
                }
            }
        }

        // Rest Day Daily Anabolism Checklist
        item {
            CyberCard(borderColor = NeonGreen) {
                Column {
                    Text(
                        text = "REST DAY ANABOLISM CHECKLIST",
                        color = NeonGreen,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    ChecklistItem("Surplus Fuel", "Eat 100% of your bulking calorie target today (Muscles grow during rest!).")
                    ChecklistItem("Protein Pacing", "Consume 30-40g protein every 3-4 hours to keep MPS elevated.")
                    ChecklistItem("Deep Hydration", "Drink 3.5 liters of water to hydrate muscle glycogen cells.")
                    ChecklistItem("Active Walking", "20-30 minutes low-intensity walk to flush lactic metabolic waste.")
                }
            }
        }

        // Recovery Protocols & Guides
        item {
            Text(
                text = "HYPERTROPHY RECOVERY PROTOCOLS",
                color = NeonCyan,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            )
        }

        items(BulkingPresets.RECOVERY_PROTOCOLS) { proto ->
            val isExpanded = expandedProtocolId == proto.id
            CyberCard(borderColor = CyberBorder) {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                expandedProtocolId = if (isExpanded) null else proto.id
                            },
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Box(
                                modifier = Modifier
                                    .background(NeonCyan.copy(alpha = 0.15f), RoundedCornerShape(4.dp))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = proto.tag,
                                    color = NeonCyan,
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = proto.title,
                                color = TextPrimary,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = proto.summary,
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                        }

                        Icon(
                            imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = null,
                            tint = NeonCyan,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    AnimatedVisibility(visible = isExpanded) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp)
                                .background(Color(0xFF0C1322), RoundedCornerShape(8.dp))
                                .padding(10.dp)
                        ) {
                            proto.detailedTips.forEach { tip ->
                                Row(
                                    modifier = Modifier.padding(vertical = 3.dp),
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Text("⚡", fontSize = 11.sp)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(tip, color = TextPrimary, fontSize = 11.sp, lineHeight = 16.sp)
                                }
                            }
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
private fun ChecklistItem(title: String, desc: String) {
    var checked by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { checked = !checked },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
                .background(if (checked) NeonGreen else CyberCardBg)
                .border(1.dp, if (checked) NeonGreen else CyberBorder, CircleShape)
        ) {
            if (checked) {
                Icon(Icons.Default.Check, contentDescription = null, tint = CyberBlack, modifier = Modifier.size(14.dp))
            }
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(
                text = title,
                color = if (checked) TextMuted else TextPrimary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = desc,
                color = TextSecondary,
                fontSize = 10.sp,
                lineHeight = 14.sp
            )
        }
    }
}
