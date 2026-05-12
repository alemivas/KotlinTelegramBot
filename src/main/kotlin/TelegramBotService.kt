import org.example.Question
import org.example.Word
import java.net.URI
import java.net.URLEncoder
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class TelegramBotService(
    botToken: String
) {
    companion object {
        const val LEARN_WORDS_CLICKED = "learn_words_clicked"
        const val STATISTICS_CLICKED = "statistics_clicked"
        const val CALLBACK_DATA_ANSWER_PREFIX = "answer_"
    }

    private val baseURL = "https://api.telegram.org/bot$botToken/"
    private val urlSendMessage = "${baseURL}sendMessage"
    private val client: HttpClient = HttpClient.newBuilder().build()

    fun getUpdates(updateId: Int): String {
        val urlGetUpdates = "${baseURL}getUpdates?offset=$updateId"
        val request: HttpRequest = HttpRequest.newBuilder().uri(URI.create(urlGetUpdates)).build()
        val response: HttpResponse<String> =
            client.send(request, HttpResponse.BodyHandlers.ofString())
        return response.body()
    }

    fun sendMessage(chatId: Int, message: String): String {
        val encodedMessage = URLEncoder.encode(message, "UTF-8")
        val urlParamsSendMessage = "$urlSendMessage?chat_id=$chatId&text=$encodedMessage"
        val request: HttpRequest = HttpRequest.newBuilder().uri(URI.create(urlParamsSendMessage)).build()
        val response: HttpResponse<String> =
            client.send(request, HttpResponse.BodyHandlers.ofString())
        return response.body()
    }

    fun sendMenu(chatId: Int): String {
        val sendMenuBody = """
            {
                "chat_id": $chatId,
                "text": "Основное меню",
                "reply_markup": {
                    "inline_keyboard": [
                        [
                            {
                                "text": "Изучить слова",
                                "callback_data": "$LEARN_WORDS_CLICKED"
                            },
                            {
                                "text": "Статистика",
                                "callback_data": "$STATISTICS_CLICKED"
                            }
                        ]
                    ]
                }
            }
        """.trimIndent()
        val request: HttpRequest = HttpRequest.newBuilder().uri(URI.create(urlSendMessage))
            .header("Content-type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(sendMenuBody))
            .build()
        val response: HttpResponse<String> =
            client.send(request, HttpResponse.BodyHandlers.ofString())
        return response.body()
    }

    fun sendQuestion(chatId: Int, question: Question): String {
        val answerVariants = question.variants
            .mapIndexed { index: Int, word: Word ->
                "[{\"text\": \"${word.translate}\"," +
                  "\"callback_data\": \"${CALLBACK_DATA_ANSWER_PREFIX + index}\"}]"
            }
            .joinToString(",")
        val sendQuestionBody = """
            {
                "chat_id": $chatId,
                "text": "${question.variants[question.correctAnswerId].original}",
                "reply_markup": {
                    "inline_keyboard": [
                        $answerVariants
                    ]
                }
            }
        """.trimIndent()
        val request: HttpRequest = HttpRequest.newBuilder().uri(URI.create(urlSendMessage))
            .header("Content-type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(sendQuestionBody))
            .build()
        val response: HttpResponse<String> =
            client.send(request, HttpResponse.BodyHandlers.ofString())
        return response.body()
    }
}
