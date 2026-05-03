fun main(args: Array<String>) {
    val botToken = args[0]
    var updateId = 0
    val updateIdRegex: Regex = "\"update_id\":([0-9]+?),\\s\"message\"".toRegex()
    val messageTextRegex: Regex = "\"text\":\"(.+?)\"".toRegex()
    val chatIdRegex: Regex = "\"chat\":\\{\"id\":([0-9]+?),\"first_name\"".toRegex()
    val tgBotService = TelegramBotService(botToken)
    val helloText = "Hello"

    while (true) {
        Thread.sleep(2000)
        val updates: String = tgBotService.getUpdates(updateId)
        println(updates)

        val updateIdMatchResult: MatchResult? = updateIdRegex.findAll(updates).lastOrNull()
        val updateIdGroups = updateIdMatchResult?.groups
        val updateIdString = updateIdGroups?.get(1)?.value ?: continue
        updateId = updateIdString.toInt() + 1

        val messageMatchResult: MatchResult? = messageTextRegex.find(updates)
        val messageGroups = messageMatchResult?.groups
        val text = messageGroups?.get(1)?.value
        println(text)

        val chatIdMatchResult: MatchResult? = chatIdRegex.find(updates)
        val chatIdGroups = chatIdMatchResult?.groups
        val chatId = chatIdGroups?.get(1)?.value?.toIntOrNull()
        if (text.equals(helloText, ignoreCase = true) && chatId != null)
            println(tgBotService.sendMessage(chatId, helloText))
    }
}
