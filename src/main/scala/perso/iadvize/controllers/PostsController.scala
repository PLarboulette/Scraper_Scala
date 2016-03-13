package controllers

/**
  * Created by pierre on 12/03/16.
  */

import java.io.StringWriter
import javax.inject.Inject

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.twitter.finagle.httpx.Request
import com.twitter.finatra._
import perso.iadvize.domain.Post
import perso.iadvize.services.PostsService


class PostsController  @Inject () (postsService: PostsService) extends Controller {

  val mapper = new ObjectMapper()
  mapper.registerModule(DefaultScalaModule)

  /**
    *  Return all the posts if no  parameter is specified
    *  Otherwise, the response is filtered by the different parameters send into request
    */
  get("/posts") { request: Request =>
    val author : Option[String]= request.params.get("author")
    val from : Option[String] = request.params.get("from")
    val to : Option[String] = request.params.get("to")

    postsService.getPosts(author, from, to).map(post => post)
  }

  get("/posts/:id") {request : Request =>
  }



}
