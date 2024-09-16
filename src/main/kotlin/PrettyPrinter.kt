import org.slf4j.LoggerFactory
import java.io.IOException
import kotlin.io.path.Path
import kotlin.io.path.exists
import kotlin.io.path.writeText

fun newsPrinter(news: List<News>, configure: NewsPrinter.() -> Unit = {}) {
    NewsPrinter().apply(configure).print(news)
}

class NewsPrinter {
    private val logger = LoggerFactory.getLogger("NewsPrinter")

    fun print(news: List<News>) {
        val builder = buildNewsString(news)
        logger.info("Печать новостей в консоль")
        println(builder.toString())
    }

    fun saveToFile(path: String, news: List<News>) {
        val builder = buildNewsString(news)
        val filePath = Path(path)

        try {
            require(!filePath.exists()) { "Файл уже существует по пути $path" }

            filePath.writeText(builder.toString())
            logger.info("Сохранено ${news.size} новостей в $path")
        } catch (e: IOException) {
            logger.error("Не удалось сохранить новости в файл", e)
        } catch (e: Exception) {
            logger.error("Произошла непредвиденная ошибка", e)
        }
    }

    private fun buildNewsString(news: List<News>): StringBuilder {
        val builder = StringBuilder()
        news.forEach {
            newsItem(builder) {
                id = it.id
                title = it.title
                place = it.place?.title ?: "Неизвестно"
                description = it.description
                siteUrl = it.siteUrl
                favorites = it.favoritesCount
                comments = it.commentsCount
                publicationDate = it.formattedDate
                rating = it.rating
            }
        }
        return builder
    }

    private fun newsItem(builder: StringBuilder, init: NewsItemBuilder.() -> Unit) {
        val itemBuilder = NewsItemBuilder()
        itemBuilder.init()
        builder.append(itemBuilder.build())
    }

    class NewsItemBuilder {
        var id: Int = 0
        var title: String = ""
        var place: String = ""
        var description: String = ""
        var siteUrl: String = ""
        var favorites: Int = 0
        var comments: Int = 0
        var publicationDate: String = ""
        var rating: Double = 0.0

        fun build(): String {
            return """
                ID: $id
                Title: $title
                Place: $place
                Description: $description
                Site URL: $siteUrl
                Favorites: $favorites
                Comments: $comments
                Publication Date: $publicationDate
                Rating: $rating
                ----------------------------------------
            """.trimIndent() + "\n"
        }
    }
}