import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class Update(
    @SerialName("update_id")
    val updateId: Long,
    @SerialName("message")
    val message: Message? = null,
    @SerialName("callback_query")
    val callbackQuery: CallbackQuery? = null,
)

@Serializable
data class Response(
    @SerialName("result")
    val result: List<Update>
)

@Serializable
data class Message(
    @SerialName("text")
    val text: String
)

@Serializable
data class CallbackQuery(
    @SerialName("data")
    val data: String
)

fun main() {

//    val myWord = Word(
//        original = "Hello0",
//        translate = "Привет0",
//        correctAnswersCount = 0,
//    )
//    println(myWord)

    val json = Json {
        ignoreUnknownKeys = true
    }

    val responseString = """
        {
          "ok": true,
          "result": [
            {
              "update_id": 450306169,
              "message": {
                "message_id": 426,
                "from": {
                  "id": 1217159186,
                  "is_bot": false,
                  "first_name": "Александр",
                  "username": "al35353535",
                  "language_code": "ru"
                },
                "chat": {
                  "id": 1217159186,
                  "first_name": "Александр",
                  "username": "al35353535",
                  "type": "private"
                },
                "date": 1778725961,
                "text": "/start",
                "entities": [
                  {
                    "offset": 0,
                    "length": 6,
                    "type": "bot_command"
                  }
                ]
              }
            },
            {
              "update_id": 450306205,
              "callback_query": {
                "id": "5227658898154941068",
                "from": {
                  "id": 1217159186,
                  "is_bot": false,
                  "first_name": "Александр",
                  "username": "al35353535",
                  "language_code": "ru"
                },
                "message": {
                  "message_id": 487,
                  "from": {
                    "id": 8473510194,
                    "is_bot": true,
                    "first_name": "English Words Learning Bot",
                    "username": "EnglishWordsAlexanderBot"
                  },
                  "chat": {
                    "id": 1217159186,
                    "first_name": "Александр",
                    "username": "al35353535",
                    "type": "private"
                  },
                  "date": 1778895454,
                  "text": "Основное меню",
                  "reply_markup": {
                    "inline_keyboard": [
                      [
                        {
                          "text": "Изучить слова",
                          "callback_data": "learn_words_clicked"
                        },
                        {
                          "text": "Статистика",
                          "callback_data": "statistics_clicked"
                        }
                      ]
                    ]
                  }
                },
                "chat_instance": "1444495561775812572",
                "data": "statistics_clicked"
              }
            }
          ]
        }
    """.trimIndent()

//    val word = Json.encodeToString(
//        Word(
//            original = "Hello",
//            translate = "Привет",
//            correctAnswersCount = 0,    //если отличается от по умолчанию, то выводит
//        )
//    )
//
//    println()
//    println(word)
//
//    val wordObject = Json.decodeFromString<Word>(
//        """{"original":"Hello","translate":"Привет","correctAnswersCount":0}"""
//    )
//
//    println()
//    println(wordObject)

    val response = json.decodeFromString<Response>(responseString)
    println(response)

}
