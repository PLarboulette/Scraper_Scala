package perso.iadvize.scraper

import java.util.UUID

import net.ruippeixotog.scalascraper.browser.Browser
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL._
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import perso.iadvize.database.PostsRedis
import perso.iadvize.domain.Post
import perso.iadvize.utils.Utils

import scala.collection.mutable


/**
  * Created by pierre on 13/03/16.
  */
object Scrapper {

  def init () : Unit = {
    val browser = new Browser

    for {
      content <- browser.get("http://www.viedemerde.fr") tryExtract elementList("div p")
    } processPage(content)

    for( a <- 1 to 15 ) {
      for {
        content <- browser.get("http://www.viedemerde.fr/?page="+a) tryExtract elementList("div p")
      }
        processPage(content)
    }
  }

  def processPage (content : AnyRef) : Unit = {

    var contentList = mutable.ListBuffer[String]()
    var dateList = mutable.ListBuffer[String]()
    var authorList = mutable.ListBuffer[String]()

    content.asInstanceOf[List[Element]].foreach {
      item =>
        // Get the children of the current paragraph
        val children  = item.children()

        // Content of the post
         contentList = processContent(children, contentList)

        // Date, hour of the post
        dateList = processDate(children, dateList)

        // Author of the post
        authorList = processAuthor(children, authorList)
    }

    for( a <- contentList.indices){
      // Id of the post (I choose to generate UUID instead of use for example the ID given by the website ofr their divs)
      val uuid = UUID.randomUUID.toString
      val post : Post = Post(uuid, contentList(a), Utils.dateStringFormat(dateList(a)), authorList(a))
      PostsRedis.savePost(post)
    }
  }


  def processContent (children : AnyRef, contentList : mutable.ListBuffer[String]) : mutable.ListBuffer[String] = {
    val result = mutable.ListBuffer[String]()
    result.appendAll(contentList)
    var contentPost: String = ""
    children.asInstanceOf[Elements]
      .filter(child => !child.attr("href").isEmpty)
      .filter(child => child.className().equalsIgnoreCase("fmllink"))
      .foreach {
        child => contentPost = contentPost + child.html
      }
    if (contentPost.trim.nonEmpty) result += contentPost
    result
  }

  def processDate (children : AnyRef, dateList : mutable.ListBuffer[String]) : mutable.ListBuffer[String] = {
    val result = mutable.ListBuffer[String]()
    result.appendAll(dateList)
    var datePost : String = ""
    children.asInstanceOf[Elements]
      .filter(child => child.className().equalsIgnoreCase("liencat"))
      .filter(child => child.parent.text.startsWith("Le") )
      .foreach {
        child =>
          datePost = datePost + child.parent.text.split("-")(0).split("à")(0).substring(3)  // Get date
          datePost = datePost + " "+ child.parent.text.split("-")(0).split("à")(1).substring(1) // Get hour
      }
    if (datePost.trim.nonEmpty) result += datePost
    result
  }

  def processAuthor ( children : AnyRef, authorList : mutable.ListBuffer[String]) : mutable.ListBuffer[String] = {
    val result = mutable.ListBuffer[String]()
    result.appendAll(authorList)
    var authorPost : String = ""
    children.asInstanceOf[Elements]
      .filter(child => child.className().equalsIgnoreCase("liencat"))
      .filter(child => child.parent.text.startsWith("Le"))
      .foreach {
        child =>
          authorPost = child.parent.text.split("-")(2).substring(5)
      }
    if (authorPost.trim.nonEmpty) result += authorPost
    result
  }
}
