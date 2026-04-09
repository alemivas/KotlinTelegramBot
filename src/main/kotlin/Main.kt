package org.example

import java.io.File
import java.io.FileNotFoundException

fun main() {
    println("=== Тренажер английских слов ===")
    val dictionary = loadDictionary()

    while (true) {
        println()
        println("Меню:")
        println("1 – Учить слова")
        println("2 – Статистика")
        println("0 – Выход")
        when (readln()) {
            "1" -> {
                println("Выбран пункт \"Учить слова\"")
                while (true) {
                    val notLearnedList = dictionary.filter { it.correctAnswersCount < MIN_CORRECT_ANSWERS_COUNT }
                    if (notLearnedList.isNotEmpty()) {
                        val questionWords = notLearnedList.shuffled().take(ANSWERS_VARIANTS_COUNT)
//                        val correctAnswer = questionWords[0]
//                        val correctAnswerId = (0..<ANSWERS_VARIANTS_COUNT).random()
                        val correctAnswerId = ANSWERS_VARIANTS_RANGE.random() - 1
                        println()
//                        println("${correctAnswer.original}:")
                        println("${questionWords[correctAnswerId].original}:")
//                        questionWords.shuffled().forEachIndexed { index, word ->
//                            println("${index + 1} - ${word.translate}")
//                        }

                        questionWords.forEachIndexed { index, word ->
                            println("${index + 1} - ${word.translate}")
                        }
                        println("----------")
                        println("0 - Меню")
                        val userAnswerInput = readln()
                        when {
                            userAnswerInput == "0" -> break
//                            userAnswerInput.toInt() - 1 == correctAnswerId -> println("Правильно!")
//                            userAnswerInput.toIntOrNull()?.minus(1) == correctAnswerId -> println("Правильно!")
                            userAnswerInput == (correctAnswerId + 1).toString() -> {
                                println("Правильно!")
//                                questionWords[correctAnswerId].correctAnswersCount++
                                dictionary[dictionary.indexOf(questionWords[correctAnswerId])].correctAnswersCount++
                                saveDictionary(dictionary)
                            }
//                            userAnswerInput.toInt() in ANSWERS_VARIANTS_RANGE ->
                            userAnswerInput.toIntOrNull() in ANSWERS_VARIANTS_RANGE ->
//                            null in ANSWERS_VARIANTS_RANGE ->
                                println(
                                    "Неправильно! ${questionWords[correctAnswerId].original} – " + "это ${questionWords[correctAnswerId].translate}"
                                )
//                            else -> println("Введите число $ANSWERS_VARIANTS_RANGE или 0")
                            else -> println("Введите число ${ANSWERS_VARIANTS_RANGE.toList().joinToString()} или 0")
                        }
//                        if (userAnswerInput == "0") break
//                        if (userAnswerInput.toInt() == correctAnswerId) println("Правильно!")

                    } else {
                        println("Все слова в словаре выучены")
                        break
                    }
                }
            }

            "2" -> {
                println("Выбран пункт \"Статистика\"")
                val totalCount = dictionary.size
                if (totalCount != 0) {
                    val learnedCount = dictionary.filter { it.correctAnswersCount >= MIN_CORRECT_ANSWERS_COUNT }.size
                    val percent = learnedCount * 100 / totalCount
                    println("Выучено $learnedCount из $totalCount слов | $percent%")
                } else println("Словарь пустой")
            }

            "0" -> return
            else -> println("Введите число 1, 2 или 0")
        }
    }
}

fun loadDictionary(): MutableList<Word> {
    val fileName = "words.txt"
    val wordsFile = File(fileName)
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
        println("Файл \"$fileName\" не найден")
    }

    return dictionary
}

fun saveDictionary(dictionary: MutableList<Word>) {
    val fileName = "TEMPwords.txt"
    val wordsFile = File(fileName)
//    wordsFile.clear()
    wordsFile.writeText("")
    dictionary.forEach { word ->
        wordsFile.appendText("${word.original}|${word.translate}|${word.correctAnswersCount}\n")
    }
}

data class Word(
    val original: String,
    val translate: String,
    var correctAnswersCount: Int,
)

const val MIN_CORRECT_ANSWERS_COUNT = 3
const val ANSWERS_VARIANTS_COUNT = 4
val ANSWERS_VARIANTS_RANGE = 1..ANSWERS_VARIANTS_COUNT
