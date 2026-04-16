package org.example

import java.io.File
import java.io.FileNotFoundException

const val MIN_CORRECT_ANSWERS_COUNT = 3
const val ANSWERS_VARIANTS_COUNT = 4
const val FILE_NAME = "words.txt"
val wordsFile = File(FILE_NAME)

data class Statistics(
    val totalCount: Int,
    val learnedCount: Int,
    val percent: Int,
)

data class Question(
    val variants: List<Word>,
    val correctAnswerId: Int,
    val variantsRange: IntRange,
)

class LearnWordsTrainer {
    private var question: Question? = null
    private val dictionary = loadDictionary()

    fun getStatistics(): Statistics {
        val totalCount = dictionary.size
        val learnedCount =
            if (totalCount != 0) dictionary.filter { it.correctAnswersCount >= MIN_CORRECT_ANSWERS_COUNT }.size
            else 0
        val percent =
            if (totalCount != 0) learnedCount * 100 / totalCount
            else 0
        return Statistics(totalCount, learnedCount, percent)
    }

    fun getNextQuestion(): Question? {
        val notLearnedList =
            dictionary.filter { it.correctAnswersCount < MIN_CORRECT_ANSWERS_COUNT }
        if (notLearnedList.isEmpty()) return null
        val questionWords = notLearnedList.shuffled().take(ANSWERS_VARIANTS_COUNT)
        val answersVariantsRange = 1..questionWords.size
        val correctAnswerId = answersVariantsRange.random() - 1
        question = Question(
            variants = questionWords,
            correctAnswerId = correctAnswerId,
            variantsRange = answersVariantsRange,
        )
        return question
    }

    fun checkAnswer(userAnswerIndex: Int?): Boolean {
        return question?.let {
            if (userAnswerIndex == it.correctAnswerId) {
                it.variants[it.correctAnswerId].correctAnswersCount++
                saveDictionary(dictionary)
                true
            } else
                false
        } ?: false
    }

    private fun loadDictionary(): MutableList<Word> {
        var splitLine: List<String>
        val dictionary = mutableListOf<Word>()

        try {
            wordsFile.readLines().forEach { line ->
                splitLine = line.split("|")
                dictionary.add(
                    Word(
                        original = splitLine[0],
                        translate = splitLine[1],
                        correctAnswersCount = (splitLine.getOrNull(2))?.toIntOrNull() ?: 0,
                    )
                )
            }
        } catch (e: FileNotFoundException) {
            println("Файл \"$FILE_NAME\" не найден")
        }

        return dictionary
    }

    private fun saveDictionary(dictionary: MutableList<Word>) {
        wordsFile.writeText("")
        dictionary.forEach { word ->
            wordsFile.appendText("${word.original}|${word.translate}|${word.correctAnswersCount}\n")
        }
    }
}
