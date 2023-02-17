import com.github.tototoshi.csv.CSVReader

import java.io.File
import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Paths, StandardOpenOption}

object ScripTablaUnivesal extends App{
  val reader = CSVReader.open(new File("C:\\Users\\USUARIO\\Downloads\\movie_dataset.csv"))
  val data = reader.allWithHeaders()
  reader.close()

  def escapeMysql(text: String): String = text
    .replaceAll("\\\\", "\\\\\\\\")
    .replaceAll("\b", "\\\\b")
    .replaceAll("\n", "\\\\n")
    .replaceAll("\r", "\\\\r")
    .replaceAll("\t", "\\\\t")
    .replaceAll("\\x1A", "\\\\Z")
    .replaceAll("\\x00", "\\\\0")
    .replaceAll("'", "\\\\'")
    .replaceAll("\"", "\\\\\"")

  case class Movie(
                    budget: Long,
                    homepage: String,
                    id_original: Int,
                    original_language: String,
                    original_title: String,
                    overview: String,
                    popularity: Double,
                    release_date: String,
                    revenue: Long,
                    runtime: Double,
                    status: String,
                    tagline: String,
                    title: String,
                    vote_average: Double,
                    vote_count: Int
                  )

  val movieData = data
    .map(row => Movie(
      row("budget").toLong,
      row("homepage"),
      row("id").toInt,
      row("original_language"),
      row("original_title"),
      row("overview") ,
      row("popularity").toDouble,
      row("release_date"),
      row("revenue").toLong,
      row("runtime") match {
        case valueOfRun if valueOfRun.trim.isEmpty => 0.0
        case valueOfRun => valueOfRun.toDouble
      },
      row("status"),
      row("tagline"),
      row("title"),
      row("vote_average").toDouble,
      row("vote_count").toInt
    )
    )

  val SQL_INSERT_PATTERN =
    """INSERT INTO Movie (budget, homepage, id_original, original_language, original_title, overview, popularity, release_date, revenue, runtime, status, tagline, title, vote_average, vote_count)
      |VALUES
      |('%d', '%s', '%d' , '%s', '%s', '%s', '%f', '%s', '%d', '%f', '%s', '%s', '%s', '%f', '%d');
      |""".stripMargin

  val scriptData = movieData
    .map(movie => SQL_INSERT_PATTERN.formatLocal(java.util.Locale.US,
      movie.budget,
      escapeMysql(movie.homepage),
      movie.id_original,
      escapeMysql(movie.original_language),
      escapeMysql(movie.original_title),
      escapeMysql(movie.overview),
      movie.popularity,
      movie.release_date,
      movie.revenue,
      movie.runtime,
      movie.status,
      escapeMysql(movie.tagline),
      escapeMysql(movie.title),
      movie.vote_average,
      movie.vote_count
    ))

  val ScriptFile = new File("C:\\Users\\USUARIO\\Desktop\\movies_insert.sql")
  if(ScriptFile.exists()) ScriptFile.delete()

  scriptData.foreach(insert =>
    Files.write(Paths.get("C:\\Users\\USUARIO\\Desktop\\movies_insert.sql"), insert.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.APPEND)
  )
}