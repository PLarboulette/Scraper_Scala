package perso.iadvize.services

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import org.joda.time.DateTime
import perso.iadvize.database.PostsRedis
import perso.iadvize.domain.Post
import perso.iadvize.scraper.Scrapper

import scala.collection.mutable
import perso.iadvize.utils.Utils


/**
  * Created by pierre on 12/03/16.
  */
class PostsService {


  def getPosts (author : Option[String], from : Option[String], to : Option[String]) : mutable.Set[Post] = {

      if (author nonEmpty) PostsRedis.getPosts().filter(post => post.author == author.get)
      else if (from.nonEmpty && to.nonEmpty) getPostsBetweenDates(from.get, to.get)
      else PostsRedis.getPosts()
  }

  def getPostById (postId : String) : Option[Post] = {
    val posts : mutable.Set[Post] = getPosts(None, None, None).filter(post => post.id == postId)
    posts.headOption
  }

  def scraping() : Unit = {
    PostsRedis.clearDatabase()
    Scrapper.init()
  }

  def getPostsBetweenDates (from: String, to : String) : mutable.Set[Post] = {
    val datetimeFrom : DateTime = new DateTime(from)
    val datetimeTo : DateTime = new DateTime(to)
    PostsRedis.getPosts().filter(
      post =>
        new DateTime(Utils.getMinimalDate(post.date)).isBefore(datetimeTo)
          && new DateTime(Utils.getMinimalDate(post.date)).isAfter(datetimeFrom)
    )

  }


}
