package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CyberBlack
import com.example.ui.theme.CyberBorder
import com.example.ui.theme.NeonAmber
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.NeonPink
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import kotlin.math.cos
import kotlin.math.sin

data class Point3D(val x: Float, val y: Float, val z: Float, val tag: String = "")

data class ProjectedPoint(val x: Float, val y: Float, val z: Float, val scale: Float, val tag: String)

@Composable
fun HolographicBody3D(
    selectedMuscle: String = "ALL",
    onMuscleSelected: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    // 3D rotation angles (degrees)
    var yawDeg by remember { mutableFloatStateOf(0f) }
    var pitchDeg by remember { mutableFloatStateOf(10f) }

    // Subtle idle animation
    val infiniteTransition = rememberInfiniteTransition(label = "3d_idle")
    val idleRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(28000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    val scanlineY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(3500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "scanline"
    )

    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    val currentYaw = yawDeg + idleRotation

    Box(
        modifier = modifier
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0xFF0F1A2E), CyberBlack),
                    radius = 500f
                ),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(12.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header Info
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
                        Spacer(modifier = Modifier.size(6.dp))
                        Text(
                            text = "3D KINETIC ANATOMY // HUD",
                            color = NeonGreen,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.5.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                    Text(
                        text = if (selectedMuscle == "ALL") "DRAG TO ROTATE 360°" else "TARGET: $selectedMuscle",
                        color = TextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                IconButton(
                    onClick = {
                        yawDeg = 0f
                        pitchDeg = 10f
                        onMuscleSelected("ALL")
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Reset 3D View",
                        tint = NeonCyan,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // 3D Canvas
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .pointerInput(Unit) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            yawDeg += dragAmount.x * 0.7f
                            pitchDeg = (pitchDeg - dragAmount.y * 0.5f).coerceIn(-40f, 40f)
                        }
                    }
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val cx = size.width / 2f
                    val cy = size.height / 2f - 10f
                    val fov = 650f

                    // Draw holographic perspective floor grid
                    drawCyberGrid(cx, cy + 130f, currentYaw)

                    // Draw 3D anatomical model nodes and skeletal wireframe
                    draw3DHumanoid(
                        cx = cx,
                        cy = cy,
                        fov = fov,
                        yawDeg = currentYaw,
                        pitchDeg = pitchDeg,
                        selectedMuscle = selectedMuscle,
                        pulseAlpha = pulseAlpha
                    )

                    // Draw holographic scanning lasers
                    val scanY = size.height * scanlineY
                    drawLine(
                        brush = Brush.horizontalGradient(
                            colors = listOf(Color.Transparent, NeonCyan.copy(alpha = 0.5f), Color.Transparent)
                        ),
                        start = Offset(0f, scanY),
                        end = Offset(size.width, scanY),
                        strokeWidth = 1.5f
                    )
                }
            }

            // Muscle Filter Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                listOf("ALL", "CHEST", "BACK", "ARMS", "LEGS").forEach { muscle ->
                    val isSelected = selectedMuscle == muscle
                    Box(
                        modifier = Modifier
                            .background(
                                color = if (isSelected) NeonGreen.copy(alpha = 0.2f) else Color(0xFF131B2B),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                            .pointerInput(muscle) {
                                detectDragGestures { _, _ -> }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = muscle,
                            color = if (isSelected) NeonGreen else TextMuted,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }
        }
    }
}

private fun DrawScope.drawCyberGrid(cx: Float, gridY: Float, yaw: Float) {
    val rad = Math.toRadians(yaw.toDouble()).toFloat()
    val ringColor = NeonCyan.copy(alpha = 0.15f)

    // Concentric holographic rings
    for (r in listOf(80f, 130f, 180f)) {
        drawOval(
            color = ringColor,
            topLeft = Offset(cx - r, gridY - (r * 0.35f)),
            size = androidx.compose.ui.geometry.Size(r * 2f, r * 0.7f),
            style = Stroke(width = 1f)
        )
    }

    // Dynamic rotating crosshair marks
    val markerX = cx + cos(rad) * 130f
    val markerY = gridY + sin(rad) * (130f * 0.35f)
    drawCircle(
        color = NeonGreen.copy(alpha = 0.6f),
        radius = 3f,
        center = Offset(markerX, markerY)
    )
}

private fun DrawScope.draw3DHumanoid(
    cx: Float,
    cy: Float,
    fov: Float,
    yawDeg: Float,
    pitchDeg: Float,
    selectedMuscle: String,
    pulseAlpha: Float
) {
    val yaw = Math.toRadians(yawDeg.toDouble()).toFloat()
    val pitch = Math.toRadians(pitchDeg.toDouble()).toFloat()

    // 3D anatomical keypoints (Body coordinate space: Y is down, Z is into screen)
    val rawNodes = listOf(
        // Head / Neck
        Point3D(0f, -125f, 0f, "HEAD"),
        Point3D(0f, -95f, 0f, "NECK"),

        // Torso / Chest / Back
        Point3D(0f, -75f, 8f, "CHEST"),
        Point3D(0f, -75f, -8f, "BACK"),
        Point3D(0f, -40f, 0f, "CORE"),
        Point3D(0f, -10f, 0f, "HIPS"),

        // Left Arm (Shoulder, Bicep, Elbow, Wrist)
        Point3D(-42f, -85f, 0f, "SHOULDERS"),
        Point3D(-65f, -50f, 6f, "ARMS"),
        Point3D(-78f, -15f, 12f, "ARMS"),
        Point3D(-86f, 15f, 18f, "ARMS"),

        // Right Arm
        Point3D(42f, -85f, 0f, "SHOULDERS"),
        Point3D(65f, -50f, 6f, "ARMS"),
        Point3D(78f, -15f, 12f, "ARMS"),
        Point3D(86f, 15f, 18f, "ARMS"),

        // Left Leg (Hip, Quad, Knee, Calf, Ankle)
        Point3D(-24f, -5f, 0f, "HIPS"),
        Point3D(-28f, 35f, 8f, "LEGS"),
        Point3D(-30f, 75f, 0f, "LEGS"),
        Point3D(-32f, 115f, -4f, "LEGS"),
        Point3D(-33f, 140f, 4f, "LEGS"),

        // Right Leg
        Point3D(24f, -5f, 0f, "HIPS"),
        Point3D(28f, 35f, 8f, "LEGS"),
        Point3D(30f, 75f, 0f, "LEGS"),
        Point3D(32f, 115f, -4f, "LEGS"),
        Point3D(33f, 140f, 4f, "LEGS")
    )

    // Project points into 2D screen coordinates with 3D rotation matrix & depth scaling
    val projected = rawNodes.map { p ->
        // Rotate around Y axis (Yaw)
        val x1 = p.x * cos(yaw) + p.z * sin(yaw)
        val z1 = -p.x * sin(yaw) + p.z * cos(yaw)

        // Rotate around X axis (Pitch)
        val y2 = p.y * cos(pitch) - z1 * sin(pitch)
        val z2 = p.y * sin(pitch) + z1 * cos(pitch)

        // Perspective projection: scale = fov / (fov + z + distance)
        val distance = 380f
        val scale = fov / (fov + z2 + distance)
        val screenX = cx + x1 * scale
        val screenY = cy + y2 * scale

        ProjectedPoint(screenX, screenY, z2, scale, p.tag)
    }

    // Connect skeleton wireframe joints
    val connections = listOf(
        Pair(0, 1), // Head to Neck
        Pair(1, 2), // Neck to Chest
        Pair(2, 4), // Chest to Core
        Pair(4, 5), // Core to Hips

        // Shoulders
        Pair(1, 6), // Neck to L Shoulder
        Pair(1, 10), // Neck to R Shoulder
        Pair(6, 7), // L Shoulder to L Bicep
        Pair(7, 8), // L Bicep to L Elbow
        Pair(8, 9), // L Elbow to L Wrist
        Pair(10, 11), // R Shoulder to R Bicep
        Pair(11, 12), // R Bicep to R Elbow
        Pair(12, 13), // R Elbow to R Wrist

        // Legs
        Pair(5, 14), // Hips to L Hip
        Pair(14, 15), // L Hip to L Quad
        Pair(15, 16), // L Quad to L Knee
        Pair(16, 17), // L Knee to L Calf
        Pair(17, 18), // L Calf to L Foot
        Pair(5, 19), // Hips to R Hip
        Pair(19, 20), // R Hip to R Quad
        Pair(20, 21), // R Quad to R Knee
        Pair(21, 22), // R Knee to R Calf
        Pair(22, 23)  // R Calf to R Foot
    )

    // Draw skeletal connection lines
    connections.forEach { (i1, i2) ->
        val p1 = projected[i1]
        val p2 = projected[i2]

        val isTargeted = (selectedMuscle != "ALL") &&
                (p1.tag == selectedMuscle || p2.tag == selectedMuscle)

        val alpha = if (isTargeted) 0.9f else 0.35f
        val color = if (isTargeted) NeonGreen else NeonCyan.copy(alpha = alpha)

        drawLine(
            color = color,
            start = Offset(p1.x, p1.y),
            end = Offset(p2.x, p2.y),
            strokeWidth = if (isTargeted) 2.5f else 1.2f
        )
    }

    // Sort nodes by Z (back to front) for accurate 3D layering
    val sortedNodes = projected.sortedBy { it.z }

    sortedNodes.forEach { p ->
        val isTargeted = (selectedMuscle == "ALL") || (p.tag == selectedMuscle)
        val nodeColor = when {
            isTargeted && p.tag == "CHEST" -> NeonGreen
            isTargeted && p.tag == "BACK" -> NeonCyan
            isTargeted && p.tag == "ARMS" -> NeonAmber
            isTargeted && p.tag == "LEGS" -> NeonPink
            isTargeted -> NeonGreen
            else -> CyberBorder
        }

        val baseRadius = when (p.tag) {
            "HEAD" -> 16f * p.scale
            "CHEST" -> 10f * p.scale
            "BACK" -> 9f * p.scale
            "CORE" -> 7f * p.scale
            "SHOULDERS" -> 7f * p.scale
            "ARMS" -> 6f * p.scale
            "LEGS" -> 7f * p.scale
            else -> 5f * p.scale
        }

        // Glowing outer halo for active muscles
        if (isTargeted) {
            drawCircle(
                color = nodeColor.copy(alpha = 0.3f * pulseAlpha),
                radius = baseRadius * 1.8f,
                center = Offset(p.x, p.y)
            )
        }

        // Core joint node
        drawCircle(
            color = nodeColor,
            radius = baseRadius,
            center = Offset(p.x, p.y)
        )

        // Center highlight
        drawCircle(
            color = Color.White.copy(alpha = 0.8f),
            radius = (baseRadius * 0.4f).coerceAtLeast(1.5f),
            center = Offset(p.x, p.y)
        )
    }
}
