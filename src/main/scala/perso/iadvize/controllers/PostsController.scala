package controllers

/**
  * Created by pierre on 12/03/16.
  */

import javax.inject.Inject

import com.twitter.finagle.httpx.Request
import com.twitter.finatra._
import perso.iadvize.domain.Post
import perso.iadvize.services.PostsService

class PostsController  @Inject () (postsService: PostsService) extends Controller {


  /**
    *  Return all the posts if no  parameter is specified
    *  Otherwise, the response is filtered by the different parameters send into request
    */
  get("/posts") { request: Request =>
    val author : Option[String]= request.params.get("author")
    val from : Option[String] = request.params.get("from")
    val to : Option[String] = request.params.get("to")

    val dateRegex = "([0-9]{4}-[0-9]{2}-[0-9]{2})"
    val DateOnly = dateRegex.r
    def get(s: String): Boolean = s match {
      case DateOnly(d) => true
      case _ => false
    }

    val goodFormatFrom: Boolean = if (from.nonEmpty) get(from.get) else false
    val goodFormatTo: Boolean = if (to.nonEmpty) get(to.get) else false

    if (from.nonEmpty && !goodFormatFrom)
      Map("Date" -> "Incorrect format for From input")
    else if (to.nonEmpty && !goodFormatTo)
      Map("Date" -> "Incorrect format for To input")
    else {
      val posts: Seq[Post] = postsService.getPosts(author, from, to).toSeq.sorted
      Map("posts" -> posts, "count" -> posts.size)
    }

  }

  get("/posts/:id") {request : Request =>
    val id : String = request.params.getOrElse("id","Fail")
    val post : Option[Post] =  postsService.getPostById(id)

    if (post.nonEmpty) Map("posts" -> postsService.getPostById(id).toSeq.sorted)
    else Map("Error" -> "ID not recognized")

  }

  get("/scraping") { request : Request =>
    postsService.scraping()
    Map("Scraping" -> "Scrap is fun")
  }
}
