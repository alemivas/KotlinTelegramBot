package org.example

import java.io.File

fun main() {
    println("=== Тренажер английских слов ===")

    val trainer = LearnWordsTrainer()

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
                    val notLearnedList =
                        trainer.dictionary.filter { it.correctAnswersCount < MIN_CORRECT_ANSWERS_COUNT }
                    if (notLearnedList.isNotEmpty()) {
                        val questionWords = notLearnedList.shuffled().take(ANSWERS_VARIANTS_COUNT)
                        val answersVariantsRange = 1..questionWords.size
                        val correctAnswerId = answersVariantsRange.random() - 1
                        println()
                        println("${questionWords[correctAnswerId].original}:")
                        questionWords.forEachIndexed { index, word ->
                            println("${index + 1} - ${word.translate}")
                        }
                        println("----------")
                        println("0 - Меню")
                        val userAnswerInput = readln().toIntOrNull()
                        when (userAnswerInput) {
                            0 -> break

                            correctAnswerId + 1 -> {
                                println("Правильно!")
//                                dictionary[dictionary.indexOf(questionWords[correctAnswerId])].correctAnswersCount++
                                questionWords[correctAnswerId].correctAnswersCount++
                                trainer.saveDictionary(trainer.dictionary)
                            }

                            in answersVariantsRange -> println(
                                "Неправильно! ${questionWords[correctAnswerId].original} – " +
                                        "это ${questionWords[correctAnswerId].translate}"
                            )

                            else -> println("Введите число ${answersVariantsRange.toList().joinToString()} или 0")
                        }
                    } else {
                        println()
                        println("Все слова в словаре выучены")
                        break
                    }
                }
            }

            "2" -> {
                println("Выбран пункт \"Статистика\"")

                val statistics = trainer.getStatistics()

//                val totalCount = trainer.dictionary.size
//                if (totalCount != 0) {
                if (statistics.totalCount != 0) {
//                    val learnedCount = trainer.dictionary.filter { it.correctAnswersCount >= MIN_CORRECT_ANSWERS_COUNT }.size
//                    val percent = learnedCount * 100 / totalCount
                    println("Выучено ${statistics.learnedCount} из ${statistics.totalCount} слов | ${statistics.percent}%")
                } else println("Словарь пустой")
            }

            "0" -> return

            else -> println("Введите число 1, 2 или 0")
        }
    }
}

data class Word(
    val original: String,
    val translate: String,
    var correctAnswersCount: Int,
)

const val MIN_CORRECT_ANSWERS_COUNT = 3
const val ANSWERS_VARIANTS_COUNT = 4
const val FILE_NAME = "words.txt"
val wordsFile = File(FILE_NAME)
