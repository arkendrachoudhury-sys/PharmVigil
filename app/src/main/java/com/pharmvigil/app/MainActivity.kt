package com.pharmvigil.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.FactCheck
import androidx.compose.material.icons.filled.FormatListBulleted
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pharmvigil.app.data.ctcae.CtcaeTerm
import com.pharmvigil.app.ui.components.AddCaseBottomSheet
import com.pharmvigil.app.ui.screens.AnalyticsScreen
import com.pharmvigil.app.ui.screens.CasesScreen
import com.pharmvigil.app.ui.screens.CtcaeCatalogScreen
import com.pharmvigil.app.ui.screens.SaeWizardScreen
import com.pharmvigil.app.ui.theme.PharmVigilTheme
import com.pharmvigil.app.viewmodel.PharmVigilViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PharmVigilTheme {
                PharmVigilApp()
            }
        }
    }
}

enum class NavigationTab(val title: String, val icon: ImageVector) {
    CASES("AE Log", Icons.Default.FormatListBulleted),
    CTCAE("CTCAE Library", Icons.Default.MenuBook),
    SAE_WIZARD("SAE Wizard", Icons.Default.FactCheck),
    ANALYTICS("Analytics", Icons.Default.BarChart)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PharmVigilApp(viewModel: PharmVigilViewModel = viewModel()) {
    var selectedTab by remember { mutableStateOf(NavigationTab.CASES) }
    var showAddCaseSheet by remember { mutableStateOf(false) }
    var preselectedCtcaeTerm by remember { mutableStateOf<CtcaeTerm?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.primary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocalHospital,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Column {
                            Text(
                                text = "PharmVigil",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Text(
                                text = "Clinical PV & CTCAE Grader",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                            )
                        }
                    }
                },
                actions = {
                    // Offline badge
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFF4CAF50).copy(alpha = 0.15f))
                            .border(1.dp, Color(0xFF4CAF50).copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF4CAF50))
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Offline Mode",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                            color = Color(0xFF81C784)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(
                        onClick = {
                            preselectedCtcaeTerm = null
                            showAddCaseSheet = true
                        },
                        modifier = Modifier.testTag("top_bar_add_case_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "New Case",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant
            ) {
                NavigationTab.values().forEach { tab ->
                    NavigationBarItem(
                        selected = selectedTab == tab,
                        onClick = { selectedTab = tab },
                        icon = { Icon(tab.icon, contentDescription = tab.title) },
                        label = { Text(tab.title, style = MaterialTheme.typography.labelSmall) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                            indicatorColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary
                        )
                    )
                }
            }
        },
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                NavigationTab.CASES -> {
                    CasesScreen(
                        viewModel = viewModel,
                        onAddNewCase = {
                            preselectedCtcaeTerm = null
                            showAddCaseSheet = true
                        }
                    )
                }
                NavigationTab.CTCAE -> {
                    CtcaeCatalogScreen(
                        viewModel = viewModel,
                        onLogTerm = { ctcaeTerm ->
                            preselectedCtcaeTerm = ctcaeTerm
                            showAddCaseSheet = true
                        }
                    )
                }
                NavigationTab.SAE_WIZARD -> {
                    SaeWizardScreen(
                        onStartCaseWithCriteria = {
                            preselectedCtcaeTerm = null
                            showAddCaseSheet = true
                        }
                    )
                }
                NavigationTab.ANALYTICS -> {
                    AnalyticsScreen(viewModel = viewModel)
                }
            }
        }
    }

    // Add Case Bottom Sheet Modal
    if (showAddCaseSheet) {
        AddCaseBottomSheet(
            preselectedTerm = preselectedCtcaeTerm,
            onDismiss = {
                showAddCaseSheet = false
                preselectedCtcaeTerm = null
            },
            onSubmit = { newCase ->
                viewModel.addCase(newCase)
                showAddCaseSheet = false
                preselectedCtcaeTerm = null
                selectedTab = NavigationTab.CASES
            }
        )
    }
}
