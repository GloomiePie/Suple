import java.io.File
import com.github.tototoshi.csv.CSVReader
import io.circe.JsonObject
import scalikejdbc._
import play.api.libs.json._
import requests.{Response, readTimeout}

import scala.util.{Failure, Success, Try}
import scala.util.matching.Regex

object SentenciasInsertInto extends App {
  val reader = CSVReader.open(new File("C:\\Users\\USUARIO\\Downloads\\movie_dataset.csv"))
  val data = reader.allWithHeaders()
  reader.close()

  Class.forName("com.mysql.cj.jdbc.Driver")
  ConnectionPool.singleton("jdbc:mysql://localhost:3306/ProyectoIntegrador", "root", "29122003g?")
  implicit val session: DBSession = AutoSession

  /*//COLUMNA GENRES - Pasado A Base De Datos
  val generos = data
    .map(elem => (elem("id"), elem("genres").replace("Science Fiction", "Science_Fiction")))
    .filter(_._2.nonEmpty)
    .map(x => (x._1, x._2.split(" ")))
    .flatMap(x => x._2.map((x._1, _)))
    .map(x => (x._1.toInt, x._2))
    .sortBy(_._1)
    generos.map(elem =>
    sql"""
         |INSERT INTO genres(idMovie, name)
         |VALUES
         |(${elem._1}, ${elem._2})
               """.stripMargin
      .update
      .apply())*/

  //TABLA PERSON - Pasado a base de datos

  val person = data
    .map(elem => (elem("director"), elem("cast")))
    .filter(_._2.nonEmpty)
    .map(x => (x._1, x._2.split(" ")))
    .flatMap(x => x._2.map((x._1, _)))
    .map(x => (x._1, x._2))

//COLUMNA person a base de datos
/*
 val personDirector = person
    .map(_._1)
   .distinct
    .map(elem =>
    sql"""
         |INSERT INTO Person (Name, Role)
         |VALUES
         |(${elem}, ${"Director"})
                   """.stripMargin
      .update
      .apply()
    )


 val personCast = person
    .map(_._2)
   .distinct
    .map(elem =>
    sql"""
         |INSERT INTO Person (Name, Role)
         |VALUES
         |(${elem}, ${"Actor"})
                 """.stripMargin
      .update
      .apply())*/

  //TABLA Cast - Pasado a base de datos
/*
val lista = (1 to (person.map(_._1).size + person.map(_._2).size)).toList
  val cast = data
    .flatMap(x => x("index").zip(lista))
    .map(elem =>
     sql"""
         |INSERT INTO Cast (idMovie, PersonId)
         |VALUES
         |(${elem._1.toInt+1}, ${elem._2})
                 """.stripMargin
      .update
      .apply())


*/
  //COLUMNA companie - Pasado a base de datos
  /*val companie = data
    .map(elem =>
      (Json.parse(elem("production_companies")), Json.parse(elem("production_companies"))))
    .map(x => (x._1 \\ "id", x._2 \\ "name"))
    .filter(x => x._1.nonEmpty && x._2.nonEmpty)
    .flatMap(x => x._1.zip(x._2))
    .map(x => (x._1.as[Int], x._2.as[String]))
    .map(elem =>
      sql"""
           |INSERT INTO Companie (name, OriginalId)
           |VALUES
           |(${elem._2},${elem._1})
                     """.stripMargin
        .update
        .apply())*/

  //COLUMNA production_companies - Pasado a base de datos
/*

  val production = (1 to data
    .map(x => Json.parse(x("production_companies")))
    .map(x =>x \\ "name")
    .count(x => x.nonEmpty)).toList


 val production_companies = data
   .map(x => x("index"))
   .map(_.toInt).zip(production)

    production_companies.map(elem =>
    sql"""
         |INSERT INTO production_companies (idMovie, idCompany)
         |VALUES
         |(${elem._1 + 1}, ${elem._2})
                     """.stripMargin
      .update
      .apply())

*/

  //COLUMNA Languages - Pasado a base de datos
  /*val languages = data.
    map(elem => (Json.parse(elem("spoken_languages")), Json.parse(elem("spoken_languages"))))
    .map(x => (x._1 \\ "iso_639_1", x._2 \\ "name"))
    .filter(x => x._1.nonEmpty && x._2.nonEmpty)
    .flatMap(x => x._1.zip(x._2))
    .map(x => (x._1.as[String], x._2.as[String]))
    .map(elem =>
      sql"""
           |INSERT INTO Language (iso_639_1, name)
           |VALUES
           |(${elem._1},${elem._2})
                       """.stripMargin
        .update
        .apply())
*/
  //COLUMNA spoken_language - Pasado a base de datos
  /*val spoken =(1 to data
    .map(x =>
      Json.parse(x("spoken_languages")))
    .map(x => x \\ "name")
    .count(x => x.nonEmpty)).toList

  val spoken_language = data
    .map(x => x("index"))
    .map(_.toInt).zip(spoken)
  spoken_language.map(elem =>
    sql"""
         |INSERT INTO spoken_language (`idMovie`,`LanguageId`)
         |VALUES
         |(${elem._1 + 1}, ${elem._2})
                           """.stripMargin
      .update
      .apply())*/

//COLUMNA Crew
val pattern1 = "(\\s\"(.*?)\",)".r
val pattern2 = "([a-z]\\s\"(.*?)\"\\s*[A-Z])".r
val pattern3 = "(:\\s'\"(.*?)',)".r
def replacePattern4(original: String, pattern: Regex): String = {
  var txtOr = original
  for (m <- pattern.findAllIn(original)) {
    if (pattern == pattern2) {
      txtOr = txtOr.replace(m, m.replace("\"", "-u0022"))
    } else if (pattern == pattern1) {
      txtOr = txtOr.replace(m, m.replace("'", "-u0027"))
    } else if (pattern == pattern3) {
      txtOr = txtOr.replace(m, m.replace("\"", "-u0022"))
    }
  }
  txtOr
}
val crew = data
  .map(row => row("crew"))
  .map(text => replacePattern4(text, pattern2))
  .map(text => replacePattern4(text, pattern1))
  .map(text => replacePattern4(text, pattern3))
  .map(text => text.replace("'", "\""))
  .map(text => text.replace("-u0027", "'"))
  .map(text => text.replace("-u0022", "\\\""))
val id = data
  .map(row => row("index"))
val crewId = id.zip(crew)
  .map(x =>
    (x._1, Json.parse(x._2), Json.parse(x._2), Json.parse(x._2), Json.parse(x._2), Json.parse(x._2), Json.parse(x._2)))
  .map(x => (x._1.toInt, x._2 \\ "name", x._3 \\ "gender", x._4 \\ "department", x._5 \\ "job", x._6 \\ "credit_id", x._7 \\ "id"))
  .filter(x => x._2.nonEmpty && x._3.nonEmpty)
  .flatMap(x => x._2.zip(x._3).zip(x._4).zip(x._5).zip(x._6).zip(x._7).map((x._1, _)))
  .map{ case (id, (((((name, gender), department), job), creditId), personId)) =>
    (id, name.as[String], gender.as[Int], department.as[String], job.as[String], creditId.as[String], personId.as[Int])
  }
crewId.map(elem =>
  sql"""
       |INSERT INTO Crew (idMovie, id, name, gender, departament, job, credit_id)
       |VALUES
       |(${elem._1+1}, ${elem._7}, ${elem._2}, ${elem._3}, ${elem._4}, ${elem._5}, ${elem._6})
                   """.stripMargin
    .update
    .apply())
}
