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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.presets.BulkingExercise
import com.example.data.presets.BulkingPresets
import com.example.data.presets.WorkoutRoutine
import com.example.ui.components.CyberCard
import com.example.ui.components.HolographicBody3D
import com.example.ui.components.RestTimerDialog
import com.example.ui.theme.CyberBlack
import com.example.ui.theme.CyberBorder
import com.example.ui.theme.CyberCardBg
import com.example.ui.theme.CyberDarkSurface
import com.example.ui.theme.NeonAmber
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.NeonPink
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.FitnessViewModel

@Composable
fun WorkoutScreen(
    viewModel: FitnessViewModel,
    modifier: Modifier = Modifier
) {
    val activeRoutine by viewModel.activeRoutine.collectAsState()
    val completedSets by viewModel.completedSets.collectAsState()
    val workoutLogs by viewModel.workoutLogs.collectAsState()

    var selectedRoutineId by remember { mutableStateOf("r_push") }
    var showRestTimer by remember { mutableStateOf(false) }
    var restTimerSeconds by remember { mutableIntStateOf(90) }
    var selectedMuscleFilter by remember { mutableStateOf("ALL") }

    val currentRoutine = remember(selectedRoutineId) {
        BulkingPresets.WORKOUT_ROUTINES.firstOrNull { it.id == selectedRoutineId }
            ?: BulkingPresets.WORKOUT_ROUTINES.first()
    }

    if (showRestTimer) {
        RestTimerDialog(
            initialSeconds = restTimerSeconds,
            onDismiss = { showRestTimer = false }
        )
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(CyberBlack),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(NeonGreen, RoundedCornerShape(4.dp))
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "HYPERTROPHY WAR ROOM",
                            color = NeonGreen,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.2.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                    Text(
                        text = "BULKING WORKOUT ROUTINES",
                        color = TextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace
                    )
                }

                Button(
                    onClick = {
                        restTimerSeconds = 90
                        showRestTimer = true
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CyberCardBg,
                        contentColor = NeonCyan
                    ),
                    shape = RoundedCornerShape(10.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, NeonCyan.copy(alpha = 0.5f)),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Icon(Icons.Default.Timer, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("REST TIMER", fontSize = 10.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                }
            }
        }

        // Active Live Workout Session Banner (If in progress)
        if (activeRoutine != null) {
            item {
                ActiveWorkoutSessionBanner(
                    routine = activeRoutine!!,
                    completedSetsCount = completedSets.size,
                    totalVolumeKg = completedSets.sumOf { (it.weightKg * it.reps).toDouble() }.toFloat(),
                    onOpenRestTimer = { sec ->
                        restTimerSeconds = sec
                        showRestTimer = true
                    },
                    onFinishWorkout = { duration ->
                        viewModel.finishWorkoutSession(duration)
                    },
                    onCancelWorkout = {
                        viewModel.cancelWorkoutSession()
                    }
                )
            }
        }

        // Routine Selector Pills
        item {
            Column {
                Text(
                    text = "SELECT BULKING PROGRAM",
                    color = NeonCyan,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(BulkingPresets.WORKOUT_ROUTINES) { r ->
                        val isSelected = r.id == selectedRoutineId
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) NeonGreen.copy(alpha = 0.2f) else CyberCardBg)
                                .border(
                                    1.dp,
                                    if (isSelected) NeonGreen else CyberBorder,
                                    RoundedCornerShape(12.dp)
                                )
                                .clickable { selectedRoutineId = r.id }
                                .padding(horizontal = 14.dp, vertical = 10.dp)
                        ) {
                            Column {
                                Text(
                                    text = r.title.split(":")[0],
                                    color = if (isSelected) NeonGreen else TextPrimary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace
                                )
                                Text(
                                    text = "${r.daysPerWeek} Days/wk • ${r.difficulty}",
                                    color = TextMuted,
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        // Current Routine Details Overview Card
        item {
            CyberCard(borderColor = NeonGreen) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = currentRoutine.title,
                                color = TextPrimary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = currentRoutine.subtitle,
                                color = NeonCyan,
                                fontSize = 12.sp
                            )
                        }

                        if (activeRoutine?.id != currentRoutine.id) {
                            Button(
                                onClick = { viewModel.startWorkoutSession(currentRoutine) },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = NeonGreen,
                                    contentColor = CyberBlack
                                ),
                                shape = RoundedCornerShape(10.dp),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("START", fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, fontFamily = FontFamily.Monospace)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        currentRoutine.focusMuscles.forEach { muscle ->
                            Box(
                                modifier = Modifier
                                    .background(NeonPink.copy(alpha = 0.2f), RoundedCornerShape(6.dp))
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    text = muscle,
                                    color = NeonPink,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "⚡ Bulking Rule: Hypertrophy for hardgainers relies on progressive tension in the 6-10 rep range with 90-150s rest periods to allow maximal ATP-CP creatine recovery.",
                        color = TextSecondary,
                        fontSize = 11.sp,
                        lineHeight = 15.sp
                    )
                }
            }
        }

        // 3D Anatomy Focus for this routine
        item {
            HolographicBody3D(
                selectedMuscle = currentRoutine.focusMuscles.firstOrNull() ?: "ALL",
                onMuscleSelected = { selectedMuscleFilter = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
            )
        }

        // Exercise List Header
        item {
            Text(
                text = "HYPERTROPHY EXERCISES (${currentRoutine.exercises.size})",
                color = NeonGreen,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            )
        }

        // Exercises
        items(currentRoutine.exercises) { ex ->
            ExerciseBulkingCard(
                exercise = ex,
                isSessionActive = activeRoutine != null,
                onLogSet = { weight, reps ->
                    val nextSetNum = completedSets.count { it.exerciseName == ex.name } + 1
                    viewModel.logWorkoutSet(ex.name, nextSetNum, weight, reps)
                    restTimerSeconds = ex.restSeconds
                    showRestTimer = true
                },
                completedSets = completedSets.filter { it.exerciseName == ex.name }
            )
        }

        // Recent Workout History Log
        if (workoutLogs.isNotEmpty()) {
            item {
                Column {
                    Text(
                        text = "COMPLETED WORKOUT SESSIONS (${workoutLogs.size})",
                        color = NeonCyan,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            items(workoutLogs.take(5)) { log ->
                CyberCard(borderColor = CyberBorder) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = log.routineName,
                                color = TextPrimary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${log.dateString}  •  ${log.durationMinutes} mins  •  ${log.exercisesCompleted} exercises",
                                color = TextMuted,
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace
                            )
                        }

                        Text(
                            text = "${log.totalVolumeKg.toInt()} kg",
                            color = NeonGreen,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
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
private fun ActiveWorkoutSessionBanner(
    routine: WorkoutRoutine,
    completedSetsCount: Int,
    totalVolumeKg: Float,
    onOpenRestTimer: (Int) -> Unit,
    onFinishWorkout: (Int) -> Unit,
    onCancelWorkout: () -> Unit
) {
    var sessionDurationMin by remember { mutableIntStateOf(45) }

    CyberCard(borderColor = NeonGreen) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .background(NeonGreen, RoundedCornerShape(5.dp))
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "LIVE GYM SESSION IN PROGRESS",
                        color = NeonGreen,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace
                    )
                }

                IconButton(onClick = onCancelWorkout) {
                    Icon(Icons.Default.Close, contentDescription = "Cancel", tint = TextMuted, modifier = Modifier.size(18.dp))
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("TOTAL SETS", color = TextMuted, fontSize = 9.sp, fontFamily = FontFamily.Monospace)
                    Text(
                        text = "$completedSetsCount Sets",
                        color = TextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("TOTAL VOLUME", color = TextMuted, fontSize = 9.sp, fontFamily = FontFamily.Monospace)
                    Text(
                        text = "${totalVolumeKg.toInt()} KG",
                        color = NeonGreen,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("REST CLOCK", color = TextMuted, fontSize = 9.sp, fontFamily = FontFamily.Monospace)
                    TextButton(
                        onClick = { onOpenRestTimer(90) },
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text("LAUNCH 90s", color = NeonCyan, fontSize = 11.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = { onFinishWorkout(sessionDurationMin) },
                colors = ButtonDefaults.buttonColors(containerColor = NeonGreen, contentColor = CyberBlack),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth().height(42.dp)
            ) {
                Text("FINISH WORKOUT & RECORD VOLUME", fontWeight = FontWeight.Black, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
            }
        }
    }
}

@Composable
private fun ExerciseBulkingCard(
    exercise: BulkingExercise,
    isSessionActive: Boolean,
    onLogSet: (weightKg: Float, reps: Int) -> Unit,
    completedSets: List<com.example.ui.viewmodel.ActiveSetLog>
) {
    var weightInput by remember { mutableStateOf("") }
    var repsInput by remember { mutableStateOf("") }
    var isFormExpanded by remember { mutableStateOf(false) }

    CyberCard(borderColor = CyberBorder) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = exercise.name,
                        color = TextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${exercise.targetMuscle}  •  ${exercise.sets} Sets x ${exercise.repRange}  •  ${exercise.restSeconds}s Rest",
                        color = NeonCyan,
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Bulking coaching tip
            Text(
                text = "💡 ${exercise.bulkingTip}",
                color = TextSecondary,
                fontSize = 11.sp,
                lineHeight = 15.sp
            )

            // Form Cues Toggle
            Text(
                text = if (isFormExpanded) "Hide Execution Form Cues" else "Show Execution Form Cues",
                color = NeonAmber,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .clickable { isFormExpanded = !isFormExpanded }
            )

            AnimatedVisibility(visible = isFormExpanded) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF0D1422), RoundedCornerShape(8.dp))
                        .padding(8.dp)
                ) {
                    Text(
                        text = exercise.formCues,
                        color = TextPrimary,
                        fontSize = 11.sp,
                        lineHeight = 15.sp
                    )
                }
            }

            // If session is active, allow set logging right here!
            if (isSessionActive) {
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = weightInput,
                        onValueChange = { weightInput = it },
                        label = { Text("Kg", fontSize = 10.sp) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeonGreen,
                            unfocusedBorderColor = CyberBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        modifier = Modifier.weight(1f).height(50.dp),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = repsInput,
                        onValueChange = { repsInput = it },
                        label = { Text("Reps", fontSize = 10.sp) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeonGreen,
                            unfocusedBorderColor = CyberBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        modifier = Modifier.weight(1f).height(50.dp),
                        singleLine = true
                    )

                    Button(
                        onClick = {
                            val w = weightInput.toFloatOrNull() ?: 0f
                            val r = repsInput.toIntOrNull() ?: 0
                            if (r > 0) {
                                onLogSet(w, r)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = NeonGreen, contentColor = CyberBlack),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.height(50.dp)
                    ) {
                        Text("+ SET", fontSize = 10.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                    }
                }

                // Completed sets badge list
                if (completedSets.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        completedSets.forEach { s ->
                            Box(
                                modifier = Modifier
                                    .background(NeonGreen.copy(alpha = 0.2f), RoundedCornerShape(6.dp))
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    text = "Set ${s.setNumber}: ${s.weightKg}kg × ${s.reps}",
                                    color = NeonGreen,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
