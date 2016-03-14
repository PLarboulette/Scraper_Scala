import com.twitter.finagle.httpx.Status
import com.twitter.finatra.http.test.EmbeddedHttpServer
import com.twitter.inject.server.FeatureTest
import perso.iadvize.Server
import perso.iadvize.database.PostsRedis
import perso.iadvize.services.PostsService

/**
  * Created by pierre on 14/03/16.
  */
class PostsNoFilterTest extends FeatureTest {

  override val server = new EmbeddedHttpServer(new Server)


  "test status /posts" should {
    new PostsService().scraping()
    " get a status 200" in {
      server.httpGet(
        path = "/posts",
        andExpect = Status.Ok
      )
    }
  }

  "test status /posts" should {
    new PostsService().scraping()
    " list VDM posts (200 items)" in {
      server.httpGet(
        path = "/posts",
        andExpect = Status.Ok
      )
    }
    PostsRedis.getPosts().size should be(200)
  }

  "test status /posts" should {
    PostsRedis.clearDatabase()
    " list VDM posts (no items)" in {
      server.httpGet(
        path = "/posts",
        andExpect = Status.Ok
      )
    }
    PostsRedis.getPosts().size should be(0)
  }

  "test status /posts" should {
    "list VDM posts (no items) and get back JSON" in {
      PostsRedis.clearDatabase()
      server.httpGet(
        path = "/posts",
        andExpect = Status.Ok,
        withJsonBody = "{\"posts\":[],\"count\":0}"
      )
    }
  }
}
