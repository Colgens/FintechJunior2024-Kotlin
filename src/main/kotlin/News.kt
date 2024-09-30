import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.math.exp

@Serializable
data class News(
    val id: Int,
    val title: String,
    val place: Place?,
    val description: String,
    @SerialName("site_url") val siteUrl: String,
    @SerialName("favorites_count") val favoritesCount: Int,
    @SerialName("comments_count") val commentsCount: Int,
    @SerialName("publication_date") val publicationDate: Long
) {
    val rating: Double by lazy {
        1 / (1 + exp(-(favoritesCount.toDouble() / (commentsCount + 1))))
    }

    val formattedDate: String
        get() {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
            return Instant.ofEpochSecond(publicationDate)
                .atZone(ZoneId.systemDefault())
                .format(formatter)
        }
}

@Serializable
data class Place(
    val id: Int? = null,
    val title: String? = null,
    val slug: String? = null,
    val address: String? = null,
    @SerialName("site_url") val siteUrl: String? = null,
    @SerialName("is_closed") val isClosed: Boolean? = null,
    val location: String? = null
)