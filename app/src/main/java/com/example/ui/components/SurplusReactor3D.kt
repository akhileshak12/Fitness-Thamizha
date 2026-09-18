package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CyberBorder
import com.example.ui.theme.CyberCardBg
import com.example.ui.theme.NeonAmber
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.NeonPink
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun SurplusReactor3D(
    consumedCalories: Int,
    targetCalories: Int,
    proteinG: Int,
    targetProteinG: Int,
    carbsG: Int,
    targetCarbsG: Int,
    fatG: Int,
    targetFatG: Int,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "reactor_spin")
    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "orbit"
    )

    val pulseGlow by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )

    val progress = if (targetCalories > 0) {
        (consumedCalories.toFloat() / targetCalories.toFloat()).coerceIn(0f, 1.25f)
    } else 0f

    val remainingKcal = targetCalories - consumedCalories

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(Color(0xFF111827), Color(0xFF090E18))
                ),
                shape = RoundedCornerShape(24.dp)
            )
            .graphicsLayer {
                rotationX = 4f
                cameraDistance = 14f * density
            }
            .padding(18.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Header
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
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "ANABOLIC SURPLUS REACTOR",
                        color = NeonGreen,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.2.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }

                Box(
                    modifier = Modifier
                        .background(NeonAmber.copy(alpha = 0.15f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = "BULK MODE",
                        color = NeonAmber,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 3D Circular Gauge
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(200.dp)
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val cx = size.width / 2f
                    val cy = size.height / 2f
                    val radius = size.width * 0.40f

                    // Background Track Arc (240 degrees)
                    drawArc(
                        color = CyberBorder.copy(alpha = 0.4f),
                        startAngle = 150f,
                        sweepAngle = 240f,
                        useCenter = false,
                        topLeft = Offset(cx - radius, cy - radius),
                        size = Size(radius * 2f, radius * 2f),
                        style = Stroke(width = 14f, cap = StrokeCap.Round)
                    )

                    // Active Progress Arc
                    val sweep = (240f * progress.coerceAtMost(1f))
                    drawArc(
                        brush = Brush.sweepGradient(
                            colors = listOf(NeonCyan, NeonGreen, NeonAmber)
                        ),
                        startAngle = 150f,
                        sweepAngle = sweep,
                        useCenter = false,
                        topLeft = Offset(cx - radius, cy - radius),
                        size = Size(radius * 2f, radius * 2f),
                        style = Stroke(width = 14f, cap = StrokeCap.Round)
                    )

                    // Glowing Orbiting Scanner Ring
                    drawOrbitRing(cx, cy, radius * 1.22f, rotationAngle, pulseGlow)
                }

                // Center Calorie Readout
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "$consumedCalories",
                        color = TextPrimary,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = "/ $targetCalories KCAL",
                        color = NeonCyan,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (remainingKcal > 0) "$remainingKcal kcal to target" else "🔥 SURPLUS HIT!",
                        color = if (remainingKcal > 0) TextMuted else NeonGreen,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Macro Split Gauges (Protein, Carbs, Fats)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                MacroPill(
                    label = "PROTEIN",
                    current = proteinG,
                    target = targetProteinG,
                    color = NeonGreen,
                    unit = "g"
                )
                MacroPill(
                    label = "CARBS",
                    current = carbsG,
                    target = targetCarbsG,
                    color = NeonCyan,
                    unit = "g"
                )
                MacroPill(
                    label = "FATS",
                    current = fatG,
                    target = targetFatG,
                    color = NeonAmber,
                    unit = "g"
                )
            }
        }
    }
}

private fun DrawScope.drawOrbitRing(
    cx: Float,
    cy: Float,
    r: Float,
    angleDeg: Float,
    glow: Float
) {
    // Outer dashed ring
    drawCircle(
        color = NeonCyan.copy(alpha = 0.15f),
        radius = r,
        center = Offset(cx, cy),
        style = Stroke(width = 1f)
    )

    // Orbiting particle
    val rad = Math.toRadians(angleDeg.toDouble()).toFloat()
    val px = cx + cos(rad) * r
    val py = cy + sin(rad) * r

    drawCircle(
        color = NeonGreen.copy(alpha = 0.4f * glow),
        radius = 8f,
        center = Offset(px, py)
    )
    drawCircle(
        color = NeonGreen,
        radius = 3.5f,
        center = Offset(px, py)
    )
}

@Composable
private fun MacroPill(
    label: String,
    current: Int,
    target: Int,
    color: Color,
    unit: String
) {
    val macroRatio = if (target > 0) (current.toFloat() / target.toFloat()).coerceIn(0f, 1f) else 0f

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(CyberCardBg, RoundedCornerShape(12.dp))
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Text(
            text = label,
            color = color,
            fontSize = 10.sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 1.sp,
            fontFamily = FontFamily.Monospace
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = "$current / $target$unit",
            color = TextPrimary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace
        )
        Spacer(modifier = Modifier.height(4.dp))
        // Progress bar
        Box(
            modifier = Modifier
                .width(60.dp)
                .height(4.dp)
                .background(Color(0xFF1E293B), RoundedCornerShape(2.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(macroRatio)
                    .height(4.dp)
                    .background(color, RoundedCornerShape(2.dp))
            )
        }
    }
}
