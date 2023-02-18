import com.github.tototoshi.csv.CSVReader

import java.io._
import com.cibo.evilplot.plot.BarChart
import com.cibo.evilplot.plot.aesthetics.DefaultTheme.{DefaultElements, DefaultTheme}
import scalikejdbc._

object Preguntas extends App{

  val reader = CSVReader.open(new File("C:\\Users\\USUARIO\\Downloads\\movie_dataset.csv"))
  val data = reader.allWithHeaders()
  reader.close()

  //1. ¿Cuantas películas tiene cada idioma?

  val idioma = data
    .map(elem => elem("original_language"))

  val cantidadIdioma = idioma.groupBy(identity).mapValues(_.size).toList.sortBy(-_._2)
  println("1. ¿Cuantas películas tiene cada idioma?")
  println(cantidadIdioma , "\n")

  //2. top 10 de las peliculas más taquilleras del año 2009

  val pelicula = data
    .map(elem => (elem("title"), elem("revenue"), elem("release_date")))
    .filter(x => x._3.contains("2009"))
    .map(x => (x._1, x._2.toLong, x._3))

  implicit val theme = DefaultTheme.copy(
    elements = DefaultElements.copy(categoricalXAxisLabelOrientation = 45))
  println("2. top 10 de las peliculas más taquilleras del año 2009")
  println(pelicula.sortBy(-_._2).take(10).map(_._1), "\n")

  BarChart(pelicula.sortBy(-_._2).take(10).map(_._2))
    .title("top 10 de las peliculas más taquilleras del año 2009")
    .xAxis(pelicula.sortBy(-_._2).take(10).map(_._1))
    .yAxis()
    .frame()
    .yLabel("")
    .bottomLegend()
    .render()
    .write(new File("C:\\Users\\USUARIO\\Downloads\\taquilleras.png"))

//3. obtener el título de las peliculas con una ganancia mayor o igual a 1000000000 que se han lanzado desde el 2010

val pelicula2 = data
  .map(elem => (elem("title"), elem("revenue"), elem("release_date")))
  .filter(x => x._3.contains("201"))
  .filter(x => x._2.toLong >= 1000000000)
  .map(x => (x._1, x._2.toLong, x._3))

  println("3. obtener el título de las peliculas con una ganancia mayor o igual " +
    "a 1000000000 que se han desde despues el 2010")
  println(pelicula2.sortBy(-_._2).map(_._1), "\n")

  BarChart(pelicula2.sortBy(-_._2).map(_._2))
    .title("peliculas con una ganancia mayor o igual " +
      "a 1000000000 que se han desde despues el 2010")
    .xAxis(pelicula2.sortBy(-_._2).map(_._1))
    .yAxis()
    .frame()
    .yLabel("")
    .bottomLegend()
    .render()
    .write(new File("C:\\Users\\USUARIO\\Downloads\\taquilleras2.png"))

  //4. promedio de votos de las 5 primeras peliculas más largas

  val votos = data
    .map(elem => (elem("title"),elem("vote_average"), elem("runtime")))
    .filter(x => x._1.nonEmpty && x._2.nonEmpty && x._3.nonEmpty)
    .map(x => (x._1, x._2.toDouble, x._3.toDouble))

  println("4. promedio de votos de las 5 primeras peliculas más largas")
  println(votos.sortBy(-_._3).map(_._2).take(5))

  BarChart(votos.sortBy(-_._3).map(_._2).take(5))
    .title("promedio de votos de las 5 primeras peliculas más largas")
    .xAxis(votos.sortBy(-_._3).map(_._1).take(5))
    .yAxis()
    .frame()
    .yLabel("")
    .bottomLegend()
    .render()
    .write(new File("C:\\Users\\USUARIO\\Downloads\\peliculasLargas.png"))

  //5. ¿Cuáles son las peliculas que se encuentran en post production a partir del 2010?

  val status = data
    .map(elem => (elem("title"), elem("status"), elem("release_date")))
    .filter(x => x._1.nonEmpty && x._2.nonEmpty && x._3.nonEmpty)
    .filter(x => x._2.contains("Post Production") && x._3.contains("201"))

  println("5. ¿Cuáles son las peliculas que se encuentran en post production a partir del 2010?")
  println(status)

  println("----------------------------------------------------------------------------------")
  println("Generación de script")
  // Nombre del archivo que se generará con las consultas
  val nombreArchivo = "C:\\Users\\USUARIO\\Desktop\\consultas.sql"

  // Función que genera las consultas

  def generarConsultas(): Seq[SQL[Nothing, NoExtractor]] = Seq(
    sql"SELECT original_language AS 'Idioma', COUNT(original_language) AS 'Número de películas' FROM Movie GROUP BY original_language;",
    sql"SELECT title AS 'Título', release_date AS 'Fecha' , revenue FROM Movie WHERE SUBSTRING(release_date, 1, 4) = '2009' ORDER BY revenue DESC LIMIT 10;",
    sql"SELECT title AS 'Título', revenue AS 'Ganacia', release_date AS 'Fecha' FROM movie WHERE(revenue >= 1000000000 && SUBSTRING(release_date, 1, 4) > '2010') ORDER BY release_date;",
    sql"SELECT title AS 'Título', vote_average AS 'Promedio de votos', runtime AS 'Duración' FROM movie ORDER BY runtime DESC LIMIT 5;",
    sql"SELECT title AS 'Título', status AS 'Estatus', release_date AS 'Fecha' FROM movie WHERE (status = 'post production' && SUBSTRING(release_date, 1, 4) >= '2010');"
  )

  // Apertura del archivo y escritura de las consultas
  using(new PrintWriter(nombreArchivo)) { escritor =>
    generarConsultas().foreach { consulta =>
      escritor.println(consulta.statement)
      escritor.println()
    }
  }

  println(s"Se han generado las consultas en el archivo $nombreArchivo")

}
