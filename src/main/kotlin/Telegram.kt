import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

fun main(args: Array<String>) {
    val botToken = args[0]
    val baseURL = "https://api.telegram.org/bot$botToken/"
    val urlGetMe = "${baseURL}getMe"
    val urlGetUpdates = "${baseURL}getUpdates"

    val client: HttpClient = HttpClient.newBuilder().build()
    val getMeRequest: HttpRequest = HttpRequest.newBuilder().uri(URI.create(urlGetMe)).build()
    val getUpdatesRequest: HttpRequest = HttpRequest.newBuilder().uri(URI.create(urlGetUpdates)).build()
    val getMeResponse: HttpResponse<String> =
        client.send(getMeRequest, HttpResponse.BodyHandlers.ofString())
    val getUpdatesResponse: HttpResponse<String> =
        client.send(getUpdatesRequest, HttpResponse.BodyHandlers.ofString())

    println(getMeResponse.body())
    println()
    println(getUpdatesResponse.body())
}
