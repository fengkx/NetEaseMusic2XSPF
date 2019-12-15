import scala.io.Source
import scala.xml.XML


val playListUrl = "https://music.163.com/playlist?id=8471571"
val sourceHtml = Source.fromURL(playListUrl).mkString

val titleRegex = "<title>(.+)</title>".r
val playListTitle = titleRegex.findFirstMatchIn(sourceHtml).get.group(1)

val nameRegex = "class=\"name\">\\s*<a[^>]+>(.+)<\\/a>".r
val playListCreator = nameRegex.findFirstMatchIn(sourceHtml).get.group(1)

val songRegex = "<li>\\s*<a href=\"(.+?)\">(.+?)</a></li>".r
val songList = songRegex.findAllMatchIn(sourceHtml).toList.map(m => {
  (m.group(2), m.group(1))
})

val trackList = songList.slice(0,2).map(song => {
  val url = "https://music.163.com" + song._2
  val sourceHtml = Source.fromURL(url).mkString

  val artistRegex = "歌手：.+title=\"(.+)\">".r
  val artist = artistRegex.findFirstMatchIn(sourceHtml).get.group(1)

  val albumRegex = "所属专辑.+<a[^>]+?>(.+)<\\/a>".r
  val album = albumRegex.findFirstMatchIn(sourceHtml).get.group(1)
  (artist, album, url, song._1)
})

val playListXml =
  <playlist version="1" xmlns="http://xspf.org/ns/0/">
    <title>{playListTitle}</title>
    <creator>{playListCreator}</creator>
    <location>{playListUrl}</location>
    <trackList>
      {
        trackList.map(track =>
          <track>
            <location>{track._3}</location>
            <title>{track._4}</title>
            <album>{track._2}</album>
            <creator>{track._1}</creator>
          </track>
        )
      }
    </trackList>
  </playlist>


XML.save("/home/fengkx/tmp/t.xml", playListXml, "UTF-8", true, null)