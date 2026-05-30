package com.example

import android.content.Context
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class DataRepository(private val context: Context) {
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    fun loadLessons(lang: AppLanguage): List<Lesson> {
        return try {
            val json = context.assets.open("data/${lang.code}/lecons.json").bufferedReader().use { it.readText() }
            val type = Types.newParameterizedType(List::class.java, Lesson::class.java)
            val adapter = moshi.adapter<List<Lesson>>(type)
            adapter.fromJson(json) ?: emptyList()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    fun loadQuiz(lang: AppLanguage): List<QuizQuestion> {
        return try {
            val json = context.assets.open("data/${lang.code}/quiz.json").bufferedReader().use { it.readText() }
            val type = Types.newParameterizedType(List::class.java, QuizQuestion::class.java)
            val adapter = moshi.adapter<List<QuizQuestion>>(type)
            adapter.fromJson(json) ?: emptyList()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    fun loadFlashcards(lang: AppLanguage): List<Flashcard> {
        return try {
            val json = context.assets.open("data/${lang.code}/flashcards.json").bufferedReader().use { it.readText() }
            val type = Types.newParameterizedType(List::class.java, Flashcard::class.java)
            val adapter = moshi.adapter<List<Flashcard>>(type)
            adapter.fromJson(json) ?: emptyList()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}
