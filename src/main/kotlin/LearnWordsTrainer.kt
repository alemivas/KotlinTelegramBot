package org.example

import java.io.File

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

class LearnWordsTrainer(
    private val minCorrectAnswersCount: Int = 3,
    private val answersVariantsCount: Int = 4
) {
    private val wordsFile = File("words.txt")
    private var question: Question? = null
    private val dictionary = loadDictionary()

    fun getStatistics(): Statistics {
        val totalCount = dictionary.size
        val learnedCount =
            if (totalCount != 0) dictionary.filter { it.correctAnswersCount >= minCorrectAnswersCount }.size
            else 0
        val percent =
            if (totalCount != 0) learnedCount * 100 / totalCount
            else 0
        return Statistics(totalCount, learnedCount, percent)
    }

    fun getNextQuestion(): Question? {
        val notLearnedList =
            dictionary.filter { it.correctAnswersCount < minCorrectAnswersCount }
        if (notLearnedList.isEmpty()) return null
        val questionWords =
            if (notLearnedList.size > answersVariantsCount) {
                notLearnedList.shuffled().take(answersVariantsCount)
            } else {
                val learnedList =
                    dictionary.filter { it.correctAnswersCount >= minCorrectAnswersCount }
                val addedLearnedList = learnedList.shuffled().take(answersVariantsCount - notLearnedList.size)
                (notLearnedList + addedLearnedList).shuffled().take(answersVariantsCount)
            }
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
        return dictionary
    }

    private fun saveDictionary(dictionary: MutableList<Word>) {
        wordsFile.writeText("")
        dictionary.forEach { word ->
            wordsFile.appendText("${word.original}|${word.translate}|${word.correctAnswersCount}\n")
        }
    }
}
