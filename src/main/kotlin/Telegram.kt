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
    } catch (e: Exception) {
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
        if (data.equals("learn_words_clicked", ignoreCase = true) && chatId != null)
            println(tgBotService.sendMessage(chatId, "learn_words_clicked"))
        if (data.equals("statistics_clicked", ignoreCase = true) && chatId != null)
            println(tgBotService.sendMessage(chatId, "Выучено 10 из 10 слов | 100%"))
    }
}
