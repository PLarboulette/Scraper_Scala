package perso.iadvize.scraper

import java.util.UUID

import net.ruippeixotog.scalascraper.browser.Browser
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL.Parse._
import org.jsoup.nodes.Element
import perso.iadvize.domain.Post

import scala.collection.mutable


/**
  * Created by pierre on 13/03/16.
  */
object Scrapper {


  def init () : Unit = {
    val browser = new Browser
  val doc = browser.get("http://www.viedemerde.fr")
    //    val doc2 = browser.parseFile("file.html")

   /* val items: List[Element] = doc extract elementList("#8690529")

    val itemTitles: List[String] = items.map(_ >> text("div"))
    println(items)*/


    val mapPostss  = mutable.HashMap[String,Post]()

    for {
      content <- browser.get("http://www.viedemerde.fr") tryExtract elementList("div p")
      date <- browser.get("http://www.viedemerde.fr") tryExtract elementList("div .date .right_part p") // Seems OK
    }
      content.asInstanceOf[List[Element]].map {
      item =>

        // Get the children of the current paragraph
        val children  = item.children()

        // Id of the post (I choose to generate UUID instead of use for example the ID given by the website ofr their divs)
        val postID : String = UUID.randomUUID.toString

        // Content of the post
        var contentPost: String = ""
        children
          .filter(child => !child.attr("href").isEmpty)
          .filter(child => child.className().equalsIgnoreCase("fmllink"))
          .foreach {
            child => contentPost = contentPost + child.html
          }

        // Date, hour  and author of the post
        var datePost : String = ""
        var authorPost : String = ""
        children.filter(child => child.className().equalsIgnoreCase("liencat"))
          .map {
            child =>
              datePost = datePost + child.parent.text.split("-")(0).split("à")(0).substring(3)  // OK
              datePost = datePost + " "+ child.parent.text.split("-")(0).split("à")(1).substring(1) // OK
              authorPost = child.parent.text.split("-")(2).substring(5)
          }

        val post : Post = Post(postID, contentPost, datePost, authorPost)

      }

  }




}
