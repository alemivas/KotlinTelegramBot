package org.example

import java.io.File

//const val minCorrectAnswersCount = 3
//const val answersVariantsCount = 4
//const val FILE_NAME = "words.txt"
//val wordsFile = File(FILE_NAME)

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

        val learnedList =
            dictionary.filter { it.correctAnswersCount >= minCorrectAnswersCount }

        val addedLearnedCount =
            if (notLearnedList.size > answersVariantsCount) 0
            else answersVariantsCount - notLearnedList.size

//        val addedLearnedList = learnedList.shuffled().take(answersVariantsCount - notLearnedList.size)
        val addedLearnedList = learnedList.shuffled().take(addedLearnedCount)
//        val addedLearnedList = learnedList.shuffled().take(-3)
//        val addedLearnedList = learnedList.shuffled().take(-3)

//        val questionWords = notLearnedList.shuffled().take(answersVariantsCount)
        val questionWords = (notLearnedList + addedLearnedList).shuffled().take(answersVariantsCount)


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

//        try {
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
//        } catch (e: FileNotFoundException) {
////            println("Файл \"$FILE_NAME\" не найден")
//            println("Файл не найден")
//        } /*catch (e: IndexOutOfBoundsException) {
//            println("Файл \"$FILE_NAME\" не найден")
//            throw IllegalStateException("некор файлЁ")
//        }

        return dictionary
    }

    private fun saveDictionary(dictionary: MutableList<Word>) {
        wordsFile.writeText("")
        dictionary.forEach { word ->
            wordsFile.appendText("${word.original}|${word.translate}|${word.correctAnswersCount}\n")
        }
    }
}
