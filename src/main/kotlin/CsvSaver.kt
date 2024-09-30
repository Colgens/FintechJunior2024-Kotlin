import com.opencsv.CSVWriter
import kotlin.io.path.Path
import kotlin.io.path.exists
import org.slf4j.LoggerFactory
import java.io.IOException
import java.nio.file.Files

fun saveNews(path: String, news: Collection<News>) {
    val logger = LoggerFactory.getLogger("CsvSaver")
    val filePath = Path(path)

    try {
        require(!filePath.exists()) { "Файл уже существует по пути $path" }

        Files.newOutputStream(filePath).use { outputStream ->
            CSVWriter(outputStream.writer()).use { csvWriter ->
                val header = arrayOf("id", "title", "place", "description", "siteUrl", "favoritesCount", "commentsCount", "publicationDate", "rating")
                csvWriter.writeNext(header)

                news.forEach { newsItem ->
                    val row = arrayOf(
                        newsItem.id.toString(),
                        newsItem.title,
                        newsItem.place?.title ?: "Неизвестно",
                        newsItem.description,
                        newsItem.siteUrl,
                        newsItem.favoritesCount.toString(),
                        newsItem.commentsCount.toString(),
                        newsItem.formattedDate,
                        newsItem.rating.toString()
                    )
                    csvWriter.writeNext(row)
                }
            }
        }

        logger.info("Сохранено ${news.size} новостей в $path")
    } catch (e: IOException) {
        logger.error("Не удалось сохранить новости в CSV", e)
    } catch (e: Exception) {
        logger.error("Произошла непредвиденная ошибка", e)
    }
}