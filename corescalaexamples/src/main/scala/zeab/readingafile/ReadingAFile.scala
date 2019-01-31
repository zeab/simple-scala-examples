package zeab.readingafile

//Imports
import scala.io.{BufferedSource, Source}

object ReadingAFile {

  def main(args: Array[String]): Unit = {

    //The path of the file
    val inputFile: String = "~/thefile.txt"

    //Open the source
    val bufferedSource: BufferedSource = Source.fromFile(inputFile)

    //Read it in and flatten it out into a list (this can prolly be done better)
    val source: List[String] = (for{lines <- bufferedSource.getLines} yield lines).map(List(_)).toList.flatten

    //Close it
    bufferedSource.close

    //Print it out
    source.foreach(println)

  }

}
