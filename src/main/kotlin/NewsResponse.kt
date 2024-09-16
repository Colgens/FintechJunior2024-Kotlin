import kotlinx.serialization.Serializable

@Serializable
data class NewsResponse(
    val results: List<News>
)