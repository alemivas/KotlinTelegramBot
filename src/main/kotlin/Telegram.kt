import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

fun main(args: Array<String>) {
    val botToken = args[0]
    var updateId = 0
    val updateIdRegex: Regex = "\"update_id\":([0-9]+?),\\s\"message\"".toRegex()
    val messageTextRegex: Regex = "\"text\":\"(.+?)\"".toRegex()

    while (true) {
        Thread.sleep(2000)
        val updates: String = getUpdates(botToken, updateId)
        println(updates)

        val updateIdMatchResult: MatchResult? = updateIdRegex.findAll(updates).lastOrNull()
        val updateIdGroups = updateIdMatchResult?.groups
        val updateIdString = updateIdGroups?.get(1)?.value ?: continue
        updateId = updateIdString.toInt() + 1

        val messageMatchResult: MatchResult? = messageTextRegex.find(updates)
        val messageGroups = messageMatchResult?.groups
        val text = messageGroups?.get(1)?.value
        println(text)
    }
}

fun getUpdates(botToken: String, updateId: Int): String {
    val baseURL = "https://api.telegram.org/bot$botToken/"
    val urlGetUpdates = "${baseURL}getUpdates?offset=$updateId"
    val client: HttpClient = HttpClient.newBuilder().build()
    val request: HttpRequest = HttpRequest.newBuilder().uri(URI.create(urlGetUpdates)).build()
    val response: HttpResponse<String> =
        client.send(request, HttpResponse.BodyHandlers.ofString())
    return response.body()
}
