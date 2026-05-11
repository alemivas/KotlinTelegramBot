import org.example.LearnWordsTrainer

fun main(args: Array<String>) {
    val botToken = args[0]
    var updateId = 0
    val updateIdRegex: Regex = "\"update_id\":([0-9]+?),".toRegex()
    val messageTextRegex: Regex = "\"text\":\"(.+?)\"".toRegex()
    val chatIdRegex: Regex = "\"chat\":\\{\"id\":([0-9]+?),".toRegex()
    val dataRegex: Regex = "\"data\":\"(.+?)\"".toRegex()
    val tgBotService = TelegramBotService(botToken)
    val helloText = "Hello"
    val trainer = try {
        LearnWordsTrainer(minCorrectAnswersCount = 3, answersVariantsCount = 4)
    } catch (_: Exception) {
        println()
        println("Невозможно загрузить словарь")
        return
    }

    while (true) {
        Thread.sleep(2000)
        val updates: String = tgBotService.getUpdates(updateId)
        println(updates)

        updateId = updateIdRegex.findAll(updates)
            .lastOrNull()?.groups?.get(1)?.value?.toIntOrNull()?.plus(1) ?: continue
        val text = messageTextRegex.find(updates)?.groups?.get(1)?.value
        val chatId = chatIdRegex.find(updates)?.groups?.get(1)?.value?.toIntOrNull()
        val data = dataRegex.find(updates)?.groups?.get(1)?.value

        if (text.equals(helloText, ignoreCase = true) && chatId != null)
            println(tgBotService.sendMessage(chatId, helloText))
        if (text.equals("/start", ignoreCase = true) && chatId != null)
            println(tgBotService.sendMenu(chatId))
        if (data == TelegramBotService.LEARN_WORDS_CLICKED && chatId != null) {
            checkNextQuestionAndSend(trainer, tgBotService, chatId)
        }
        if (data == TelegramBotService.STATISTICS_CLICKED && chatId != null) {
            val statistics = trainer.getStatistics()
            val message = if (statistics.totalCount != 0)
                "Выучено ${statistics.learnedCount} из ${statistics.totalCount} слов | ${statistics.percent}%"
            else
                "Словарь пустой"
            println(tgBotService.sendMessage(chatId, message))
        }
    }
}

fun checkNextQuestionAndSend(
    trainer: LearnWordsTrainer,
    telegramBotService: TelegramBotService,
    chatId: Int
) {
    val question = trainer.getNextQuestion()
    if (question != null) {
        println(telegramBotService.sendQuestion(chatId, question))
    } else {
        println(telegramBotService.sendMessage(chatId, "Все слова в словаре выучены"))
    }
}
