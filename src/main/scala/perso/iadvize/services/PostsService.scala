package perso.iadvize.services

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import perso.iadvize.database.PostsRedis
import perso.iadvize.domain.Post
import perso.iadvize.scraper.Scrapper

import scala.collection.mutable
/**
  * Created by pierre on 12/03/16.
  */
class PostsService {


  def getPosts (author : Option[String], from : Option[String], to : Option[String]) : mutable.Set[Post] = {
    PostsRedis.getPosts()
  }

  def getPostById ( postId : String) : Option[Post] = {
    val posts : mutable.Set[Post] = getPosts(None, None, None).filter(post => post.id == postId)
    posts.headOption
  }

  def scraping() : Unit = {
    Scrapper.init()
  }


}
