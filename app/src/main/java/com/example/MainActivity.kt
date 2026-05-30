package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.ThemeStyle
import androidx.lifecycle.viewmodel.compose.viewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val mainViewModel: MainViewModel = viewModel()
            val isDark by mainViewModel.isDarkTheme.collectAsState()
            val style by mainViewModel.themeStyle.collectAsState()
            
            MyApplicationTheme(
                darkTheme = isDark,
                themeStyle = style
            ) {
                MainAppScreen(viewModel = mainViewModel)
            }
        }
    }
}

@Composable
fun MainAppScreen(viewModel: MainViewModel = viewModel()) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val currentLang by viewModel.currentLanguage.collectAsState()
    val completedLessons by viewModel.completedLessons.collectAsState()
    val masteredFlashcards by viewModel.masteredFlashcards.collectAsState()
    val quizScores by viewModel.quizScores.collectAsState()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .testTag("app_main_scaffold"),
        topBar = {
            HeaderBar(
                currentLang = currentLang,
                onLanguageChange = { viewModel.setLanguage(it) }
            )
        },
        bottomBar = {
            BottomNavBar(
                currentScreen = currentScreen,
                onNavigate = { viewModel.navigateTo(it) },
                lang = currentLang
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            when (currentScreen) {
                Screen.DASHBOARD -> DashboardScreen(
                    viewModel = viewModel,
                    lang = currentLang,
                    completedLessons = completedLessons,
                    masteredFlashcards = masteredFlashcards,
                    quizScores = quizScores
                )
                Screen.LESSONS -> LessonsListScreen(
                    viewModel = viewModel,
                    lang = currentLang,
                    completedLessons = completedLessons
                )
                Screen.LESSON_DETAIL -> LessonDetailScreen(
                    viewModel = viewModel,
                    lang = currentLang,
                    completedLessons = completedLessons
                )
                Screen.QUIZ -> QuizScreen(
                    viewModel = viewModel,
                    lang = currentLang
                )
                Screen.FLASHCARDS -> FlashcardsScreen(
                    viewModel = viewModel,
                    lang = currentLang,
                    masteredCards = masteredFlashcards
                )
                Screen.ABOUT -> AboutScreen(
                    viewModel = viewModel,
                    lang = currentLang,
                    completedLessons = completedLessons,
                    quizScores = quizScores,
                    masteredFlashcards = masteredFlashcards
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeaderBar(
    currentLang: AppLanguage,
    onLanguageChange: (AppLanguage) -> Unit
) {
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .border(
                            width = 1.5.dp,
                            color = MaterialTheme.colorScheme.primary,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Balance,
                        contentDescription = "Education Logo",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Column {
                    Text(
                        text = "EAC Seconde",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Citoyenneté & Droits",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        },
        actions = {
            Row(
                modifier = Modifier.padding(end = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                AppLanguage.entries.forEach { lang ->
                    val isSelected = lang == currentLang
                    Box(
                        modifier = Modifier
                            .testTag("lang_button_${lang.code}")
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                if (isSelected) MaterialTheme.colorScheme.primaryContainer
                                else Color.Transparent
                            )
                            .border(
                                width = 1.dp,
                                color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable { onLanguageChange(lang) }
                            .padding(horizontal = 8.dp, vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(text = lang.flag, fontSize = 16.sp)
                            Text(
                                text = lang.code.uppercase(),
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
        )
    )
}

@Composable
fun BottomNavBar(
    currentScreen: Screen,
    onNavigate: (Screen) -> Unit,
    lang: AppLanguage
) {
    NavigationBar(
        modifier = Modifier.testTag("navigation_bar"),
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            modifier = Modifier.testTag("nav_btn_dashboard"),
            selected = currentScreen == Screen.DASHBOARD,
            onClick = { onNavigate(Screen.DASHBOARD) },
            icon = { Icon(Icons.Default.Home, contentDescription = "Dashboard") },
            label = { Text(UISerial.getLabel("dashboard_title", lang), fontSize = 10.sp, maxLines = 1) },
            alwaysShowLabel = true
        )
        NavigationBarItem(
            modifier = Modifier.testTag("nav_btn_lessons"),
            selected = currentScreen == Screen.LESSONS || currentScreen == Screen.LESSON_DETAIL,
            onClick = { onNavigate(Screen.LESSONS) },
            icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = "Lessons") },
            label = { Text(UISerial.getLabel("lessons", lang), fontSize = 10.sp, maxLines = 1) },
            alwaysShowLabel = true
        )
        NavigationBarItem(
            modifier = Modifier.testTag("nav_btn_quiz"),
            selected = currentScreen == Screen.QUIZ,
            onClick = { onNavigate(Screen.QUIZ) },
            icon = { Icon(Icons.Default.Star, contentDescription = "Quizzes") },
            label = { Text(UISerial.getLabel("quizzes", lang), fontSize = 10.sp, maxLines = 1) },
            alwaysShowLabel = true
        )
        NavigationBarItem(
            modifier = Modifier.testTag("nav_btn_flashcards"),
            selected = currentScreen == Screen.FLASHCARDS,
            onClick = { onNavigate(Screen.FLASHCARDS) },
            icon = { Icon(Icons.Default.Favorite, contentDescription = "Flashcards") },
            label = { Text(UISerial.getLabel("flashcards", lang), fontSize = 10.sp, maxLines = 1) },
            alwaysShowLabel = true
        )
        NavigationBarItem(
            modifier = Modifier.testTag("nav_btn_about"),
            selected = currentScreen == Screen.ABOUT,
            onClick = { onNavigate(Screen.ABOUT) },
            icon = { Icon(Icons.Default.Info, contentDescription = "About") },
            label = { Text(UISerial.getLabel("about", lang), fontSize = 10.sp, maxLines = 1) },
            alwaysShowLabel = true
        )
    }
}

@Composable
fun DashboardScreen(
    viewModel: MainViewModel,
    lang: AppLanguage,
    completedLessons: Set<Int>,
    masteredFlashcards: Set<Int>,
    quizScores: Map<Int, Int>
) {
    val lessons by viewModel.lessons.collectAsState()
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Welcoming card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("welcome_card"),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = UISerial.getLabel("welcome_back", lang),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = UISerial.getLabel("quote", lang),
                    style = MaterialTheme.typography.bodyMedium,
                    fontStyle = FontStyle.Italic,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                )
            }
        }

        // Theme Customizer Card
        Card(
            modifier = Modifier.fillMaxWidth().testTag("theme_customizer_card"),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp)
            ),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = when (lang) {
                                AppLanguage.FRENCH -> "Personnalisation du Thème"
                                AppLanguage.ENGLISH -> "Theme Personalization"
                                AppLanguage.MALAGASY -> "Sary sy Lokofahitra"
                            },
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = when (lang) {
                                AppLanguage.FRENCH -> "Choisis ton style présidentiel et ton mode"
                                AppLanguage.ENGLISH -> "Choose your presidential style & theme mode"
                                AppLanguage.MALAGASY -> "Safidio ny loko ofisialy tianao"
                            },
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    
                    val isDark by viewModel.isDarkTheme.collectAsState()
                    Button(
                        onClick = { viewModel.setDarkTheme(!isDark) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                        ),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = if (isDark) {
                                when (lang) {
                                    AppLanguage.FRENCH -> "Sombre 🌙"
                                    AppLanguage.ENGLISH -> "Dark 🌙"
                                    AppLanguage.MALAGASY -> "Maizina 🌙"
                                }
                            } else {
                                when (lang) {
                                    AppLanguage.FRENCH -> "Clair ☀️"
                                    AppLanguage.ENGLISH -> "Light ☀️"
                                    AppLanguage.MALAGASY -> "Miharihary ☀️"
                                }
                            },
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                
                val activeStyle by viewModel.themeStyle.collectAsState()
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ThemeStyle.entries.forEach { style ->
                        val (styleLabel, styleColor) = when (style) {
                            ThemeStyle.GREEN -> Pair(
                                when (lang) {
                                    AppLanguage.FRENCH -> "Vert (Orig)"
                                    AppLanguage.ENGLISH -> "Green (Orig)"
                                    AppLanguage.MALAGASY -> "Maintso"
                                }, Color(0xFF006B54)
                            )
                            ThemeStyle.ORANGE -> Pair(
                                when (lang) {
                                    AppLanguage.FRENCH -> "Orange"
                                    AppLanguage.ENGLISH -> "Orange"
                                    AppLanguage.MALAGASY -> "Oranjy"
                                }, Color(0xFF944A00)
                            )
                            ThemeStyle.BLUE -> Pair(
                                when (lang) {
                                    AppLanguage.FRENCH -> "Bleu"
                                    AppLanguage.ENGLISH -> "Blue"
                                    AppLanguage.MALAGASY -> "Manga"
                                }, Color(0xFF005FAF)
                            )
                            ThemeStyle.VIOLET -> Pair(
                                when (lang) {
                                    AppLanguage.FRENCH -> "Violet (Orig)"
                                    AppLanguage.ENGLISH -> "Violet (Orig)"
                                    AppLanguage.MALAGASY -> "Volomparasy"
                                }, Color(0xFF7949B8)
                            )
                        }
                        
                        val isSelected = style == activeStyle
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { viewModel.setThemeStyle(style) }
                                .padding(horizontal = 8.dp, vertical = 6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(styleColor)
                                    .border(
                                        width = if (isSelected) 3.dp else 1.dp,
                                        color = if (isSelected) MaterialTheme.colorScheme.onSurface else Color.Transparent,
                                        shape = CircleShape
                                    )
                            )
                            Text(
                                text = styleLabel,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }

        // Statistics Progress Panel
        Text(
            text = when (lang) {
                AppLanguage.FRENCH -> "Statistiques de progression"
                AppLanguage.ENGLISH -> "Progress Statistics"
                AppLanguage.MALAGASY -> "Taham-pandrosoana ankapobeny"
            },
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onBackground
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Lessons learned statistics
            Card(
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "${completedLessons.size} / 7",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = UISerial.getLabel("lessons", lang),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    LinearProgressIndicator(
                        progress = { completedLessons.size / 7f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(CircleShape),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            // Quizzes metrics
            val quizzesPassed = quizScores.values.count { it >= 4 }
            Card(
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "$quizzesPassed / 7",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = when (lang) {
                            AppLanguage.FRENCH -> "Quiz validés"
                            AppLanguage.ENGLISH -> "Quizzes Passed"
                            AppLanguage.MALAGASY -> "Quiz voavaly tsara"
                        },
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    LinearProgressIndicator(
                        progress = { quizzesPassed / 7f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(CircleShape),
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }
 
            // Flashcards progress
            Card(
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "${masteredFlashcards.size} / 30",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = UISerial.getLabel("flashcards_rate", lang),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    LinearProgressIndicator(
                        progress = { masteredFlashcards.size / 30f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(CircleShape),
                        color = MaterialTheme.colorScheme.tertiary
                    )
                }
            }
        }

        // Active Quick Links to lessons list
        Text(
            text = UISerial.getLabel("all_lessons", lang),
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onBackground
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            lessons.forEach { lesson ->
                val isCompleted = completedLessons.contains(lesson.id)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.selectLesson(lesson) },
                    colors = CardDefaults.cardColors(
                        containerColor = if (isCompleted) MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp)
                        else MaterialTheme.colorScheme.surface
                    ),
                    border = BorderStroke(
                        width = 1.dp,
                        color = if (isCompleted) MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                        else MaterialTheme.colorScheme.outlineVariant
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    if (isCompleted) MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                                    else MaterialTheme.colorScheme.secondary.copy(alpha = 0.15f)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${lesson.id}",
                                color = if (isCompleted) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = lesson.title,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Text(
                                text = "Comprend l'accroche, astuces et réflexion",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        if (isCompleted) {
                            Icon(
                                Icons.Default.CheckCircle,
                                contentDescription = "Completed",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(24.dp)
                            )
                        } else {
                            val score = quizScores[lesson.id]
                            if (score != null) {
                                Text(
                                    text = "$score / 5 ⭐",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                            } else {
                                Icon(
                                    Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = "Read",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LessonsListScreen(
    viewModel: MainViewModel,
    lang: AppLanguage,
    completedLessons: Set<Int>
) {
    val lessons by viewModel.lessons.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    val filteredLessons = remember(lessons, searchQuery) {
        if (searchQuery.isBlank()) {
            lessons
        } else {
            lessons.filter { lesson ->
                lesson.title.contains(searchQuery, ignoreCase = true) ||
                lesson.section1Text.contains(searchQuery, ignoreCase = true) ||
                lesson.section2Text.contains(searchQuery, ignoreCase = true) ||
                lesson.section3Text.contains(searchQuery, ignoreCase = true) ||
                lesson.section4Text.contains(searchQuery, ignoreCase = true) ||
                lesson.section5Text.contains(searchQuery, ignoreCase = true) ||
                lesson.mnemonic.contains(searchQuery, ignoreCase = true)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = UISerial.getLabel("all_lessons", lang),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = UISerial.getLabel("lessons_subtitle", lang),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Interactive Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .testTag("lessons_search_field"),
            placeholder = {
                Text(
                    text = when (lang) {
                        AppLanguage.FRENCH -> "Rechercher une leçon..."
                        AppLanguage.ENGLISH -> "Search a lesson..."
                        AppLanguage.MALAGASY -> "Hitady lesona..."
                    },
                    fontSize = 14.sp
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search icon"
                )
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { searchQuery = "" }) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear search"
                        )
                    }
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline
            )
        )

        if (filteredLessons.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text("🔍", fontSize = 48.sp)
                    Text(
                        text = when (lang) {
                            AppLanguage.FRENCH -> "Aucun résultat trouvé pour « $searchQuery »"
                            AppLanguage.ENGLISH -> "No results found for \"$searchQuery\""
                            AppLanguage.MALAGASY -> "Tsy nisy vokatra hita tamin \"%s\"".format(searchQuery)
                        },
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(filteredLessons, key = { it.id }) { lesson ->
                val isCompleted = completedLessons.contains(lesson.id)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("lesson_card_${lesson.id}")
                        .clickable { viewModel.selectLesson(lesson) },
                    colors = CardDefaults.cardColors(
                        containerColor = if (isCompleted) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f)
                        else MaterialTheme.colorScheme.surface
                    ),
                    border = BorderStroke(
                        width = 1.dp,
                        color = if (isCompleted) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.outlineVariant
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primary),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${lesson.id}",
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Text(
                                text = "LEÇON ${lesson.id}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            if (isCompleted) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = UISerial.getLabel("completed", lang).uppercase(),
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }

                        Text(
                            text = lesson.title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = lesson.section1Text,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}
}

@Composable
fun LessonDetailScreen(
    viewModel: MainViewModel,
    lang: AppLanguage,
    completedLessons: Set<Int>
) {
    val lesson by viewModel.activeLesson.collectAsState()
    val scrollState = rememberScrollState()

    lesson?.let { l ->
        val isCompleted = completedLessons.contains(l.id)
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // Header Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp))
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { viewModel.navigateTo(Screen.LESSONS) },
                    modifier = Modifier.testTag("lesson_back_button")
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Leçon ${l.id} / 7",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )

                // Complete Toolbar check Button
                Button(
                    onClick = { viewModel.toggleLessonCompleted(l.id) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isCompleted) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = if (isCompleted) MaterialTheme.colorScheme.onPrimary
                        else MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    modifier = Modifier.testTag("lesson_check_button"),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            if (isCompleted) Icons.Default.Check else Icons.Default.Add,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = if (isCompleted) UISerial.getLabel("completed", lang) else "Marquer Lu",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Scrollable Content
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(scrollState)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Main Display Title
                Text(
                    text = l.title,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                // SECTION 1: Hook Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    ),
                    border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = l.section1,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        HorizontalDivider(color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f), modifier = Modifier.padding(bottom = 8.dp))
                        FormattedLessonText(text = l.section1Text)
                    }
                }

                // SECTION 2
                SectionBlock(title = l.section2, text = l.section2Text)

                // SECTION 3
                SectionBlock(title = l.section3, text = l.section3Text)

                // MNEMONIC CARD
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.primary)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Star,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = UISerial.getLabel("mnemonic_tip", lang),
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                fontSize = 15.sp,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                            Text(
                                text = l.mnemonic,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }

                // SECTION 4
                SectionBlock(title = l.section4, text = l.section4Text)

                // SECTION 5
                SectionBlock(title = l.section5, text = l.section5Text)

                // REFLECTION QUESTION CARD
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.outlineVariant)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = UISerial.getLabel("reflection", lang),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Text(
                            text = l.question,
                            style = MaterialTheme.typography.bodyMedium,
                            fontStyle = FontStyle.Italic,
                            fontWeight = FontWeight.Medium,
                            lineHeight = 20.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Bottom CTA action button to initiate Quiz
                Button(
                    onClick = { viewModel.startQuiz(l.id) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("lesson_start_quiz_btn"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.Star, contentDescription = null)
                        Text(
                            text = UISerial.getLabel("try_quiz", lang),
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    } ?: run {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
}

@Composable
fun FormattedLessonText(text: String) {
    val lines = text.split("\n")
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        lines.forEach { line ->
            val trimmed = line.trim()
            if (trimmed.isEmpty()) return@forEach

            when {
                // List item with explicit numbering: e.g., "1. Key : Desc" or "1. Desc"
                trimmed.firstOrNull()?.isDigit() == true && trimmed.contains(".") -> {
                    val dotIdx = trimmed.indexOf(".")
                    val numStr = trimmed.substring(0, dotIdx).trim()
                    val restStr = trimmed.substring(dotIdx + 1).trim()
                    val colonIdx = restStr.indexOf(":")
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(start = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Box(
                            modifier = Modifier
                                .padding(top = 2.dp)
                                .size(22.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = numStr,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            if (colonIdx != -1) {
                                val boldPart = restStr.substring(0, colonIdx).trim()
                                val lightPart = restStr.substring(colonIdx + 1).trim()
                                Text(
                                    text = boldPart,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    lineHeight = 20.sp
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = lightPart,
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    lineHeight = 18.sp
                                )
                            } else {
                                Text(
                                    text = restStr,
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    lineHeight = 18.sp
                                )
                            }
                        }
                    }
                }
                // Bullet points: e.g. "- Citoyenneté civile : Description"
                trimmed.startsWith("-") || trimmed.startsWith("•") || trimmed.startsWith("*") -> {
                    val content = trimmed.substring(1).trim()
                    val colonIdx = content.indexOf(":")
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(start = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Box(
                            modifier = Modifier
                                .padding(top = 6.dp)
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.secondary)
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            if (colonIdx != -1) {
                                val boldPart = content.substring(0, colonIdx).trim()
                                val lightPart = content.substring(colonIdx + 1).trim()
                                Text(
                                    text = boldPart,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    lineHeight = 20.sp
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = lightPart,
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    lineHeight = 18.sp
                                )
                            } else {
                                Text(
                                    text = content,
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    lineHeight = 18.sp
                                )
                            }
                        }
                    }
                }
                // Important Warning, Piège or Definition containing a clear colon ": " style, but not starting with list markers
                trimmed.contains(" : ") || trimmed.contains(" – ") -> {
                    val sep = if (trimmed.contains(" : ")) " : " else " – "
                    val parts = trimmed.split(sep, limit = 2)
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp)
                        ),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = "Definition",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = parts[0].trim(),
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontSize = 13.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = parts[1].trim(),
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 13.sp,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }
                // Normal paragraph
                else -> {
                    Text(
                        text = trimmed,
                        style = MaterialTheme.typography.bodyMedium,
                        lineHeight = 21.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(vertical = 2.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun SectionBlock(title: String, text: String) {
    // Check if title has numbered prefix like "1. ", "1) ", "I. "
    val numberRegex = "^\\s*([a-zA-Z0-9]+)[\\.\\)]\\s*(.*)$".toRegex()
    val matchResult = numberRegex.find(title)
    
    val (displayNumber, displayTitle) = if (matchResult != null) {
        Pair(matchResult.groupValues[1], matchResult.groupValues[2])
    } else {
        Pair(null, title)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp)
        ),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                // Beautiful circular numeric or alphabetic badge
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = displayNumber ?: "✦",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }

                Text(
                    text = displayTitle,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    lineHeight = 22.sp,
                    modifier = Modifier.weight(1f)
                )
            }
            
            HorizontalDivider(
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f),
                thickness = 1.dp
            )
            
            FormattedLessonText(text = text)
        }
    }
}

@Composable
fun QuizScreen(
    viewModel: MainViewModel,
    lang: AppLanguage
) {
    val activeQuizLessonId by viewModel.activeQuizLessonId.collectAsState()
    val activeQuestions by viewModel.activeQuizQuestions.collectAsState()
    val currentIndex by viewModel.currentQuizIndex.collectAsState()
    val selectedIndex by viewModel.selectedAnswerIndex.collectAsState()
    val submitted by viewModel.quizSubmitted.collectAsState()
    val score by viewModel.currentQuizScore.collectAsState()
    val stepCompleted by viewModel.quizStepCompleted.collectAsState()
    val lessons by viewModel.lessons.collectAsState()

    val activeLessonTitle = remember(activeQuizLessonId, lessons) {
        lessons.find { it.id == activeQuizLessonId }?.title ?: ""
    }

    if (activeQuestions.isEmpty()) {
        // Render selector of quizzes for all 7 lessons
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = UISerial.getLabel("quizzes", lang),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.secondary
            )
            Text(
                text = UISerial.getLabel("quiz_subtitle", lang),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(lessons, key = { it.id }) { lesson ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.startQuiz(lesson.id) },
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.outlineVariant)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.12f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.Star,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.secondary
                                )
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Quiz Leçon ${lesson.id}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                                Text(
                                    text = lesson.title,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = "Start Quiz",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                            )
                        }
                    }
                }
            }
        }
    } else if (stepCompleted) {
        // Final Score review screen
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(
                        if (score >= 4) Color(0xFF4CAF50).copy(alpha = 0.15f)
                        else MaterialTheme.colorScheme.error.copy(alpha = 0.15f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (score >= 4) "🏆" else "📚",
                    fontSize = 50.sp
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = if (score >= 4) UISerial.getLabel("congrats", lang) else "Entraîne-toi encore !",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = if (score >= 4) Color(0xFF2E7D32) else MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "${UISerial.getLabel("quiz_finished", lang)} : Leçon ${activeQuizLessonId}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = activeLessonTitle,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Score gauge description
            Card(
                modifier = Modifier.width(220.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "SCORE FINAL", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "$score / 5",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (score >= 4) Color(0xFF2E7D32) else MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    val validationMessage = if (score >= 4) "Leçon VALIDÉE ✅" else "Requis : 4 / 5 pour valider ❌"
                    Text(
                        text = validationMessage,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (score >= 4) Color(0xFF2E7D32) else MaterialTheme.colorScheme.error
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = { viewModel.startQuiz(activeQuizLessonId) },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(text = UISerial.getLabel("try_again", lang), fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = { viewModel.resetQuizProgress() },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(text = "Finir / Finish", fontWeight = FontWeight.Bold)
                }
            }
        }
    } else {
        // Individual Question study card
        val question = activeQuestions.getOrNull(currentIndex)
        question?.let { q ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Toolbar Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { viewModel.resetQuizProgress() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Quit")
                    }
                    Column {
                        Text(
                            text = "Quiz Leçon ${activeQuizLessonId}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Text(
                            text = "Question ${currentIndex + 1} sur 5",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Progress Indicators
                LinearProgressIndicator(
                    progress = { (currentIndex + 1) / 5f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(CircleShape),
                    color = MaterialTheme.colorScheme.secondary
                )

                // Question Box
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("question_text_container"),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp)),
                    border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.outlineVariant)
                ) {
                    Text(
                        text = q.question,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(16.dp),
                        lineHeight = 22.sp
                    )
                }

                // Answer Options Mapping
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    q.options.forEachIndexed { idx, opt ->
                        val isSelected = selectedIndex == idx
                        val isCorrect = q.correctIndex == idx

                        val cardColor = when {
                            submitted && isCorrect -> Color(0xFFE8F5E9)   // Green success
                            submitted && isSelected && !isCorrect -> Color(0xFFFFEBEE) // Red failure
                            isSelected -> MaterialTheme.colorScheme.secondaryContainer
                            else -> MaterialTheme.colorScheme.surface
                        }

                        val strokeColor = when {
                            submitted && isCorrect -> Color(0xFF4CAF50)
                            submitted && isSelected && !isCorrect -> Color(0xFFE57373)
                            isSelected -> MaterialTheme.colorScheme.secondary
                            else -> MaterialTheme.colorScheme.outlineVariant
                        }

                        val contentColor = when {
                            submitted && isCorrect -> Color(0xFF2E7D32)
                            submitted && isSelected && !isCorrect -> Color(0xFFC62828)
                            isSelected -> MaterialTheme.colorScheme.onSecondaryContainer
                            else -> MaterialTheme.colorScheme.onSurface
                        }

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("quiz_option_$idx")
                                .clickable { viewModel.selectQuizOption(idx) },
                            colors = CardDefaults.cardColors(containerColor = cardColor),
                            border = BorderStroke(width = 1.dp, color = strokeColor)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = opt,
                                    fontSize = 14.sp,
                                    fontWeight = if (isSelected || (submitted && isCorrect)) FontWeight.Bold else FontWeight.Normal,
                                    color = contentColor,
                                    modifier = Modifier.weight(1f)
                                )

                                if (submitted) {
                                    if (isCorrect) {
                                        Text(text = "✅", fontSize = 16.sp)
                                    } else if (isSelected) {
                                        Text(text = "❌", fontSize = 16.sp)
                                    }
                                }
                            }
                        }
                    }
                }

                // Explanation Block
                if (submitted) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("explanation_card"),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8E1)),
                        border = BorderStroke(width = 1.dp, color = Color(0xFFFFD54F))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "EXPLICATION :",
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFF57F17),
                                fontSize = 12.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = q.explanation,
                                fontSize = 13.sp,
                                color = Color(0xFF5D4037)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                // Action validation buttons
                if (!submitted) {
                    Button(
                        onClick = { viewModel.submitQuizAnswer() },
                        enabled = selectedIndex != -1,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("submit_answer_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = if (selectedIndex == -1) UISerial.getLabel("not_answered", lang) else UISerial.getLabel("submit", lang),
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else {
                    Button(
                        onClick = { viewModel.advanceQuiz() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("next_question_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = UISerial.getLabel("next", lang),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FlashcardsScreen(
    viewModel: MainViewModel,
    lang: AppLanguage,
    masteredCards: Set<Int>
) {
    val flashcards by viewModel.flashcards.collectAsState()
    var currentIndex by remember { mutableStateOf(0) }
    var isFlipped by remember { mutableStateOf(false) }

    // Study filter state
    // 0 = All cards, 1 = Mastered only, 2 = Study only
    var activeFilter by remember { mutableStateOf(0) }

    val filteredList = remember(flashcards, masteredCards, activeFilter) {
        when (activeFilter) {
            1 -> flashcards.filter { masteredCards.contains(it.id) }
            2 -> flashcards.filter { !masteredCards.contains(it.id) }
            else -> flashcards
        }
    }

    // Reset screen card indexes if filter list updates
    LaunchedEffect(filteredList) {
        currentIndex = 0
        isFlipped = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = UISerial.getLabel("flashcards", lang),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.tertiary
        )
        Text(
            text = UISerial.getLabel("flash_subtitle", lang),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        // Filters mapping
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            FilterChip(
                selected = activeFilter == 0,
                onClick = { activeFilter = 0 },
                label = { Text("Tous (30)", fontSize = 11.sp) }
            )
            FilterChip(
                selected = activeFilter == 1,
                onClick = { activeFilter = 1 },
                label = { Text("Appris : ${masteredCards.size}", fontSize = 11.sp) }
            )
            FilterChip(
                selected = activeFilter == 2,
                onClick = { activeFilter = 2 },
                label = { Text("À réviser : ${30 - masteredCards.size}", fontSize = 11.sp) }
            )
        }

        if (filteredList.isEmpty()) {
            // Render friendly empty feedback state
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "🎴", fontSize = 48.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Aucune carte dans cette catégorie.",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Mets à jour le statut de tes cartes révisées !",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            val card = filteredList.getOrNull(currentIndex)
            card?.let { c ->
                val isCardMastered = masteredCards.contains(c.id)

                // Flipping perspective animations
                val rotationAngle by animateFloatAsState(
                    targetValue = if (isFlipped) 180f else 0f,
                    animationSpec = tween(durationMillis = 400),
                    label = "flip_rotation"
                )

                // Study Central 3D Card Containment
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .testTag("flashcard_container")
                        .graphicsLayer {
                            rotationY = rotationAngle
                            cameraDistance = 12f * density
                        }
                        .clickable { isFlipped = !isFlipped }
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            if (isFlipped) MaterialTheme.colorScheme.surfaceVariant
                            else MaterialTheme.colorScheme.tertiaryContainer
                        )
                        .border(
                            width = 2.dp,
                            color = if (isFlipped) MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
                            else MaterialTheme.colorScheme.tertiary,
                            shape = RoundedCornerShape(16.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        if (rotationAngle <= 90f) {
                            // FRONT SIDE
                            Text(
                                text = "TERME / CONCEPT",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.tertiary
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = c.term,
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.ExtraBold,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                        } else {
                            // BACK SIDE: inverted graphic layout
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .graphicsLayer { rotationY = 180f }
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = "DÉFINITION",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text(
                                        text = c.definition,
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = FontWeight.Medium,
                                        textAlign = TextAlign.Center,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        lineHeight = 24.sp
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            text = UISerial.getLabel("tap_to_flip", lang),
                            fontSize = 11.sp,
                            fontStyle = FontStyle.Italic,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                        )
                    }
                }

                // Quick Mastered Toggle Controls
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Maîtrise de la carte :",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Button(
                            onClick = { viewModel.toggleFlashcardMastered(c.id) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isCardMastered) Color(0xFF2E7D32) else MaterialTheme.colorScheme.error
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = if (isCardMastered) UISerial.getLabel("mastered", lang) else UISerial.getLabel("not_mastered", lang),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // Deck navigation arrows
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            if (currentIndex > 0) {
                                currentIndex -= 1
                                isFlipped = false
                            }
                        },
                        enabled = currentIndex > 0,
                        modifier = Modifier.testTag("flashcard_prev_btn")
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Previous")
                    }

                    Text(
                        text = "CARTE ${currentIndex + 1} SUR ${filteredList.size}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    IconButton(
                        onClick = {
                            if (currentIndex < filteredList.size - 1) {
                                currentIndex += 1
                                isFlipped = false
                            }
                        },
                        enabled = currentIndex < filteredList.size - 1,
                        modifier = Modifier.testTag("flashcard_next_btn")
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Next")
                    }
                }
            }
        }
    }
}

@Composable
fun AboutScreen(
    viewModel: MainViewModel,
    lang: AppLanguage,
    completedLessons: Set<Int>,
    quizScores: Map<Int, Int>,
    masteredFlashcards: Set<Int>
) {
    val scrollState = rememberScrollState()
    var selectedPillarIndex by remember { mutableStateOf(0) }

    val pillars = listOf(
        Triple(
            when (lang) {
                AppLanguage.FRENCH -> "1. Citoyenneté Active & Civisme"
                AppLanguage.ENGLISH -> "1. Active Citizenship & Civics"
                AppLanguage.MALAGASY -> "1. Maha-olompirenena Mavitrika"
            },
            when (lang) {
                AppLanguage.FRENCH -> "S'approprier ses droits et devoirs civiques. La citoyenneté implique l'exercice actif du civisme, la participation à la vie communautaire, le respect des lois et des institutions, et l'amour de la patrie."
                AppLanguage.ENGLISH -> "Owning civic rights and duties. Citizenship involves the active exercise of civic action, participation in community life, respect for laws and institutions, and patriotism."
                AppLanguage.MALAGASY -> "Fahafantarana sy fametrahana ny zo sy adidin'ny olom-pirenena. Ny maha-olona mavitrika dia mitaky fandraisana anjara, fanatanterahana adidy amin'ny firenena sy ny fiarahamonina."
            },
            listOf(
                when (lang) {
                    AppLanguage.FRENCH -> "Droit de vote et d'éligibilité aux affaires politiques."
                    AppLanguage.ENGLISH -> "Voting rights and eligibility for policy-making."
                    AppLanguage.MALAGASY -> "Zon'ny olom-pirenena handatsa-bato sy hofidiana amin'ny raharaham-panjakana."
                },
                when (lang) {
                    AppLanguage.FRENCH -> "Devoir de payer les impôts et de respecter le bien public."
                    AppLanguage.ENGLISH -> "Duty to pay taxes and preserve public property."
                    AppLanguage.MALAGASY -> "Adidy handoa hetra sy hanaja ny fananam-bahoaka."
                },
                when (lang) {
                    AppLanguage.FRENCH -> "Principes républicains fondamentaux : Liberté, Égalité, Fraternité."
                    AppLanguage.ENGLISH -> "Core democratic principles: Liberty, Equality, Fraternity."
                    AppLanguage.MALAGASY -> "Fototra ho an'ny repoblika: Fahafahana, Fitoviana, Firaisankina."
                }
            )
        ),
        Triple(
            when (lang) {
                AppLanguage.FRENCH -> "2. Droits de l'Homme (DUDH)"
                AppLanguage.ENGLISH -> "2. Universal Human Rights"
                AppLanguage.MALAGASY -> "2. Zon'Olombelona Iraisam-pirenena"
            },
            when (lang) {
                AppLanguage.FRENCH -> "La Déclaration Universelle des Droits de l'Homme (DUDH) de 1948 proclame les droits inaliénables inhérents à chaque être humain, sans distinction de race, de genre, de religion ou d'origin."
                AppLanguage.ENGLISH -> "The Universal Declaration of Human Rights (UDHR) of 1948 proclaims the inalienable rights inherent to every human, without distinction of race, gender, religion, or origin."
                AppLanguage.MALAGASY -> "Ny Fanambarana Iraisam-pirenena momba ny Zon'olombelona (DUDH) 1948 dia manasongadina ny zo tsy azo esorina ho an'ny olona tsirairay, tsy mila fanavakavahana."
            },
            listOf(
                when (lang) {
                    AppLanguage.FRENCH -> "Droit à la vie, à la liberté et à la sécurité personnelle de chacun."
                    AppLanguage.ENGLISH -> "Right to life, liberty, and personal security for all."
                    AppLanguage.MALAGASY -> "Zo ho velona, ho afaka ary ho voaaro ny maha-izy azy any rehetra any."
                },
                when (lang) {
                    AppLanguage.FRENCH -> "Interdiction absolue de la torture et de l'esclavage sous toutes formes."
                    AppLanguage.ENGLISH -> "Strict prohibition of torture and slavery in any form."
                    AppLanguage.MALAGASY -> "Fandrarana ny fampijaliana sy ny fanandevozana amin'ny endriny rehetra."
                },
                when (lang) {
                    AppLanguage.FRENCH -> "Liberté fondamentale d'expression, de pensée et de réunion pacifique."
                    AppLanguage.ENGLISH -> "Core freedoms of expression, thought, and peaceful assembly."
                    AppLanguage.MALAGASY -> "Fahafahana maneho hevitra, misaina, ary mivory am-pilaminana."
                }
            )
        ),
        Triple(
            when (lang) {
                AppLanguage.FRENCH -> "3. Protection de l'Environnement"
                AppLanguage.ENGLISH -> "3. Environmental Preservation"
                AppLanguage.MALAGASY -> "3. Fiarovana ny Tontolo Iainana"
            },
            when (lang) {
                AppLanguage.FRENCH -> "Madagascar abrite une biodiversité unique au monde qu'il est crucial de protéger. L'éducation environnementale engage la responsabilité citoyenne face au réchauffement climatique et aux désastres écologiques terrestres."
                AppLanguage.ENGLISH -> "Madagascar is home to a globally unique biodiversity that is urgent to protect. Environmental education channels civic action against global warming and ecological crises."
                AppLanguage.MALAGASY -> "Manana zava-manan'aina miavaka sy tsy hita any an-kafa i Madagasikara. Ny fizarana fahalalana momba ny tontolo iainana dia nampandriana ho an'ny andraikitry ny olom-pirenena."
            },
            listOf(
                when (lang) {
                    AppLanguage.FRENCH -> "Préservation de la faune et de la flore exceptionnelles de la Grande Île."
                    AppLanguage.ENGLISH -> "Preserving the exceptional flora and fauna of Madagascar."
                    AppLanguage.MALAGASY -> "Fiarovana ny biby sy ny zava-maniry mampiavaka ny Nosintsika."
                },
                when (lang) {
                    AppLanguage.FRENCH -> "Action d'éco-citoyenneté : gestion constructive des déchets et reboisement actif."
                    AppLanguage.ENGLISH -> "Eco-citizenship: proactive waste management and active reforestation."
                    AppLanguage.MALAGASY -> "Andraikitra maha-olom-pirenena: fikojakojana fako sy fambolen-kazo mavitrika."
                },
                when (lang) {
                    AppLanguage.FRENCH -> "Lutte contre la déforestation sauvage et les feux de brousse agressifs."
                    AppLanguage.ENGLISH -> "Combating illegal logging, poaching, and destructive bushfires."
                    AppLanguage.MALAGASY -> "Fanoherana ny doro ala, doro tanety, ary ny fitrandrahana tsy ara-dalàna."
                }
            )
        ),
        Triple(
            when (lang) {
                AppLanguage.FRENCH -> "4. Lutte contre la Corruption"
                AppLanguage.ENGLISH -> "4. Anti-Corruption Crusade"
                AppLanguage.MALAGASY -> "4. Ady amin'ny Kolikoly"
            },
            when (lang) {
                AppLanguage.FRENCH -> "La corruption détruit la confiance civique et freine le développement économique. L'intégrité personnelle est le premier rempart républicain pour ériger une société honnête et juste."
                AppLanguage.ENGLISH -> "Corruption erodes public trust and hinders sustainable growth. Individual integrity remains the basic pillar of a fair, democratic, and honest society."
                AppLanguage.MALAGASY -> "Mandringana ny fitokisan'ny vahoaka sy mampihemotra fampandrosoana ny kolikoly. Ny fahitsian-tena no ampinga voalohany hanorenana fiarahamonina marina."
            },
            listOf(
                when (lang) {
                    AppLanguage.FRENCH -> "Sensibilisation aux rôles des organes de contrôle nationaux, tels que le BIANCO."
                    AppLanguage.ENGLISH -> "Raising awareness of the role of anti-corruption agencies like BIANCO."
                    AppLanguage.MALAGASY -> "Fampahafantarana ny andraikitry ny rafitra miady amin'ny kolikoly toy ny BIANCO."
                },
                when (lang) {
                    AppLanguage.FRENCH -> "Promotion absolue du mérite, de la redevabilité et de la transparence."
                    AppLanguage.ENGLISH -> "Absolute promotion of meritocracy, transparency, and accountability."
                    AppLanguage.MALAGASY -> "Fampiroboroboana ny fahamendrehana, ny fangaraharana ary ny rariny."
                },
                when (lang) {
                    AppLanguage.FRENCH -> "Le refus catégorique des pots-de-vin et la dénonciation des abus de pouvoir."
                    AppLanguage.ENGLISH -> "Category refusal of taking or offering bribes, and reporting power abuses."
                    AppLanguage.MALAGASY -> "Fandavana an-kitsirano ny kolikoly sy fahasahiana mitoroka izany."
                }
            )
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Upper Title
        Text(
            text = when (lang) {
                AppLanguage.FRENCH -> "Documentations Officielles"
                AppLanguage.ENGLISH -> "Official Curriculum Docs"
                AppLanguage.MALAGASY -> "Tari-dalana sy Ny Dinan'ny Olompirenena"
            },
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = UISerial.getLabel("about_subtitle", lang),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        // Horizontal Pillar Selector Tabs (Material 3 style)
        val tabIcons = listOf("🏛️", "⚖️", "🌳", "🛡️")
        val tabLabels = listOf(
            when (lang) {
                AppLanguage.FRENCH -> "Civisme"
                AppLanguage.ENGLISH -> "Civics"
                AppLanguage.MALAGASY -> "Civisme"
            },
            when (lang) {
                AppLanguage.FRENCH -> "Droits"
                AppLanguage.ENGLISH -> "Rights"
                AppLanguage.MALAGASY -> "Zo"
            },
            when (lang) {
                AppLanguage.FRENCH -> "Ecol."
                AppLanguage.ENGLISH -> "Ecology"
                AppLanguage.MALAGASY -> "Tontolo"
            },
            when (lang) {
                AppLanguage.FRENCH -> "Intégrité"
                AppLanguage.ENGLISH -> "Integrity"
                AppLanguage.MALAGASY -> "Hitsy"
            }
        )

        ScrollableTabRow(
            selectedTabIndex = selectedPillarIndex,
            edgePadding = 0.dp,
            containerColor = Color.Transparent,
            indicator = { tabPositions ->
                if (selectedPillarIndex < tabPositions.size) {
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedPillarIndex]),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        ) {
            tabLabels.forEachIndexed { index, label ->
                val isSelected = selectedPillarIndex == index
                Tab(
                    selected = isSelected,
                    onClick = { selectedPillarIndex = index },
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(text = tabIcons[index], fontSize = 16.sp)
                            Text(
                                text = label,
                                fontSize = 13.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                )
            }
        }

        // Active Pillar Detail Card
        val activePillar = pillars[selectedPillarIndex]
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.25f)
            ),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.4f))
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = tabIcons[selectedPillarIndex], fontSize = 24.sp)
                    Text(
                        text = activePillar.first,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                HorizontalDivider(color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))

                Text(
                    text = activePillar.second,
                    style = MaterialTheme.typography.bodyMedium,
                    lineHeight = 22.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Bullet points
                activePillar.third.forEach { point ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(
                            text = "⚡",
                            fontSize = 12.sp,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                        Text(
                            text = point,
                            fontSize = 13.sp,
                            lineHeight = 18.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // Summary Statistics List
        Text(
            text = when (lang) {
                AppLanguage.FRENCH -> "Résumé Global d'Apprentissage"
                AppLanguage.ENGLISH -> "Global Course Summary"
                AppLanguage.MALAGASY -> "Taham-pandrosoana Ankapobeny"
            },
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
        ) {
            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                val lessonText = when (lang) {
                    AppLanguage.FRENCH -> "${completedLessons.size} / 7 appris"
                    AppLanguage.ENGLISH -> "${completedLessons.size} / 7 studied"
                    AppLanguage.MALAGASY -> "${completedLessons.size} / 7 voavaky"
                }

                val quizText = when (lang) {
                    AppLanguage.FRENCH -> "${quizScores.size} / 7 validés"
                    AppLanguage.ENGLISH -> "${quizScores.size} / 7 completed"
                    AppLanguage.MALAGASY -> "${quizScores.size} / 7 vita"
                }

                val cardsText = when (lang) {
                    AppLanguage.FRENCH -> "${masteredFlashcards.size} / 30 maîtrisées"
                    AppLanguage.ENGLISH -> "${masteredFlashcards.size} / 30 mastered"
                    AppLanguage.MALAGASY -> "${masteredFlashcards.size} / 30 voafehy"
                }

                SummaryRow(label = UISerial.getLabel("total_lessons", lang), stateText = lessonText)
                SummaryRow(label = UISerial.getLabel("total_quizzes", lang), stateText = quizText)
                SummaryRow(label = UISerial.getLabel("total_cards", lang), stateText = cardsText)
            }
        }

        // DUDH Core Proclamation
        Text(
            text = UISerial.getLabel("credits_title", lang),
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f))
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = when (lang) {
                        AppLanguage.FRENCH -> "Éducation Civique & Sensibilisation"
                        AppLanguage.ENGLISH -> "Civic Education & Awareness"
                        AppLanguage.MALAGASY -> "Fanabeazana ny Olom-pirenena"
                    },
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = UISerial.getLabel("credits_body", lang),
                    fontSize = 13.sp,
                    lineHeight = 20.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        // Author / Metadata Section
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp)),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = when (lang) {
                        AppLanguage.FRENCH -> "APPLICATION OFFICIELLE"
                        AppLanguage.ENGLISH -> "OFFICIAL APPLICATION"
                        AppLanguage.MALAGASY -> "FAMPANDRANA OFISIALY"
                    }.uppercase(),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary,
                    letterSpacing = 1.2.sp
                )
                Text(
                    text = "Sylvain SOLOFONIAINA",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = when (lang) {
                        AppLanguage.FRENCH -> "Éditeur & Concepteur National"
                        AppLanguage.ENGLISH -> "National Designer & Editor"
                        AppLanguage.MALAGASY -> "Mpanoratra sy Mpamorona"
                    },
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = when (lang) {
                        AppLanguage.FRENCH -> "Édition du 30 Mai 2026"
                        AppLanguage.ENGLISH -> "Edition of May 30, 2026"
                        AppLanguage.MALAGASY -> "Famoahana: 30 Mey 2026"
                    },
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Version 1.2.0-PRO",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }
    }
}

@Composable
fun SummaryRow(label: String, stateText: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, fontWeight = FontWeight.Medium, fontSize = 14.sp)
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(text = stateText, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer)
        }
    }
}
