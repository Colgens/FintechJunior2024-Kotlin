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
        val markdown = buildNewsMarkdown(news)
        logger.info("Печать новостей в консоль")
        println(markdown)
    }

    fun saveToFile(path: String, news: List<News>) {
        val markdown = buildNewsMarkdown(news)
        val filePath = Path(path)

        try {
            require(!filePath.exists()) { "Файл уже существует по пути $path" }

            filePath.writeText(markdown.toString())
            logger.info("Сохранено ${news.size} новостей в $path")
        } catch (e: IOException) {
            logger.error("Не удалось сохранить новости в файл", e)
        } catch (e: Exception) {
            logger.error("Произошла непредвиденная ошибка", e)
        }
    }

    private fun buildNewsMarkdown(news: List<News>): Markdown {
        return markdown {
            news.forEach { article ->
                h1 { +article.title }
                p { +article.description }
                link("Ссылка на новость", article.siteUrl)
                info("Место", article.place?.title ?: "Неизвестно")
                info("Лайки", article.favoritesCount.toString())
                info("Комментарии", article.commentsCount.toString())
                info("Дата публикации", article.formattedDate)
                info("Рейтинг", article.rating.toString())
                divider()
            }
        }
    }
}