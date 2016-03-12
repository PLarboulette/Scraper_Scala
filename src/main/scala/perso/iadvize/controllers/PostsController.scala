package controllers

/**
  * Created by pierre on 12/03/16.
  */

import java.io.StringWriter

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.twitter.finagle.httpx.Request
import com.twitter.finatra._
import perso.iadvize.domain.Post


class PostsController extends Controller {

  val mapper = new ObjectMapper()
  mapper.registerModule(DefaultScalaModule)

  /**
    *  Return all the postss if no  parameter is specified
    *  Otherwise, the response is filtered by the different parameters send into request
    */
  get("/posts") { request: Request =>

    val post = Post("1","13", "Coucou", "OK")

    val out = new StringWriter
    mapper.writeValue(out, post)
    val json = out.toString

    /*val person2 = mapper.readValue(json, classOf[Person])
    println(person2)*/

    // Get the parameters if they exists
    val author : String = request.params.getOrElse("author", None) toString
    val from : String = request.params.getOrElse("from", None) toString
    val to : String = request.params.getOrElse("to", None) toString


   /* */

    json

  }


}
