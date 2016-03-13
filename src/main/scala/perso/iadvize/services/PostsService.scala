package perso.iadvize.services

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import perso.iadvize.domain.Post

import scala.collection.mutable
/**
  * Created by pierre on 12/03/16.
  */
class PostsService {


  def getPosts (author : Option[String], from : Option[String], to : Option[String]) : mutable.Set[Post] = {
    val result = mutable.Set[Post]()
    val post1 = Post("1","2","3","4")
    val post3= Post("5","6","7","8")
    result.add(post1)
    result.add(post3)
    result
  }

  def getPostById ( postId : String) : Option[Post] = {
    val posts : mutable.Set[Post] = getPosts(None, None, None).filter(post => post.id == postId)
    posts.headOption
  }


}
