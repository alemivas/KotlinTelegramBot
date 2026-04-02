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
            "1" -> println("Выбран пункт \"Учить слова\"")
            "2" -> println("Выбран пункт \"Статистика\"")
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

data class Word(
    val original: String,
    val translate: String,
    val correctAnswersCount: Int,
)
