import com.github.tototoshi.csv.CSVReader

import java.io.File
import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Paths, StandardOpenOption}

import play.api.libs.json._

import scalikejdbc._

object ScripTablas extends App{
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

  //TABLA person------------------------------------------------------------------------
/*

  case class TablaPerson(
                        Name: String
                        )

  val patronPersona =
    """
      |INSERT INTO Person (Name)
      |VALUES
      |('%s');
      |""".stripMargin

  val datos = data
    .map(elem => TablaPerson(
      escapeMysql(elem("director"))))

  val datos2 = data
    .map(elem => elem("cast"))
    .filter(_.nonEmpty)
    .flatMap(_.split(" "))
    .map(elem => TablaPerson(
      escapeMysql(elem)
    ))

  val personDirector = datos
    .map(elem => patronPersona.formatLocal(java.util.Locale.US,
      elem.Name))

  val personCast = datos2
    .map(elem => patronPersona.formatLocal(java.util.Locale.US,
      elem.Name))

  val ScriptFile = new File("C:\\Users\\USUARIO\\Desktop\\person_insert.sql")
  if (ScriptFile.exists()) ScriptFile.delete()

  personDirector.foreach(insert =>
    Files.write(Paths.get("C:\\Users\\USUARIO\\Desktop\\person_insert.sql"), insert.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.APPEND))

  personCast.foreach(insert =>
    Files.write(Paths.get("C:\\Users\\USUARIO\\Desktop\\person_insert.sql"), insert.getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND))
*/


  //TABLA cast
/*
  case class Cast(
                 personId : Int,
                 idMovie : Int,
                 Role : String,
                 )

  val patronCast =
    """
         |INSERT INTO Cast (idMovie, PersonId, Role)
         |VALUES
         |(`%d`, `%d`, `%s`);
         |""".stripMargin


  val person = data
    .map(elem => (elem("director"), elem("cast")))
    .filter(_._2.nonEmpty)
    .map(x => (x._1, x._2.split(" ")))
    .flatMap(x => x._2.map((x._1, _)))
    .map(x => (x._1, x._2))

  val lista = (1 to (person.map(_._1).distinct.size + person.map(_._2).distinct.size)).toList

  val listaDirector = (1 to person.map(_._1).distinct.size)
    .map(_ => "Director").toList

  val listaCast = (1 to person.map(_._2).distinct.size)
    .map(_ => "Actor").toList

  val listaCompleta = listaDirector ++ listaCast


  val cast = data
    .flatMap(x => x("index").zip(lista))
    .map(x => x).zip(listaCompleta)
    .map(elem => Cast(
      elem._1._1.toInt,
      elem._1._2,
      elem._2
    ))

  val cast2 = cast
    .map(elem => patronCast.formatLocal(java.util.Locale.US,
      elem.idMovie,
      elem.personId,
      elem.Role))

  val ScriptFile2 = new File("C:\\Users\\USUARIO\\Desktop\\cast_insert.sql")
  if (ScriptFile2.exists()) ScriptFile2.delete()

  cast2.foreach(insert =>
    Files.write(Paths.get("C:\\Users\\USUARIO\\Desktop\\cast_insert.sql"), insert.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.APPEND))
*/

  //TABLA companie------------------------------------------------------------------------

/*
  case class Companie(
                     OriginalId : Int,
                     name : String
                     )

  val patronCompanie =
    """
      |INSERT INTO Companie (OriginalId,`Name`)
      |VALUES
      |(%d, "%s");
      |""".stripMargin

  val companie = data
    .map(elem =>
      (Json.parse(elem("production_companies")), Json.parse(elem("production_companies"))))
    .map(x => (x._1 \\ "id", x._2 \\ "name"))
    .filter(x => x._1.nonEmpty && x._2.nonEmpty)
    .flatMap(x => x._1.zip(x._2))
    .map(x => (x._1.as[Int], x._2.as[String]))
    .map(elem => Companie(
      elem._1,
      escapeMysql(elem._2)
    ))

  val companie2 = companie
    .map(elem => patronCompanie.formatLocal(java.util.Locale.US,
      elem.OriginalId,
      elem.name
    ))

  val ScriptFile3 = new File("C:\\Users\\USUARIO\\Desktop\\companie_insert.sql")
  if (ScriptFile3.exists()) ScriptFile3.delete()

  companie2.foreach(insert =>
    Files.write(Paths.get("C:\\Users\\USUARIO\\Desktop\\companie_insert.sql"), insert.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.APPEND))

*/

  //TABLA production_companie------------------------------------------------------------------------
/*
  case class ProductionCompanies(
                                idMovie : Int,
                                idCompanie : Int
                                )

  val PatronProductionCompanie =
    """
      |INSERT INTO production_companies (idMovie, idCompany)
      |VALUES
      |(%d, %d);
      |""".stripMargin

  val production = (1 to data
    .map(x => Json.parse(x("production_companies")))
    .map(x => x \\ "name")
    .count(x => x.nonEmpty)).toList


  val production_companies = data
    .map(x => x("index"))
    .map(_.toInt).zip(production)
    .map(elem => ProductionCompanies(
      elem._1+1,
      elem._2
    ))

  val production_companies2 = production_companies
    .map(elem => PatronProductionCompanie.formatLocal(java.util.Locale.US,
      elem.idMovie,
      elem.idCompanie
    ))

  val ScriptFile4 = new File("C:\\Users\\USUARIO\\Desktop\\production_companie_insert.sql")
  if (ScriptFile4.exists()) ScriptFile4.delete()

  production_companies2.foreach(insert =>
    Files.write(Paths.get("C:\\Users\\USUARIO\\Desktop\\production_companie_insert.sql"), insert.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.APPEND))
*/

  //TABLA Language------------------------------------------------------------------------
/*
  case class Language(
                     iso_639_1 : String,
                     name : String
                     )

  val patronLanguage =
    """
      |INSERT INTO Language (iso_639_1, name)
      |VALUES
      |(%s, %s);
                           """.stripMargin

  val languages = data.
    map(elem => (Json.parse(elem("spoken_languages")), Json.parse(elem("spoken_languages"))))
    .map(x => (x._1 \\ "iso_639_1", x._2 \\ "name"))
    .filter(x => x._1.nonEmpty && x._2.nonEmpty)
    .flatMap(x => x._1.distinct.zip(x._2.distinct))
    .map(x => (x._1.as[String], x._2.as[String]))
    .map(elem => Language(
      elem._1,
      elem._2
    ))

  val languages2 = languages
    .map(elem => patronLanguage.formatLocal(java.util.Locale.US,
      elem.iso_639_1,
      elem.name
    ))

  val ScriptFile5 = new File("C:\\Users\\USUARIO\\Desktop\\languages_insert.sql")
  if (ScriptFile5.exists()) ScriptFile5.delete()

  languages2.foreach(insert =>
    Files.write(Paths.get("C:\\Users\\USUARIO\\Desktop\\languages_insert.sql"), insert.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.APPEND))
*/

  //TABLE spoken_language------------------------------------------------------------------------

  /*case class Spoken_languages(
                             idMovie : Int,
                             LanguageId : Int
                             )
  val patronSpoken_languages =
    """
      |INSERT INTO spoken_language (`idMovie`,`LanguageId`)
      |VALUES
      |(%d, %d);
                             """.stripMargin

  val spoken = (1 to data
    .map(x =>
      Json.parse(x("spoken_languages")))
    .map(x => x \\ "name")
    .count(x => x.distinct.nonEmpty)).toList

  val spoken_language = data
    .map(x => x("index"))
    .map(_.toInt).zip(spoken)
    .map(elem => Spoken_languages(
      elem._1+1,
      elem._2
    ))

  val spoken_language2 = spoken_language
    .map(elem => patronSpoken_languages.formatLocal(java.util.Locale.US,
      elem.idMovie,
      elem.LanguageId
    ))

  val ScriptFile6 = new File("C:\\Users\\USUARIO\\Desktop\\spoken_language_insert.sql")
  if (ScriptFile6.exists()) ScriptFile6.delete()

  spoken_language2.foreach(insert =>
    Files.write(Paths.get("C:\\Users\\USUARIO\\Desktop\\spoken_language_insert.sql"), insert.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.APPEND))
*/
}
