import java.io.File
import com.github.tototoshi.csv.CSVReader
import scalikejdbc._

import play.api.libs.json._
import requests.Response


object SentenciasInsertInto extends App {
  val reader = CSVReader.open(new File("C:\\Users\\USUARIO\\Downloads\\movie_dataset.csv"))
  val data = reader.allWithHeaders()
  reader.close()

  Class.forName("com.mysql.cj.jdbc.Driver")
  ConnectionPool.singleton("jdbc:mysql://localhost:3306/ProyectoIntegrador", "root", "29122003g?")
  implicit val session: DBSession = AutoSession


  //TABLA PERSON - Pasado a base de datos

  val person = data
    .map(elem => (elem("director"), elem("cast")))
    .filter(_._2.nonEmpty)
    .map(x => (x._1, x._2.split(" ")))
    .flatMap(x => x._2.map((x._1, _)))
    .map(x => (x._1, x._2))

//TABLA person------------------------------------------------------------------------
/*
 val personDirector = person
    .map(_._1)
   .distinct
    .map(elem =>
    sql"""
         |INSERT INTO Person (Name)
         |VALUES
         |(${elem})
                   """.stripMargin
      .update
      .apply()
    )


 val personCast = person
    .map(_._2)
   .distinct
    .map(elem =>
    sql"""
         |INSERT INTO Person (Name)
         |VALUES
         |(${elem})
                 """.stripMargin
      .update
      .apply())*/

  //TABLA Cast------------------------------------------------------------------------
/*
val lista = (1 to (person.map(_._1).distinct.size + person.map(_._2).distinct.size)).toList

  val listaDirector = (1 to person.map(_._1).distinct.size)
    .map(_ => "Director").toList

  val listaCast = (1 to person.map(_._2).distinct.size)
    .map(_ => "Actor").toList

 val listaCompleta = listaDirector ++ listaCast


 val cast = data
    .flatMap(x => x("index").zip(lista))

  cast.map(x => x).zip(listaCompleta)
   .map(elem =>
     sql"""
         |INSERT INTO Cast (idMovie, PersonId, Role)
         |VALUES
         |(${elem._1._1.toInt + 1}, ${elem._1._2}, ${elem._2})
                 """.stripMargin
      .update
      .apply())


*/


  //COLUMNA companie------------------------------------------------------------------------
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

  //COLUMNA production_companies------------------------------------------------------------------------

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


  //COLUMNA Languages------------------------------------------------------------------------
 /* val languages = data.
    map(elem => (Json.parse(elem("spoken_languages")), Json.parse(elem("spoken_languages"))))
    .map(x => (x._1 \\ "iso_639_1", x._2 \\ "name"))
    .filter(x => x._1.nonEmpty && x._2.nonEmpty)
    .flatMap(x => x._1.distinct.zip(x._2.distinct))
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
  //COLUMNA spoken_language------------------------------------------------------------------------
 /* val spoken =(1 to data
    .map(x =>
      Json.parse(x("spoken_languages")))
    .map(x => x \\ "name")
    .count(x => x.distinct.nonEmpty)).toList

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
      .apply())
*/
}
