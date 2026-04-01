package org.example

import java.io.File
import java.io.FileNotFoundException

fun main() {
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

    dictionary.forEach { word ->
        println(word)
    }
}

data class Word(
    val original: String,
    val translate: String,
    val correctAnswersCount: Int,
)
