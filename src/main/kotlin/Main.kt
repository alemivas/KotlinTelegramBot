package org.example

data class Word(
    val original: String,
    val translate: String,
    var correctAnswersCount: Int,
)

fun Question.asConsoleString(): String {
    val answerVariants = variants
        .mapIndexed { index: Int, word: Word -> "${index + 1} - ${word.translate}" }
        .joinToString("\n")
    return variants[correctAnswerId].original + ":\n" +
            answerVariants + "\n" +
            "----------\n" +
            "0 - Меню"
}

fun main() {
    println("=== Тренажер английских слов ===")
    val trainer = try {
        LearnWordsTrainer(
            minCorrectAnswersCount = 3,
            answersVariantsCount = 4,
        )
    } catch (e: Exception) {
        println()
        println("Невозможно загрузить словарь")
        return
    }

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
                    if (question != null) {
                        println()
                        println(question.asConsoleString())
                        when (val userAnswerInput = readln().toIntOrNull()) {
                            0 -> break

                            in question.variantsRange -> {
                                if (trainer.checkAnswer(userAnswerInput?.minus(1))) {
                                    println("Правильно!")
                                } else {
                                    println(
                                        "Неправильно! ${question.variants[question.correctAnswerId].original} – " +
                                                "это ${question.variants[question.correctAnswerId].translate}"
                                    )
                                }
                            }

                            else -> println("Введите число ${question.variantsRange.toList().joinToString()} или 0")
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
