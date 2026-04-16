package org.example

import java.io.FileNotFoundException

data class Statistics(
    val totalCount: Int,
    val learnedCount: Int,
    val percent: Int,
)

data class Question(
    val variants: List<Word>,
    val correctAnswerId: Int,
)

class LearnWordsTrainer {
    private var question: Question? = null
    val dictionary = loadDictionary()

    fun loadDictionary(): MutableList<Word> {
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

    fun saveDictionary(dictionary: MutableList<Word>) {
        wordsFile.writeText("")
        dictionary.forEach { word ->
            wordsFile.appendText("${word.original}|${word.translate}|${word.correctAnswersCount}\n")
        }
    }

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
//            trainer.dictionary.filter { it.correctAnswersCount < MIN_CORRECT_ANSWERS_COUNT }
            dictionary.filter { it.correctAnswersCount < MIN_CORRECT_ANSWERS_COUNT }
        if (notLearnedList.isEmpty()) return null

        val questionWords = notLearnedList.shuffled().take(ANSWERS_VARIANTS_COUNT)
//        val answersVariantsRange = 1..question.variants.size
        val answersVariantsRange = 1..questionWords.size
        val correctAnswerId = answersVariantsRange.random() - 1

        question = Question(
//            variants = notLearnedList,
            variants = questionWords,
            correctAnswerId = correctAnswerId,
        )
        return question
    }

    fun checkAnswer(userAnswerIndex: Int?): Boolean {
        return question?.let {
//            if (question == null) return false
            if (userAnswerIndex == it.correctAnswerId) {
                it.variants[it.correctAnswerId].correctAnswersCount++
                saveDictionary(dictionary)
                true
            } else
                false
        } ?: false

    }
}
