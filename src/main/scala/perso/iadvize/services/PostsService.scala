package perso.iadvize.services

import org.joda.time.DateTime
import perso.iadvize.database.PostsRedis
import perso.iadvize.domain.Post
import perso.iadvize.scraper.Scrapper
import perso.iadvize.utils.Utils

import scala.collection.mutable


/**
  * Created by pierre on 12/03/16.
  */
class PostsService {


  def getPosts (author : Option[String], from : Option[String], to : Option[String]) : mutable.Set[Post] = {

    var result: mutable.Set[Post] = PostsRedis.getPosts()
    if (from.nonEmpty && to.nonEmpty) result = getPostsBetweenDates(from.get, to.get)
    else if (from.nonEmpty) result = getPostsAfterDate(from.get)
    else if (to.nonEmpty) result = getPostsAfterDate(to.get)
    if (author.nonEmpty) result.filter(post => post.author == author.get) else result


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
    val datetimeFrom: DateTime = Utils.initDatetimeFrom(from)
    val datetimeTo: DateTime = Utils.initDatetimeTo(to)
    PostsRedis.getPosts().filter(
      post =>
        new DateTime(Utils.getMinimalDate(post.date)).getMillis <= datetimeTo.getMillis
          && new DateTime(Utils.getMinimalDate(post.date)).getMillis >= datetimeFrom.getMillis
    )
  }

  def getPostsAfterDate(from: String): mutable.Set[Post] = {
    val datetimeFrom: DateTime = Utils.initDatetimeFrom(from)
    PostsRedis.getPosts().filter(
      post =>
        new DateTime(Utils.getMinimalDate(post.date)).getMillis >= datetimeFrom.getMillis
    )
  }

  def getPostsBeforeDate(to: String): mutable.Set[Post] = {
    val datetimeTo: DateTime = Utils.initDatetimeTo(to)
    PostsRedis.getPosts().filter(
      post =>
        new DateTime(Utils.getMinimalDate(post.date)).getMillis <= datetimeTo.getMillis
    )
  }





}
