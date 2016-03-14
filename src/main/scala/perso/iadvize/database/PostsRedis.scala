package perso.iadvize.database

import org.joda.time.DateTime
import perso.iadvize.domain.Post
import com.redis._

import scala.collection.mutable

/**
  * Created by pierre on 12/03/16.
  */
object PostsRedis {

  val r = new RedisClient("localhost", 6379)


  def clearDatabase () : Unit = {
    r.flushdb
  }

  def savePost (post : Post) : Unit = {
    r.rpush("Post : " +post.id, post.content)
    r.rpush("Post : " +post.id, post.date)
    r.rpush("Post : " +post.id, post.author)
  }

  def getPosts() : scala.collection.mutable.Set[Post] = {
    val result = mutable.Set[Post]()
    val set = r.keys("Post : *").get
    set.foreach { element =>
      val postAttributes = r.lrange(element.get, 0, 200).get
      val id = element.get.split(":")(1).trim
      val post : Post = Post(id,postAttributes.head.get.toString, postAttributes(1).get.toString, postAttributes(2).get.toString)
      if (result.size < 200) result += post
    }
    result
  }



}
