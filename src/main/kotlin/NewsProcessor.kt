import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import org.slf4j.LoggerFactory

fun List<News>.getMostRatedNews(count: Int, period: ClosedRange<LocalDate>): List<News> {
    val logger = LoggerFactory.getLogger("NewsProcessor")

    val filteredNews = this.filter { news ->
        val publicationDate = Instant.ofEpochSecond(news.publicationDate)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
        publicationDate in period
    }

    val sortedNews = filteredNews.sortedByDescending { it.rating }
    logger.info("Отфильтровано и отсортировано ${sortedNews.size} новостей")

    return sortedNews.take(count)
}