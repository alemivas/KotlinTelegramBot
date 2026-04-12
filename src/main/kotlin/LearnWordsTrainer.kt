package org.example

import java.io.FileNotFoundException

data class Statistics(
    val totalCount: Int,
//    if (totalCount != 0) {
    val learnedCount: Int/* = 0*/,
    val percent: Int/* = 0*/,
)


class LearnWordsTrainer {

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
//        if (totalCount != 0) {
//            val learnedCount = dictionary.filter { it.correctAnswersCount >= MIN_CORRECT_ANSWERS_COUNT }.size
//            val percent = learnedCount * 100 / totalCount
//        } else {
//            val learnedCount = 0
//            val percent = 0
//        }
        val learnedCount =
            if (totalCount != 0) dictionary.filter { it.correctAnswersCount >= MIN_CORRECT_ANSWERS_COUNT }.size
            else 0
        val percent =
            if (totalCount != 0) learnedCount * 100 / totalCount
            else 0


//        val learnedCount = 0
        return Statistics(totalCount, learnedCount, percent,
//            totalCount = dictionary.size,
//            learnedCount = if (totalCount != 0) dictionary.filter { it.correctAnswersCount >= MIN_CORRECT_ANSWERS_COUNT }.size else 0,
//            percent = if (totalCount != 0) learnedCount * 100 / totalCount else 0,

//                if (totalCount != 0) {
//                    val learnedCount = dictionary.filter { it.correctAnswersCount >= MIN_CORRECT_ANSWERS_COUNT }.size
//                    val percent = learnedCount * 100 / totalCount
        )
    }
}
