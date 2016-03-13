package perso.iadvize.database

import perso.iadvize.domain.Post

import scala.collection.mutable
/**
  * Created by pierre on 12/03/16.
  */
class PostsRedis {

  def getPosts : mutable.Set[Post] = {

    mutable.Set[Post]()

  }

  def savePost (post : Post) : Unit = {

  }

}
