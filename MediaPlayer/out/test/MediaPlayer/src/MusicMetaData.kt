package src

import org.apache.tika.metadata.Metadata
import org.apache.tika.parser.ParseContext
import org.apache.tika.parser.Parser
import org.apache.tika.parser.mp3.Mp3Parser
import org.xml.sax.ContentHandler
import org.xml.sax.helpers.DefaultHandler
import java.io.File
import java.io.FileInputStream
import java.sql.Time


class Mp3Metadata(var Path : String) {
    public val Duration : String
    public val Genre : String?
    public val Creator : String?
    public val Album : String?
    public val ReleaseDate : String?
    public val Author : String?
    public val Artist : String?
    public val Title : String?

    init {
        var inputStream = FileInputStream(File(Path))
        val handler: ContentHandler = DefaultHandler()
        val metadata = Metadata()
        val parser: Parser = Mp3Parser()
        val parseCtx = ParseContext()
        parser.parse(inputStream, handler, metadata, parseCtx)
        inputStream.close()

        var time = Time(metadata.get("xmpDM:duration").toDouble().toLong())
        time.hours -= 10
        Duration = time.toString()

        Genre = metadata.get("xmpDM:genre")
        Creator = metadata.get("creator")
        Album = metadata.get("xmpDM:album")
        ReleaseDate = metadata.get("xmpDM:releaseDate")
        Author = metadata.get("meta:author")
        Artist = metadata.get("xmpDM:artist")
        Title = metadata.get("title")

//        var metadataNames = metadata.names()
//        metadataNames.forEach {
//            println(it)
//            println(metadata.get(it))
//        }
    }
}