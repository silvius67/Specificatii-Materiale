package ro.subcontrol.decizii.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import ro.subcontrol.decizii.model.DecisionNode
import ro.subcontrol.decizii.ui.theme.*
import ro.subcontrol.decizii.viewmodel.DecisionViewModel

// ── Ecranul principal ──────────────────────────────────────────────────────

@Composable
fun DecisionScreen(vm: DecisionViewModel = viewModel()) {
    val node      by vm.currentNode.collectAsState()
    val canGoBack by vm.canGoBack.collectAsState()
    val progress  by vm.progress.collectAsState()
    val breadcrumb by vm.breadcrumb.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Navy900, Navy800, Navy900)
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            AppHeader()

            // Bara de progres
            ProgressBar(progress)
            Text(
                text = "${(progress * 5).toInt()} pași parcurși",
                fontSize = 11.sp,
                color = Navy500,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End
            )

            // Breadcrumb
            BreadcrumbRow(breadcrumb)

            // Conținut animat
            AnimatedContent(
                targetState = node,
                transitionSpec = {
                    slideInHorizontally(
                        initialOffsetX = { it / 3 },
                        animationSpec = tween(300)
                    ) togetherWith slideOutHorizontally(
                        targetOffsetX = { -it / 3 },
                        animationSpec = tween(200)
                    )
                },
                label = "node_transition"
            ) { currentNode ->
                when {
                    currentNode == null -> CircularProgressIndicator(color = Blue500)
                    currentNode.isResult ->
                        ResultCard(currentNode, onRestart = { vm.restart() })
                    else ->
                        QuestionCard(currentNode, onOptionSelected = { vm.navigate(it) })
                }
            }

            // Buton Înapoi
            if (canGoBack && !node.isResult) {
                OutlinedButton(
                    onClick = { vm.goBack() },
                    modifier = Modifier.fillMaxWidth().height(46.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Navy300),
                    border = BorderStroke(1.dp, Navy700)
                ) {
                    Text("← Înapoi", fontSize = 13.sp)
                }
            }

            // Footer
            Spacer(Modifier.height(8.dp))
            FooterNote()
        }
    }
}

// ── Header aplicație ───────────────────────────────────────────────────────

@Composable
fun AppHeader() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(
                    Brush.linearGradient(listOf(Blue600, Blue500))
                ),
            contentAlignment = Alignment.Center
        ) {
            Text("🏗", fontSize = 22.sp)
        }
        Column {
            Text(
                text = "SUBCONTROL",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                letterSpacing = 0.5.sp
            )
            Text(
                text = "VERIFICARE PRODUSE DE CONSTRUCȚII",
                fontSize = 10.sp,
                color = Navy300,
                letterSpacing = 1.sp
            )
        }
    }
}

// ── Card întrebare / alegere cale ─────────────────────────────────────────

@Composable
fun QuestionCard(node: DecisionNode, onOptionSelected: (String) -> Unit) {
    val isChoice = node.isChoice

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Navy800),
        border = BorderStroke(2.dp, Navy700)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Badge pas
            StepBadge(node.step)

            // Iconița romb
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Navy900)
                    .border(2.dp, Blue500, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                // Rotim un pătrat 45° vizual prin emoji
                Text(
                    text = if (isChoice) "⑂" else "?",
                    fontSize = 24.sp,
                    color = Blue500,
                    fontWeight = FontWeight.Bold
                )
            }

            // Textul întrebării
            Text(
                text = node.text,
                fontSize = 17.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White,
                textAlign = TextAlign.Center,
                lineHeight = 26.sp
            )

            // Referință legală
            if (node.ref.isNotBlank()) {
                Text(
                    text = node.ref,
                    fontSize = 11.sp,
                    color = Navy500,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Normal,
                    lineHeight = 16.sp
                )
            }

            Spacer(Modifier.height(4.dp))

            // Butoane opțiuni
            node.options.forEachIndexed { i, option ->
                val customColor: Color? = when {
                    isChoice && i == 0 -> Blue600
                    isChoice && i == 1 -> Color(0xFF047857)
                    else -> null
                }
                OptionButton(
                    label = option.label,
                    isPrimary = i == 0,
                    customColor = customColor,
                    onClick = { onOptionSelected(option.next) }
                )
            }
        }
    }
}

// ── Card rezultat final ────────────────────────────────────────────────────

@Composable
fun ResultCard(node: DecisionNode, onRestart: () -> Unit) {
    val (bgColor, borderColor, iconBg, labelText) = when (node.severity) {
        "success" -> listOf(Green50, Green600, Green600, "CONFORM")
        "warning" -> listOf(Amber50, Amber600, Amber600, "NAȚIONAL")
        else             -> listOf(Red50, Red600, Red700, "ATENȚIE")
    }

    @Suppress("UNCHECKED_CAST")
    val bg     = bgColor as Color
    val border = borderColor as Color
    val icon   = iconBg as Color
    val label  = labelText as String

    val iconChar = when (node.severity) {
        "success" -> "✓"
        "warning" -> "⚠"
        else             -> "✕"
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = bg),
        border = BorderStroke(2.dp, border)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Titlu rezultat
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(icon),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = iconChar,
                        fontSize = 22.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
                Column {
                    Text(
                        text = "CONCLUZIE — $label",
                        fontSize = 10.sp,
                        letterSpacing = 1.5.sp,
                        color = icon,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = node.title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A),
                        lineHeight = 22.sp
                    )
                }
            }

            // Descriere
            Text(
                text = node.text,
                fontSize = 14.sp,
                color = Color(0xFF374151),
                lineHeight = 22.sp
            )

            // Temei legal
            LegalRefCard(
                ref = node.ref,
                modifier = Modifier
            )

            // Acțiune
            ActionCard(
                action = node.action,
                accentColor = icon
            )

            Spacer(Modifier.height(4.dp))

            // Butoane navigare rezultat
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedButton(
                    onClick = onRestart,
                    modifier = Modifier.weight(1f).height(46.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFF6B7280)
                    ),
                    border = BorderStroke(1.dp, Color(0xFFD1D5DB))
                ) {
                    Text("↺ Verificare nouă", fontSize = 13.sp)
                }
            }
        }
    }
}

// ── Footer ─────────────────────────────────────────────────────────────────

@Composable
fun FooterNote() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "HG 668/2017 · RPC (UE) 305/2011 / 2024/3110 · HG 766/1997 · Legea 10/1995",
            fontSize = 10.sp,
            color = Navy700,
            textAlign = TextAlign.Center,
            lineHeight = 16.sp
        )
        Spacer(Modifier.height(2.dp))
        Text(
            text = "RPC (UE) 2024/3110 se aplică integral din 8 ianuarie 2026",
            fontSize = 10.sp,
            color = Navy700,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Light
        )
    }
}
