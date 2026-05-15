import kotlinx.serialization.json.Json
import org.example.Word

fun main() {

    val myWord = Word(
        original = "Hello0",
        translate = "Привет0",
        correctAnswersCount = 0,
    )
    println(myWord)

    val word = Json.encodeToString(
        Word(
            original = "Hello",
            translate = "Привет",
            correctAnswersCount = 0,    //если отличается от по умолчанию, то выводит
        )
    )

    println()
    println(word)

    val wordObject = Json.decodeFromString<Word>(
//        """{"original":"Hello","translate":"Привет","correctAnswersCount":0}"""
        """{"ok":true,"result":[{"update_id":450306169,
"message":{"message_id":426,"from":{"id":1217159186,"is_bot":false,"first_name":"\u0410\u043b\u0435\u043a\u0441\u0430\u043d\u0434\u0440","username":"al35353535","language_code":"ru"},"chat":{"id":1217159186,"first_name":"\u0410\u043b\u0435\u043a\u0441\u0430\u043d\u0434\u0440","username":"al35353535","type":"private"},"date":1778725961,"text":"/start","entities":[{"offset":0,"length":6,"type":"bot_command"}]}}]}"""
    )

    println()
    println(wordObject)
}
