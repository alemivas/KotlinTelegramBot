package org.example

import java.io.File

fun main() {
    val wordsFile = File("words.txt")
    wordsFile.readLines().forEach { line ->
        println(line)
    }
}
