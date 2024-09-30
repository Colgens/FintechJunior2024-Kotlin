import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory
import java.io.IOException

suspend fun getNews(count: Int = 100): List<News> {
    val logger = LoggerFactory.getLogger("ApiClient")

    return HttpClient(CIO).use { client ->
        try {
            val response: HttpResponse = client.get("https://kudago.com/public-api/v1.4/news/") {
                parameter(
                    "fields",
                    "id,title,place,description,site_url,favorites_count,comments_count,publication_date"
                )
                parameter("location", "spb")
                parameter("text_format", "text")
                parameter("page_size", count)
                parameter("order_by", "-publication_date")
                parameter("expand", "place")
            }
            val responseBody = response.bodyAsText()
            logger.info("Тело ответа: $responseBody")
            val json = Json { ignoreUnknownKeys = true; coerceInputValues = true }
            val newsResponse: NewsResponse = json.decodeFromString(responseBody)
            val newsList = newsResponse.results
            logger.info("Получено ${newsList.size} новостей")
            newsList
        } catch (e: IOException) {
            logger.error("Не удалось получить новости", e)
            emptyList()
        } catch (e: Exception) {
            logger.error("Произошла непредвиденная ошибка", e)
            emptyList()
        }
    }
}