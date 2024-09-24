interface Element {
    fun render(builder: StringBuilder)
}

class TextElement(private val text: String) : Element {
    override fun render(builder: StringBuilder) {
        builder.append(text).append("  \n") // Добавляем два пробела и перенос строки
    }
}

@DslMarker
annotation class MarkdownTagMarker

@MarkdownTagMarker
abstract class MarkdownTag(val name: String) : Element {
    val children = arrayListOf<Element>()

    protected fun <T : Element> initTag(tag: T, init: T.() -> Unit): T {
        tag.init()
        children.add(tag)
        return tag
    }

    override fun render(builder: StringBuilder) {
        for (c in children) {
            c.render(builder)
        }
        builder.append("\n") // Добавляем перенос строки после каждого элемента
    }

    override fun toString(): String {
        val builder = StringBuilder()
        render(builder)
        return builder.toString()
    }
}

abstract class MarkdownTagWithText(name: String) : MarkdownTag(name) {
    operator fun String.unaryPlus() {
        children.add(TextElement(this))
    }
}

class Markdown : MarkdownTagWithText("markdown") {
    fun h1(init: H1.() -> Unit) = initTag(H1(), init)
    fun h2(init: H2.() -> Unit) = initTag(H2(), init)
    fun p(init: P.() -> Unit) = initTag(P(), init)
    fun link(text: String, url: String) {
        children.add(TextElement("[$text]($url)"))
    }

    fun divider() = initTag(Divider()) {}

    fun info(key: String, value: String) = initTag(Info(key, value)) {}
}

class H1 : MarkdownTagWithText("h1") {
    override fun render(builder: StringBuilder) {
        builder.append("# ")
        super.render(builder)
    }
}

class H2 : MarkdownTagWithText("h2") {
    override fun render(builder: StringBuilder) {
        builder.append("## ")
        super.render(builder)
    }
}

class P : MarkdownTagWithText("p")

class Divider : MarkdownTag("divider") {
    override fun render(builder: StringBuilder) {
        builder.append("---\n")
    }
}

class Info(private val key: String, private val value: String) : MarkdownTag("info") {
    override fun render(builder: StringBuilder) {
        builder.append("**$key:** $value  \n")
    }
}


fun markdown(init: Markdown.() -> Unit): Markdown {
    val markdown = Markdown()
    markdown.init()
    return markdown
}