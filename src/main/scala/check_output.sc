import scala.xml.XML

val doc = XML.loadFile("/home/fengkx/tmp/tt.xspf")

val tracks = (doc \\ "track").toList.length