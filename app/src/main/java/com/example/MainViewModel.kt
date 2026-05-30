package com.example

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = DataRepository(application)
    private val prefs = application.getSharedPreferences("eac_seconde_prefs", Context.MODE_PRIVATE)

    private val _currentLanguage = MutableStateFlow(AppLanguage.FRENCH)
    val currentLanguage: StateFlow<AppLanguage> = _currentLanguage.asStateFlow()

    private val _currentScreen = MutableStateFlow(Screen.DASHBOARD)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    // Loaded data
    private val _lessons = MutableStateFlow<List<Lesson>>(emptyList())
    val lessons: StateFlow<List<Lesson>> = _lessons.asStateFlow()

    private val _allQuizzes = MutableStateFlow<List<QuizQuestion>>(emptyList())
    val allQuizzes: StateFlow<List<QuizQuestion>> = _allQuizzes.asStateFlow()

    private val _flashcards = MutableStateFlow<List<Flashcard>>(emptyList())
    val flashcards: StateFlow<List<Flashcard>> = _flashcards.asStateFlow()

    // Persistent studies status saved in SharedPreferences
    private val _completedLessons = MutableStateFlow<Set<Int>>(emptySet())
    val completedLessons: StateFlow<Set<Int>> = _completedLessons.asStateFlow()

    private val _masteredFlashcards = MutableStateFlow<Set<Int>>(emptySet())
    val masteredFlashcards: StateFlow<Set<Int>> = _masteredFlashcards.asStateFlow()

    private val _quizScores = MutableStateFlow<Map<Int, Int>>(emptyMap())
    val quizScores: StateFlow<Map<Int, Int>> = _quizScores.asStateFlow()

    // Active detail states
    private val _activeLesson = MutableStateFlow<Lesson?>(null)
    val activeLesson: StateFlow<Lesson?> = _activeLesson.asStateFlow()

    // Active quiz session states
    private val _activeQuizLessonId = MutableStateFlow(1)
    val activeQuizLessonId: StateFlow<Int> = _activeQuizLessonId.asStateFlow()

    private val _activeQuizQuestions = MutableStateFlow<List<QuizQuestion>>(emptyList())
    val activeQuizQuestions: StateFlow<List<QuizQuestion>> = _activeQuizQuestions.asStateFlow()

    private val _currentQuizIndex = MutableStateFlow(0)
    val currentQuizIndex: StateFlow<Int> = _currentQuizIndex.asStateFlow()

    private val _selectedAnswerIndex = MutableStateFlow(-1)
    val selectedAnswerIndex: StateFlow<Int> = _selectedAnswerIndex.asStateFlow()

    private val _quizSubmitted = MutableStateFlow(false)
    val quizSubmitted: StateFlow<Boolean> = _quizSubmitted.asStateFlow()

    private val _currentQuizScore = MutableStateFlow(0)
    val currentQuizScore: StateFlow<Int> = _currentQuizScore.asStateFlow()

    private val _quizStepCompleted = MutableStateFlow(false)
    val quizStepCompleted: StateFlow<Boolean> = _quizStepCompleted.asStateFlow()

    init {
        loadPersistedData()
        loadLanguageData(AppLanguage.FRENCH)
    }

    private fun loadPersistedData() {
        val completedString = prefs.getStringSet("completed_lessons", emptySet()) ?: emptySet()
        _completedLessons.value = completedString.mapNotNull { it.toIntOrNull() }.toSet()

        val masteredString = prefs.getStringSet("mastered_flashcards", emptySet()) ?: emptySet()
        _masteredFlashcards.value = masteredString.mapNotNull { it.toIntOrNull() }.toSet()

        val scoresMap = mutableMapOf<Int, Int>()
        for (i in 1..7) {
            val score = prefs.getInt("quiz_score_$i", -1)
            if (score != -1) {
                scoresMap[i] = score
            }
        }
        _quizScores.value = scoresMap
    }

    private fun loadLanguageData(lang: AppLanguage) {
        _lessons.value = repository.loadLessons(lang)
        _allQuizzes.value = repository.loadQuiz(lang)
        _flashcards.value = repository.loadFlashcards(lang)

        // update active lesson content with translation if it exists
        val currentActiveId = _activeLesson.value?.id
        if (currentActiveId != null) {
            _activeLesson.value = _lessons.value.find { it.id == currentActiveId }
        }
    }

    fun setLanguage(lang: AppLanguage) {
        _currentLanguage.value = lang
        loadLanguageData(lang)
    }

    fun navigateTo(screen: Screen) {
        _currentScreen.value = screen
    }

    fun selectLesson(lesson: Lesson) {
        _activeLesson.value = lesson
        _currentScreen.value = Screen.LESSON_DETAIL
    }

    fun toggleLessonCompleted(lessonId: Int) {
        val current = _completedLessons.value.toMutableSet()
        if (current.contains(lessonId)) {
            current.remove(lessonId)
        } else {
            current.add(lessonId)
        }
        _completedLessons.value = current
        prefs.edit().putStringSet("completed_lessons", current.map { it.toString() }.toSet()).apply()
    }

    fun toggleFlashcardMastered(cardId: Int) {
        val current = _masteredFlashcards.value.toMutableSet()
        if (current.contains(cardId)) {
            current.remove(cardId)
        } else {
            current.add(cardId)
        }
        _masteredFlashcards.value = current
        prefs.edit().putStringSet("mastered_flashcards", current.map { it.toString() }.toSet()).apply()
    }

    fun startQuiz(lessonId: Int) {
        _activeQuizLessonId.value = lessonId
        val questions = _allQuizzes.value.filter { it.lessonId == lessonId }
        _activeQuizQuestions.value = questions
        _currentQuizIndex.value = 0
        _selectedAnswerIndex.value = -1
        _quizSubmitted.value = false
        _currentQuizScore.value = 0
        _quizStepCompleted.value = false
        _currentScreen.value = Screen.QUIZ
    }

    fun selectQuizOption(optionIndex: Int) {
        if (!_quizSubmitted.value) {
            _selectedAnswerIndex.value = optionIndex
        }
    }

    fun submitQuizAnswer() {
        if (_selectedAnswerIndex.value != -1 && !_quizSubmitted.value) {
            _quizSubmitted.value = true
            val currentQuestion = _activeQuizQuestions.value.getOrNull(_currentQuizIndex.value)
            if (currentQuestion != null && _selectedAnswerIndex.value == currentQuestion.correctIndex) {
                _currentQuizScore.value += 1
            }
        }
    }

    fun advanceQuiz() {
        val nextIndex = _currentQuizIndex.value + 1
        if (nextIndex < _activeQuizQuestions.value.size) {
            _currentQuizIndex.value = nextIndex
            _selectedAnswerIndex.value = -1
            _quizSubmitted.value = false
        } else {
            // Quiz finished! Persist high score if higher
            val lessonId = _activeQuizLessonId.value
            val currentHighScore = _quizScores.value[lessonId] ?: 0
            val finalScore = _currentQuizScore.value
            if (finalScore > currentHighScore) {
                val updatedScores = _quizScores.value.toMutableMap()
                updatedScores[lessonId] = finalScore
                _quizScores.value = updatedScores
                prefs.edit().putInt("quiz_score_$lessonId", finalScore).apply()
            }
            // Mark lesson as completed automatically if they score 4 or 5
            if (finalScore >= 4) {
                val current = _completedLessons.value.toMutableSet()
                if (!current.contains(lessonId)) {
                    current.add(lessonId)
                    _completedLessons.value = current
                    prefs.edit().putStringSet("completed_lessons", current.map { it.toString() }.toSet()).apply()
                }
            }
            _quizStepCompleted.value = true
        }
    }

    fun resetQuizProgress() {
        _quizStepCompleted.value = false
        _selectedAnswerIndex.value = -1
        _quizSubmitted.value = false
        _currentScreen.value = Screen.LESSONS
    }
}
