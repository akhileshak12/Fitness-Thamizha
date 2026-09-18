package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Bulking Fitness", appName)
  }

  @Test
  fun `verify bulking nutrition formula calculates surplus`() {
    val (targetKcal, macros, tdee) = com.example.data.repository.FitnessRepository.calculateBulkingNutrition(
      weightKg = 70f,
      heightCm = 175f,
      age = 22,
      activityLevel = "MODERATE",
      bulkType = "CLEAN_SURPLUS"
    )

    // TDEE should be ~2500+ and target calories should include +380 kcal surplus
    org.junit.Assert.assertTrue(targetKcal > tdee)
    org.junit.Assert.assertEquals(380, targetKcal - tdee)
    org.junit.Assert.assertTrue(macros.first >= 140) // Protein target >= 140g for 70kg
  }
}
