package com.example

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Lesson(
    val id: Int,
    val title: String,
    val section1: String,
    val section1Text: String,
    val section2: String,
    val section2Text: String,
    val section3: String,
    val section3Text: String,
    val mnemonic: String,
    val section4: String,
    val section4Text: String,
    val section5: String,
    val section5Text: String,
    val question: String
)

@JsonClass(generateAdapter = true)
data class QuizQuestion(
    val lessonId: Int,
    val id: Int,
    val question: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String
)

@JsonClass(generateAdapter = true)
data class Flashcard(
    val id: Int,
    val term: String,
    val definition: String
)

enum class AppLanguage(val code: String, val label: String, val flag: String) {
    FRENCH("fr", "Français", "🇫🇷"),
    ENGLISH("en", "English", "🇬🇧"),
    MALAGASY("mg", "Malagasy", "🇲🇬")
}

enum class Screen {
    DASHBOARD,
    LESSONS,
    LESSON_DETAIL,
    QUIZ,
    FLASHCARDS,
    ABOUT
}

object UISerial {
    fun getLabel(key: String, lang: AppLanguage): String {
        return when (lang) {
            AppLanguage.FRENCH -> when (key) {
                "dashboard_title" -> "Tableau"
                "lessons" -> "Leçons"
                "quizzes" -> "Quiz"
                "flashcards" -> "Cartes"
                "about" -> "Docs"
                "all_lessons" -> "Toutes les Leçons"
                "completed" -> "Complété"
                "completion_rate" -> "Taux de complétion"
                "quiz_scores" -> "Scores de Quiz"
                "flashcards_rate" -> "Cartes maîtrisées"
                "select_language" -> "Langue"
                "start_reading" -> "Lire la leçon"
                "mnemonic_tip" -> "Moyen Mnémotechnique :"
                "reflection" -> "Question de réflexion :"
                "not_answered" -> "Aucune réponse sélectionnée"
                "submit" -> "Valider"
                "next" -> "Continuer"
                "back" -> "Retour"
                "try_quiz" -> "Tenter le Quiz"
                "congrats" -> "Félicitations !"
                "try_again" -> "Réessayer"
                "quiz_finished" -> "Quiz terminé"
                "correct" -> "Correct ! Bien joué !"
                "incorrect" -> "Incorrect. Révise encore !"
                "explanation" -> "Explication :"
                "welcome_back" -> "Bienvenue à l'EAC Seconde"
                "quote" -> "« La citoyenneté, c'est l'ensemble des règles qui permettent de vivre ensemble. »"
                "about_desc" -> "Cette application éducative a été conçue pour aider les élèves de Seconde à s'approprier les concepts de citoyenneté, de droits de l'homme, de protection environnementale et de lutte anti-corruption."
                "tap_to_flip" -> "Touchez la carte pour la retourner"
                "mastered" -> "Maîtrisée ✅"
                "not_mastered" -> "À réviser ❌"
                "lessons_subtitle" -> "Apprendre activement"
                "quiz_subtitle" -> "Tester tes connaissances"
                "flash_subtitle" -> "Entraîner ta mémoire"
                "about_subtitle" -> "Guide et Curriculum Officiel"
                "total_lessons" -> "7 Leçons obligatoires"
                "total_cards" -> "30 Flashcards interactives"
                "total_quizzes" -> "7 Épreuves de QCM"
                "credits_title" -> "Directives Pédagogiques (EAC)"
                "credits_body" -> "Éducation à la Citoyenneté (EAC) - Classe de Seconde.\nDéveloppé conformément aux directives pédagogiques nationales et universelles."
                else -> key
            }
            AppLanguage.ENGLISH -> when (key) {
                "dashboard_title" -> "Dashboard"
                "lessons" -> "Lessons"
                "quizzes" -> "Quizzes"
                "flashcards" -> "Cards"
                "about" -> "Docs"
                "all_lessons" -> "All Lessons"
                "completed" -> "Completed"
                "completion_rate" -> "Completion rate"
                "quiz_scores" -> "Quiz Scores"
                "flashcards_rate" -> "Mastered cards"
                "select_language" -> "Language"
                "start_reading" -> "Read Lesson"
                "mnemonic_tip" -> "Mnemonic tip:"
                "reflection" -> "Reflection question:"
                "not_answered" -> "No answer selected"
                "submit" -> "Submit Answer"
                "next" -> "Continue"
                "back" -> "Back"
                "try_quiz" -> "Try the Quiz"
                "congrats" -> "Congratulations!"
                "try_again" -> "Try Again"
                "quiz_finished" -> "Quiz Finished"
                "correct" -> "Correct! Good job!"
                "incorrect" -> "Incorrect. Review again!"
                "explanation" -> "Explanation:"
                "welcome_back" -> "Welcome to EAC Seconde"
                "quote" -> "“Citizenship is the set of rules that allow us to live together.”"
                "about_desc" -> "This educational application is designed to help Grade 10 students master the concepts of citizenship, human rights, environmental protection, and anti-corruption."
                "tap_to_flip" -> "Tap card to flip"
                "mastered" -> "Mastered ✅"
                "not_mastered" -> "To study ❌"
                "lessons_subtitle" -> "Active Learning"
                "quiz_subtitle" -> "Test Your Knowledge"
                "flash_subtitle" -> "Train Your Memory"
                "about_subtitle" -> "Official Program Guidelines"
                "total_lessons" -> "7 Compulsory Lessons"
                "total_cards" -> "30 Interactive Flashcards"
                "total_quizzes" -> "7 MCQ Tests"
                "credits_title" -> "Pedagogical Directives & Curriculum"
                "credits_body" -> "Civic Education (EAC) - Grade 10.\nDeveloped in conformity with national and universal pedagogical guidelines."
                else -> key
            }
            AppLanguage.MALAGASY -> when (key) {
                "dashboard_title" -> "Fandraisana"
                "lessons" -> "Lesona"
                "quizzes" -> "Quiz"
                "flashcards" -> "Karatra"
                "about" -> "Tari-dalana"
                "all_lessons" -> "Lesona Rehetra"
                "completed" -> "Vita"
                "completion_rate" -> "Taham-pandrosoana"
                "quiz_scores" -> "Naoty azo amin'ny Quiz"
                "flashcards_rate" -> "Karatra efa voafehy"
                "select_language" -> "Fisafidianana ny Teny"
                "start_reading" -> "Vakio ny Lesona"
                "mnemonic_tip" -> "Fomba fitadidiana mivaingana :"
                "reflection" -> "Fanontaniana hisainana :"
                "not_answered" -> "Tsy nisy valiny voafidy"
                "submit" -> "Hamarino ny valiny"
                "next" -> "Manohy"
                "back" -> "Hiverina"
                "try_quiz" -> "Andramo ny Quiz"
                "congrats" -> "Arahabaina !"
                "try_again" -> "Andramo indray"
                "quiz_finished" -> "Vita ny Quiz"
                "correct" -> "Marina ! Tsara be !"
                "incorrect" -> "Diso ! Avereno dinihina indray !"
                "explanation" -> "Famaritana fanazavana :"
                "welcome_back" -> "Tongasoa eto amin'ny EAC Seconde"
                "quote" -> "« Ny maha-olom-pirenena dia ny fitambaran'ny fitsipika ahafahantsika miara-monina. »"
                "about_desc" -> "Ity fitaovana fanabeazana ity dia natao hanampiana ny mpianatra amin'ny kilasy Seconde hampiofana ny saina momba ny maha-olom-pirenena, tontolo iainana ary ny ady amin'ny kolikoly."
                "tap_to_flip" -> "Kitiho ny karatra mba hanodinana azy"
                "mastered" -> "Voafehy ✅"
                "not_mastered" -> "Haverina hojerena ❌"
                "lessons_subtitle" -> "Fianarana mavitrika"
                "quiz_subtitle" -> "Hamarino ny fahalalanao"
                "flash_subtitle" -> "Ofanina ny fitadidiana"
                "about_subtitle" -> "Tari-dalana sy Ny Dinan'ny Olompirenena"
                "total_lessons" -> "Lesona fototra miisa 7"
                "total_cards" -> "Karatra miisa 30"
                "total_quizzes" -> "Quiz MCQ miisa 7"
                "credits_title" -> "Fandaharam-pianarana Ofisialy"
                "credits_body" -> "Fanabeazana ho Olom-pirenena (EAC) - Kilasy Seconde.\nNovolavolaina mifanaraka amin'ny tari-dàlana nasionaly sy iraisam-pirenena momba ny fampianarana."
                else -> key
            }
        }
    }
}

