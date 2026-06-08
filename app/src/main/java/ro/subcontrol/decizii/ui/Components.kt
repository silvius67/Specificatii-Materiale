package ro.subcontrol.decizii.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ro.subcontrol.decizii.ui.theme.*

// ── Bara de progres animată ────────────────────────────────────────────────

@Composable
fun ProgressBar(progress: Float, modifier: Modifier = Modifier) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(400),
        label = "progress"
    )
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(4.dp)
            .clip(RoundedCornerShape(2.dp))
            .background(Navy700)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(animatedProgress)
                .fillMaxHeight()
                .clip(RoundedCornerShape(2.dp))
                .background(Blue500)
        )
    }
}

// ── Breadcrumb ─────────────────────────────────────────────────────────────

@Composable
fun BreadcrumbRow(steps: List<String>, modifier: Modifier = Modifier) {
    if (steps.isEmpty()) return
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        steps.forEachIndexed { i, label ->
            val isLast = i == steps.lastIndex
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(99.dp))
                    .background(if (isLast) Blue500 else Navy800)
                    .border(1.dp, if (isLast) Blue500 else Navy700, RoundedCornerShape(99.dp))
                    .padding(horizontal = 10.dp, vertical = 3.dp)
            ) {
                Text(
                    text = label,
                    fontSize = 10.sp,
                    color = if (isLast) Color.White else Navy500,
                    fontWeight = if (isLast) FontWeight.SemiBold else FontWeight.Normal
                )
            }
        }
    }
}

// ── Badge etichetă pas ─────────────────────────────────────────────────────

@Composable
fun StepBadge(label: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Navy900)
            .border(1.dp, Navy700, RoundedCornerShape(8.dp))
            .padding(horizontal = 12.dp, vertical = 5.dp)
    ) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(Blue500)
        )
        Text(
            text = label.uppercase(),
            fontSize = 10.sp,
            letterSpacing = 1.sp,
            color = Navy300,
            fontWeight = FontWeight.Medium
        )
    }
}

// ── Buton opțiune ─────────────────────────────────────────────────────────

@Composable
fun OptionButton(
    label: String,
    isPrimary: Boolean = true,
    customColor: Color? = null,
    onClick: () -> Unit
) {
    val bgColor = customColor ?: if (isPrimary) Blue600 else Navy900
    val borderColor = customColor ?: if (isPrimary) Blue500 else Navy700

    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = bgColor,
            contentColor = Color.White
        ),
        border = androidx.compose.foundation.BorderStroke(2.dp, borderColor),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
    ) {
        Text(
            text = label,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

// ── Card referință legală ─────────────────────────────────────────────────

@Composable
fun LegalRefCard(ref: String, modifier: Modifier = Modifier) {
    if (ref.isBlank()) return
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0x18FFFFFF))
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Text(
            text = "TEMEI LEGAL",
            fontSize = 10.sp,
            letterSpacing = 1.sp,
            color = Navy500,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(3.dp))
        Text(
            text = ref,
            fontSize = 12.sp,
            color = Navy300,
            fontStyle = FontStyle.Italic,
            lineHeight = 17.sp
        )
    }
}

// ── Card acțiune recomandată ──────────────────────────────────────────────

@Composable
fun ActionCard(action: String, accentColor: Color, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(accentColor.copy(alpha = 0.08f))
            .border(
                width = 1.dp,
                color = accentColor.copy(alpha = 0.25f),
                shape = RoundedCornerShape(8.dp)
            )
            // Linie stângă accentuată
            .then(
                Modifier.border(
                    androidx.compose.foundation.BorderStroke(
                        width = 4.dp,
                        color = accentColor
                    )
                )
            )
            .padding(horizontal = 14.dp, vertical = 12.dp)
    ) {
        Text(
            text = "▶  ACȚIUNE URMĂTOARE",
            fontSize = 10.sp,
            letterSpacing = 1.sp,
            color = accentColor,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = action,
            fontSize = 13.sp,
            color = Color(0xFF1F2937),
            lineHeight = 20.sp
        )
    }
}
