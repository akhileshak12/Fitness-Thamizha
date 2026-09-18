package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.database.FitnessDatabase
import com.example.data.model.FoodLog
import com.example.data.model.RecoveryLog
import com.example.data.model.UserProfile
import com.example.data.model.WaterLog
import com.example.data.model.WeightLog
import com.example.data.model.WorkoutLog
import com.example.data.presets.BulkingMeal
import com.example.data.presets.BulkingPresets
import com.example.data.presets.QuickFoodItem
import com.example.data.presets.WorkoutRoutine
import com.example.data.repository.FitnessRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ActiveSetLog(
    val exerciseName: String,
    val setNumber: Int,
    val weightKg: Float,
    val reps: Int
)

class FitnessViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: FitnessRepository
    val todayDate: String = FitnessRepository.getTodayDateString()

    init {
        val database = FitnessDatabase.getDatabase(application)
        repository = FitnessRepository(database.fitnessDao())
        initDefaultDataIfNeeded()
    }

    // Default Profile
    val userProfile: StateFlow<UserProfile> = repository.userProfile
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UserProfile()
        ).let { flow ->
            // If null in DB, fallback to default
            MutableStateFlow(UserProfile()).apply {
                viewModelScope.launch {
                    flow.collect { prof ->
                        this@apply.value = prof ?: UserProfile()
                    }
                }
            }.asStateFlow()
        }

    val todayFoodLogs: StateFlow<List<FoodLog>> = repository.getFoodLogs(todayDate)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val todayWater: StateFlow<Int> = repository.getWaterLog(todayDate)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = WaterLog(todayDate, 0)
        ).let { flow ->
            MutableStateFlow(0).apply {
                viewModelScope.launch {
                    flow.collect { log ->
                        this@apply.value = log?.amountMl ?: 0
                    }
                }
            }.asStateFlow()
        }

    val weightLogs: StateFlow<List<WeightLog>> = repository.weightLogs
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val workoutLogs: StateFlow<List<WorkoutLog>> = repository.allWorkoutLogs
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val todayRecovery: StateFlow<RecoveryLog> = repository.getRecoveryLog(todayDate)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = RecoveryLog(todayDate)
        ).let { flow ->
            MutableStateFlow(RecoveryLog(todayDate)).apply {
                viewModelScope.launch {
                    flow.collect { log ->
                        this@apply.value = log ?: RecoveryLog(todayDate)
                    }
                }
            }.asStateFlow()
        }

    // Daily Macro Totals
    val totalCaloriesToday = MutableStateFlow(0)
    val totalProteinToday = MutableStateFlow(0)
    val totalCarbsToday = MutableStateFlow(0)
    val totalFatToday = MutableStateFlow(0)

    init {
        viewModelScope.launch {
            todayFoodLogs.collect { logs ->
                var kcal = 0
                var p = 0
                var c = 0
                var f = 0
                for (item in logs) {
                    kcal += item.calories
                    p += item.proteinG
                    c += item.carbsG
                    f += item.fatG
                }
                totalCaloriesToday.value = kcal
                totalProteinToday.value = p
                totalCarbsToday.value = c
                totalFatToday.value = f
            }
        }
    }

    // Active Gym Workout Session State
    private val _activeRoutine = MutableStateFlow<WorkoutRoutine?>(null)
    val activeRoutine: StateFlow<WorkoutRoutine?> = _activeRoutine.asStateFlow()

    private val _completedSets = MutableStateFlow<List<ActiveSetLog>>(emptyList())
    val completedSets: StateFlow<List<ActiveSetLog>> = _completedSets.asStateFlow()

    private val _workoutDurationMinutes = MutableStateFlow(0)
    val workoutDurationMinutes: StateFlow<Int> = _workoutDurationMinutes.asStateFlow()

    fun startWorkoutSession(routine: WorkoutRoutine) {
        _activeRoutine.value = routine
        _completedSets.value = emptyList()
        _workoutDurationMinutes.value = 0
    }

    fun logWorkoutSet(exerciseName: String, setNumber: Int, weightKg: Float, reps: Int) {
        val updated = _completedSets.value.toMutableList()
        updated.add(ActiveSetLog(exerciseName, setNumber, weightKg, reps))
        _completedSets.value = updated
    }

    fun finishWorkoutSession(durationMin: Int) {
        val routine = _activeRoutine.value ?: return
        val sets = _completedSets.value
        var totalVolume = 0f
        for (s in sets) {
            totalVolume += (s.weightKg * s.reps)
        }
        val distinctExercises = sets.map { it.exerciseName }.distinct().size

        viewModelScope.launch {
            repository.logWorkout(
                routineName = routine.title,
                durationMinutes = durationMin.coerceAtLeast(1),
                totalVolumeKg = totalVolume,
                exercisesCompleted = distinctExercises.coerceAtLeast(1)
            )
            _activeRoutine.value = null
            _completedSets.value = emptyList()
        }
    }

    fun cancelWorkoutSession() {
        _activeRoutine.value = null
        _completedSets.value = emptyList()
    }

    // Logging actions
    fun logFood(
        name: String,
        calories: Int,
        protein: Int,
        carbs: Int,
        fat: Int,
        mealType: String
    ) {
        viewModelScope.launch {
            repository.logFood(name, calories, protein, carbs, fat, mealType, todayDate)
        }
    }

    fun deleteFood(id: Long) {
        viewModelScope.launch {
            repository.deleteFoodLog(id)
        }
    }

    fun quickLogPresetMeal(meal: BulkingMeal) {
        val mealType = when (meal.category) {
            "SHAKES" -> "BULK_SHAKE"
            "BREAKFAST" -> "BREAKFAST"
            "LUNCH" -> "LUNCH"
            "DINNER" -> "DINNER"
            else -> "SNACK"
        }
        logFood(meal.title, meal.calories, meal.proteinG, meal.carbsG, meal.fatG, mealType)
    }

    fun quickLogFoodItem(item: QuickFoodItem) {
        logFood(item.name, item.calories, item.proteinG, item.carbsG, item.fatG, item.defaultMealType)
    }

    fun addWater(amountDeltaMl: Int) {
        viewModelScope.launch {
            repository.addWater(amountDeltaMl, todayDate, todayWater.value)
        }
    }

    fun logWeight(weightKg: Float, note: String = "") {
        viewModelScope.launch {
            repository.logWeight(weightKg, note)
            // Also update current weight in profile
            val currentProf = userProfile.value
            if (currentProf.currentWeightKg != weightKg) {
                val (newKcal, macros, _) = FitnessRepository.calculateBulkingNutrition(
                    weightKg = weightKg,
                    heightCm = currentProf.heightCm,
                    age = currentProf.age,
                    activityLevel = currentProf.activityLevel,
                    bulkType = currentProf.bulkType
                )
                repository.saveProfile(
                    currentProf.copy(
                        currentWeightKg = weightKg,
                        targetCalories = newKcal,
                        targetProteinG = macros.first,
                        targetCarbsG = macros.second,
                        targetFatG = macros.third
                    )
                )
            }
        }
    }

    fun deleteWeightLog(id: Long) {
        viewModelScope.launch {
            repository.deleteWeightLog(id)
        }
    }

    fun updateProfile(
        name: String,
        currentWeightKg: Float,
        targetWeightKg: Float,
        heightCm: Float,
        age: Int,
        activityLevel: String,
        bulkType: String,
        dietaryPreference: String
    ) {
        viewModelScope.launch {
            val (targetKcal, macros, _) = FitnessRepository.calculateBulkingNutrition(
                weightKg = currentWeightKg,
                heightCm = heightCm,
                age = age,
                activityLevel = activityLevel,
                bulkType = bulkType
            )

            val updated = userProfile.value.copy(
                name = name,
                currentWeightKg = currentWeightKg,
                targetWeightKg = targetWeightKg,
                heightCm = heightCm,
                age = age,
                activityLevel = activityLevel,
                bulkType = bulkType,
                dietaryPreference = dietaryPreference,
                targetCalories = targetKcal,
                targetProteinG = macros.first,
                targetCarbsG = macros.second,
                targetFatG = macros.third
            )
            repository.saveProfile(updated)
        }
    }

    fun updateRecovery(
        sleepHours: Float,
        sorenessLevel: Int,
        isRestDay: Boolean,
        notes: String = ""
    ) {
        viewModelScope.launch {
            repository.updateRecovery(sleepHours, sorenessLevel, isRestDay, notes, todayDate)
        }
    }

    fun toggleRestDayReminders(enabled: Boolean) {
        viewModelScope.launch {
            val updated = userProfile.value.copy(restDayRemindersEnabled = enabled)
            repository.saveProfile(updated)
        }
    }

    private fun initDefaultDataIfNeeded() {
        viewModelScope.launch {
            // Check if profile exists, if not seed initial bulking profile & initial weight
            repository.userProfile.collect { prof ->
                if (prof == null) {
                    val initialProf = UserProfile(
                        name = "Titan Builder",
                        currentWeightKg = 64.0f,
                        targetWeightKg = 75.0f,
                        heightCm = 175.0f,
                        age = 22,
                        activityLevel = "MODERATE",
                        bulkType = "CLEAN_SURPLUS",
                        dietaryPreference = "ALL",
                        targetCalories = 3250,
                        targetProteinG = 160,
                        targetCarbsG = 435,
                        targetFatG = 92
                    )
                    repository.saveProfile(initialProf)
                    repository.logWeight(64.0f, "Starting Bulking Baseline")

                    // Seed starter meals for today so user sees instant high-tech telemetry
                    repository.logFood(
                        name = "Titan Anabolic Monster Shake",
                        calories = 980,
                        proteinG = 55,
                        carbsG = 120,
                        fatG = 28,
                        mealType = "BULK_SHAKE",
                        date = todayDate
                    )
                    repository.logFood(
                        name = "Power Oatmeal & 4 Whole Eggs",
                        calories = 780,
                        proteinG = 46,
                        carbsG = 65,
                        fatG = 34,
                        mealType = "BREAKFAST",
                        date = todayDate
                    )
                    repository.addWater(1500, todayDate, 0)
                }
            }
        }
    }
}
