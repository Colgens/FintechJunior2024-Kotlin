import kotlinx.coroutines.runBlocking
import java.time.LocalDate

fun main() = runBlocking {
    val count = 500
    val newsList = getNews(count)

    val period = LocalDate.now().minusMonths(1)..LocalDate.now()
    val mostRatedNews = newsList.getMostRatedNews(10, period)

    saveNews("news.csv", mostRatedNews)
    newsPrinter(mostRatedNews) {
        saveToFile("news2.csv", mostRatedNews)
    }
}