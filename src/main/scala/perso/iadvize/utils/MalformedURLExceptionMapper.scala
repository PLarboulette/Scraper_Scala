package perso.iadvize.utils

import java.net.MalformedURLException

import com.google.inject.Inject
import com.twitter.finagle.httpx.{Request, Response}
import com.twitter.finatra.http.exceptions.ExceptionMapper
import com.twitter.finatra.http.response.ResponseBuilder

/**
  * Created by pierre on 14/03/16.
  */

class MalformedURLExceptionMapper @Inject()(response: ResponseBuilder)
  extends ExceptionMapper[MalformedURLException] {

  override def toResponse(request: Request, exception: MalformedURLException): Response = {
    response.badRequest(s"Malformed URL - ${exception.getMessage}")
  }
}

