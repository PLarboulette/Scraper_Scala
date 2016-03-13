package perso.iadvize.scraper

import java.util.UUID

import net.ruippeixotog.scalascraper.browser.Browser
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL.Parse._
import org.jsoup.nodes.Element
import perso.iadvize.database.PostsRedis
import perso.iadvize.domain.Post
import perso.iadvize.utils.Utils
import scala.collection.mutable
import scala.collection.mutable.ListBuffer


/**
  * Created by pierre on 13/03/16.
  */
object Scrapper {


  def init () : Unit = {
    val browser = new Browser
    val doc = browser.get("http://www.viedemerde.fr")

    val contentList = mutable.ListBuffer[String]()
    val dateList = mutable.ListBuffer[String]()
    val authorList = mutable.ListBuffer[String]()

    for {
      content <- browser.get("http://www.viedemerde.fr") tryExtract elementList("div p")
    }
      content.asInstanceOf[List[Element]].map {
        item =>
          // Get the children of the current paragraph
          val children  = item.children()

          // Content of the post
          var contentPost: String = ""
          children
            .filter(child => !child.attr("href").isEmpty)
            .filter(child => child.className().equalsIgnoreCase("fmllink"))
            .foreach {
              child => contentPost = contentPost + child.html
            }

          if (contentPost.trim.nonEmpty) contentList += contentPost


          // Date, hour  and author of the post
          var datePost : String = ""
          var authorPost : String = ""
          children.filter(child => child.className().equalsIgnoreCase("liencat"))
            .foreach {
              child =>
                datePost = datePost + child.parent.text.split("-")(0).split("à")(0).substring(3)  // OK
                datePost = datePost + " "+ child.parent.text.split("-")(0).split("à")(1).substring(1) // OK
                authorPost = child.parent.text.split("-")(2).substring(5)
            }

          if (datePost.trim.nonEmpty) dateList += datePost
          if (authorPost.trim.nonEmpty) authorList += authorPost

      }


    for( a <- contentList.indices){
      // Id of the post (I choose to generate UUID instead of use for example the ID given by the website ofr their divs)
      val uuid = UUID.randomUUID.toString
      val post : Post = Post(uuid, contentList(a), Utils.convertDate(dateList(a)).toString("YYYY-MM-dd hh:mm:ss"), authorList(a))
      PostsRedis.savePost(post)
    }

    PostsRedis.getPosts()
  }




}