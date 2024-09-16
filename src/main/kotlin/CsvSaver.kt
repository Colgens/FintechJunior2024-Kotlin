import kotlin.io.path.Path
import kotlin.io.path.exists
import kotlin.io.path.writeText
import org.slf4j.LoggerFactory
import java.io.IOException

fun saveNews(path: String, news: Collection<News>) {
    val logger = LoggerFactory.getLogger("CsvSaver")
    val filePath = Path(path)

    try {
        require(!filePath.exists()) { "Файл уже существует по пути $path" }

        val csvLines = buildList {
            add("id,title,place,description,siteUrl,favoritesCount,commentsCount,publicationDate,rating")
            news.forEach {
                add("${it.id},${it.title},${it.place?.title ?: "Неизвестно"},${it.description},${it.siteUrl},${it.favoritesCount},${it.commentsCount},${it.formattedDate},${it.rating}")
            }
        }

        filePath.writeText(csvLines.joinToString("\n"))
        logger.info("Сохранено ${news.size} новостей в $path")
    } catch (e: IOException) {
        logger.error("Не удалось сохранить новости в CSV", e)
    } catch (e: Exception) {
        logger.error("Произошла непредвиденная ошибка", e)
    }
}