package org.example

import java.io.File

data class Word(
    val original: String,
    val translate: String,
    var correctAnswersCount: Int,
)

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
                    val question = trainer.getNextQuestion()


//                    val notLearnedList =
//                        trainer.dictionary.filter { it.correctAnswersCount < MIN_CORRECT_ANSWERS_COUNT }
//                    if (notLearnedList.isNotEmpty()) {
                    if (question != null) {
//                        val questionWords = notLearnedList.shuffled().take(ANSWERS_VARIANTS_COUNT)
//                        val answersVariantsRange = 1..questionWords.size
                        val answersVariantsRange = 1..question.variants.size
//                        val correctAnswerId = answersVariantsRange.random() - 1
                        println()
//                        println("${questionWords[correctAnswerId].original}:")
                        println("${question.variants[question.correctAnswerId].original}:")
                        question.variants.forEachIndexed { index, word ->
                            println("${index + 1} - ${word.translate}")
                        }
                        println("----------")
                        println("0 - Меню")
                        when (val userAnswerInput = readln().toIntOrNull()) {
//                            0 -> break
//
//                            question.correctAnswerId + 1 -> {
//                                println("Правильно!")
//                                question.variants[question.correctAnswerId].correctAnswersCount++
//                                trainer.saveDictionary(trainer.dictionary)
//                            }
//
//                            in answersVariantsRange -> println(
//                                "Неправильно! ${question.variants[question.correctAnswerId].original} – " +
//                                        "это ${question.variants[question.correctAnswerId].translate}"
//                            )
//
//                            else -> println("Введите число ${answersVariantsRange.toList().joinToString()} или 0")
                            0 -> break

                            in answersVariantsRange -> {
                                if (trainer.checkAnswer(userAnswerInput?.minus(1))) {
                                    println("Правильно!")
                                } else {
                                    println(
                                        "Неправильно! ${question.variants[question.correctAnswerId].original} – " +
                                                "это ${question.variants[question.correctAnswerId].translate}"
                                    )
                                }

                            }

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
                if (statistics.totalCount != 0) {
                    println(
                        "Выучено ${statistics.learnedCount} из ${statistics.totalCount} слов | " +
                                "${statistics.percent}%"
                    )
                } else println("Словарь пустой")
            }

            "0" -> return

            else -> println("Введите число 1, 2 или 0")
        }
    }
}

const val MIN_CORRECT_ANSWERS_COUNT = 3
const val ANSWERS_VARIANTS_COUNT = 4
const val FILE_NAME = "words.txt"
val wordsFile = File(FILE_NAME)
