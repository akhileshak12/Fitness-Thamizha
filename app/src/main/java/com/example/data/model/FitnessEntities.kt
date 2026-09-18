package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey val id: Int = 1,
    val name: String = "Cyber Titan",
    val currentWeightKg: Float = 63.5f,
    val targetWeightKg: Float = 75.0f,
    val heightCm: Float = 175.0f,
    val age: Int = 22,
    val activityLevel: String = "MODERATE", // LIGHT, MODERATE, HIGH, VERY_ACTIVE
    val bulkType: String = "CLEAN_SURPLUS", // CLEAN_SURPLUS (+350 kcal), HEAVY_MASS (+650 kcal)
    val dietaryPreference: String = "ALL", // ALL, VEGETARIAN, VEGAN, HIGH_PROTEIN
    val targetCalories: Int = 3200,
    val targetProteinG: Int = 160,
    val targetCarbsG: Int = 430,
    val targetFatG: Int = 90,
    val waterTargetMl: Int = 3500,
    val restDayRemindersEnabled: Boolean = true
)

@Entity(tableName = "weight_logs")
data class WeightLog(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val dateString: String,
    val timestamp: Long = System.currentTimeMillis(),
    val weightKg: Float,
    val note: String = ""
)

@Entity(tableName = "food_logs")
data class FoodLog(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val dateString: String,
    val timestamp: Long = System.currentTimeMillis(),
    val name: String,
    val calories: Int,
    val proteinG: Int,
    val carbsG: Int,
    val fatG: Int,
    val mealType: String // "BREAKFAST", "LUNCH", "DINNER", "SNACK", "BULK_SHAKE"
)

@Entity(tableName = "water_logs")
data class WaterLog(
    @PrimaryKey val dateString: String,
    val amountMl: Int = 0
)

@Entity(tableName = "workout_logs")
data class WorkoutLog(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val dateString: String,
    val timestamp: Long = System.currentTimeMillis(),
    val routineName: String,
    val durationMinutes: Int,
    val totalVolumeKg: Float,
    val exercisesCompleted: Int
)

@Entity(tableName = "recovery_logs")
data class RecoveryLog(
    @PrimaryKey val dateString: String,
    val sleepHours: Float = 8.0f,
    val sorenessLevel: Int = 2, // 1: Fresh, 2: Minor, 3: Moderate, 4: Heavy, 5: Max DOMS
    val isRestDay: Boolean = false,
    val notes: String = ""
)
