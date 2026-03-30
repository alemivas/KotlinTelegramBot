package org.example

import java.io.File

fun main() {
    val wordsFile = File("words.txt")
//    wordsFile.readLines().forEach { line ->
//        println(line)
//    }


    val dictionary = mutableListOf<Word>()

    var lineList = mutableListOf<String>()

    for (line in wordsFile.readLines()) {

        lineList = line.split("|") as MutableList<String>
//        lineList[2] = lineList[2] ?: "0"
        val lineList2 = lineList[2] ?: "0"
        dictionary.add(
            Word(
                text = lineList[0],
                translate = lineList[1],
                correctAnswersCount = (lineList2).toInt(),
            )
        )
//        println()
    }
    dictionary.forEach {
        println(it)
    }
}

data class Word(
    val text: String,
    val translate: String,
    val correctAnswersCount: Int = 0,
)
