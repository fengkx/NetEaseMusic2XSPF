package org.fengkx.netease_music_to_xspf

import scala.io.Source
import scala.util.matching.Regex
import scala.xml.XML

object Main extends App {
  if(args.length < 2) {
    Console.err.println("Usage: <playlist url> <output path>")
  } else {
    val playListUrl = args(0).replaceFirst("#/", "")
    val fileName = if (args(1).endsWith(".xspf")) args(1) else args(1)+".xspf"

    val output = NetEase2XSPF.toXSPF(playListUrl, songName => println("[working on] "+  songName))
    XML.save(fileName, output, "UTF-8", true, null)

    val stdOuput = (output \\ "track").toList.foldLeft("")((acc, item) => {
      acc + (item \ "title").text + " - " + (item \ "creator").text + "\n"
    })
    println(stdOuput)
  }
}

object NetEase2XSPF {
  def toXSPF(playListUrl: String, onSongCb: String => Unit= _ =>{}): xml.Elem = {
    val sourceHtml = Source.fromURL(playListUrl).mkString

    val titleRegex = "<title>(.+)</title>".r
    val playListTitle = titleRegex.findFirstMatchIn(sourceHtml).get.group(1)

    val nameRegex = "class=\"name\">\\s*<a[^>]+>([^<>]+?)<\\/a>".r
    val playListCreator = nameRegex.findFirstMatchIn(sourceHtml).get.group(1)

    val songRegex = "<li>\\s*<a href=\"(.+?)\">([^<>]+?)</a></li>".r
    val songList = songRegex.findAllMatchIn(sourceHtml).toList.map(m => {
      (m.group(2), m.group(1))
    })

    val trackList = songList.map(song => {
      val url = "https://music.163.com" + song._2
      onSongCb(song._1)
      val sourceHtml = Source.fromURL(url).mkString

      val artistRegex = "歌手：.+title=\"([^<>]+?)\">".r
      val artist = artistRegex.findFirstMatchIn(sourceHtml) match {
        case None => ""
        case Some(ma) => ma.group(1)
      }

      val albumRegex = "所属专辑.+<a[^>]+?>([^<>]+?)<\\/a>".r
      val album = albumRegex.findFirstMatchIn(sourceHtml) match {
        case None => ""
        case Some(ma) => ma.group(1)
      }
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


    playListXml
  }
}