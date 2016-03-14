package perso.iadvize


import java.net.MalformedURLException

import _root_.controllers.PostsController
import com.fasterxml.jackson.module.scala.JacksonModule
import com.google.inject.Inject
import com.twitter.finagle.httpx.{Request, Response}

import com.twitter.finatra.http._
import com.twitter.finatra.http.exceptions.ExceptionMapper
import com.twitter.finatra.http.filters.{ExceptionMappingFilter, CommonFilters}
import com.twitter.finatra.http.response.ResponseBuilder
import com.twitter.finatra.http.routing.HttpRouter
import com.twitter.finatra.logging.filter.{TraceIdMDCFilter, LoggingMDCFilter}
import perso.iadvize.database.PostsRedis

import perso.iadvize.scraper.Scrapper
import perso.iadvize.utils.MalformedURLExceptionMapper

/**
  * Created by pierre on 12/03/16.
  */
object ServerMain extends Server  {

}

class Server extends HttpServer {

  override val defaultFinatraHttpPort: String = ":8080"

  PostsRedis.clearDatabase()

  Scrapper.init()

  override def configureHttp(router: HttpRouter): Unit = {
    router
      .filter[LoggingMDCFilter[Request, Response]]
      .filter[TraceIdMDCFilter[Request, Response]]
      .filter[CommonFilters]
      .add[PostsController]
      .exceptionMapper[MalformedURLExceptionMapper]
  }

}





