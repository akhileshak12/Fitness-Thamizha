package com.example.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.FoodLog
import com.example.data.model.RecoveryLog
import com.example.data.model.UserProfile
import com.example.data.model.WaterLog
import com.example.data.model.WeightLog
import com.example.data.model.WorkoutLog
import kotlinx.coroutines.flow.Flow

@Dao
interface FitnessDao {

    // User Profile
    @Query("SELECT * FROM user_profile WHERE id = 1 LIMIT 1")
    fun getUserProfile(): Flow<UserProfile?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProfile(profile: UserProfile)

    // Weight Logs
    @Query("SELECT * FROM weight_logs ORDER BY timestamp ASC")
    fun getWeightLogs(): Flow<List<WeightLog>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeightLog(log: WeightLog)

    @Query("DELETE FROM weight_logs WHERE id = :id")
    suspend fun deleteWeightLog(id: Long)

    // Food Logs
    @Query("SELECT * FROM food_logs WHERE dateString = :date ORDER BY timestamp ASC")
    fun getFoodLogsForDate(date: String): Flow<List<FoodLog>>

    @Query("SELECT * FROM food_logs ORDER BY timestamp DESC")
    fun getAllFoodLogs(): Flow<List<FoodLog>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFoodLog(log: FoodLog)

    @Query("DELETE FROM food_logs WHERE id = :id")
    suspend fun deleteFoodLog(id: Long)

    // Water Logs
    @Query("SELECT * FROM water_logs WHERE dateString = :date LIMIT 1")
    fun getWaterLogForDate(date: String): Flow<WaterLog?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateWaterLog(log: WaterLog)

    // Workout Logs
    @Query("SELECT * FROM workout_logs ORDER BY timestamp DESC")
    fun getAllWorkoutLogs(): Flow<List<WorkoutLog>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkoutLog(log: WorkoutLog)

    // Recovery Logs
    @Query("SELECT * FROM recovery_logs WHERE dateString = :date LIMIT 1")
    fun getRecoveryLogForDate(date: String): Flow<RecoveryLog?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateRecoveryLog(log: RecoveryLog)
}
