package org.example

import java.io.File
import java.io.FileNotFoundException

fun main() {
    val fileName = "words.txt"
    val wordsFile = File(fileName)
    try {
        wordsFile.readLines().forEach { line ->
            println(line)
        }
    } catch (e: FileNotFoundException) {
        println("Файл \"$fileName\" не найден")
    }
}
