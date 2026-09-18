package com.example.data.repository

import com.example.data.dao.FitnessDao
import com.example.data.model.FoodLog
import com.example.data.model.RecoveryLog
import com.example.data.model.UserProfile
import com.example.data.model.WaterLog
import com.example.data.model.WeightLog
import com.example.data.model.WorkoutLog
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FitnessRepository(private val dao: FitnessDao) {

    val userProfile: Flow<UserProfile?> = dao.getUserProfile()
    val weightLogs: Flow<List<WeightLog>> = dao.getWeightLogs()
    val allWorkoutLogs: Flow<List<WorkoutLog>> = dao.getAllWorkoutLogs()

    fun getFoodLogs(date: String): Flow<List<FoodLog>> = dao.getFoodLogsForDate(date)
    fun getWaterLog(date: String): Flow<WaterLog?> = dao.getWaterLogForDate(date)
    fun getRecoveryLog(date: String): Flow<RecoveryLog?> = dao.getRecoveryLogForDate(date)

    suspend fun saveProfile(profile: UserProfile) {
        dao.insertOrUpdateProfile(profile)
    }

    suspend fun logWeight(weightKg: Float, note: String = "") {
        val today = getTodayDateString()
        dao.insertWeightLog(
            WeightLog(
                dateString = today,
                weightKg = weightKg,
                note = note
            )
        )
    }

    suspend fun deleteWeightLog(id: Long) {
        dao.deleteWeightLog(id)
    }

    suspend fun logFood(
        name: String,
        calories: Int,
        proteinG: Int,
        carbsG: Int,
        fatG: Int,
        mealType: String,
        date: String = getTodayDateString()
    ) {
        dao.insertFoodLog(
            FoodLog(
                dateString = date,
                name = name,
                calories = calories,
                proteinG = proteinG,
                carbsG = carbsG,
                fatG = fatG,
                mealType = mealType
            )
        )
    }

    suspend fun deleteFoodLog(id: Long) {
        dao.deleteFoodLog(id)
    }

    suspend fun addWater(amountDeltaMl: Int, date: String = getTodayDateString(), currentAmount: Int = 0) {
        val newAmount = (currentAmount + amountDeltaMl).coerceAtLeast(0)
        dao.insertOrUpdateWaterLog(WaterLog(dateString = date, amountMl = newAmount))
    }

    suspend fun logWorkout(
        routineName: String,
        durationMinutes: Int,
        totalVolumeKg: Float,
        exercisesCompleted: Int
    ) {
        dao.insertWorkoutLog(
            WorkoutLog(
                dateString = getTodayDateString(),
                routineName = routineName,
                durationMinutes = durationMinutes,
                totalVolumeKg = totalVolumeKg,
                exercisesCompleted = exercisesCompleted
            )
        )
    }

    suspend fun updateRecovery(
        sleepHours: Float,
        sorenessLevel: Int,
        isRestDay: Boolean,
        notes: String = "",
        date: String = getTodayDateString()
    ) {
        dao.insertOrUpdateRecoveryLog(
            RecoveryLog(
                dateString = date,
                sleepHours = sleepHours,
                sorenessLevel = sorenessLevel,
                isRestDay = isRestDay,
                notes = notes
            )
        )
    }

    companion object {
        fun getTodayDateString(): String {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            return sdf.format(Date())
        }

        fun calculateBulkingNutrition(
            weightKg: Float,
            heightCm: Float,
            age: Int,
            activityLevel: String,
            bulkType: String
        ): Triple<Int, Triple<Int, Int, Int>, Int> {
            // Mifflin-St Jeor Formula
            val bmr = 10f * weightKg + 6.25f * heightCm - 5f * age + 5f

            val activityMultiplier = when (activityLevel) {
                "LIGHT" -> 1.375f
                "MODERATE" -> 1.55f
                "HIGH" -> 1.725f
                "VERY_ACTIVE" -> 1.9f
                else -> 1.55f
            }

            val tdee = bmr * activityMultiplier

            val surplus = when (bulkType) {
                "HEAVY_MASS" -> 650
                else -> 380 // CLEAN_SURPLUS
            }

            val targetCalories = (tdee + surplus).toInt()
            val targetProtein = (weightKg * 2.2f).toInt().coerceAtLeast(130)
            val fatCalories = (targetCalories * 0.25f).toInt()
            val targetFat = (fatCalories / 9).coerceAtLeast(60)
            val carbCalories = targetCalories - (targetProtein * 4) - fatCalories
            val targetCarbs = (carbCalories / 4).coerceAtLeast(250)

            return Triple(targetCalories, Triple(targetProtein, targetCarbs, targetFat), tdee.toInt())
        }
    }
}
