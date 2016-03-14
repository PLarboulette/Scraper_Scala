import com.twitter.finagle.httpx.Status
import com.twitter.finatra.http.test.EmbeddedHttpServer
import com.twitter.inject.server.FeatureTest
import perso.iadvize.Server
import perso.iadvize.database.PostsRedis
import perso.iadvize.domain.Post

/**
  * Created by pierre on 14/03/16.
  */
class PostsFilterByIDTest extends FeatureTest {

  override val server = new EmbeddedHttpServer(new Server)

  "test status /posts/:id" should {
    "get the posts identified by the id (id registered)" in {
      PostsRedis.clearDatabase()
      val post: Post = Post("1", "Content", "2014-06-06 15:28:00", "Pierre")
      PostsRedis.savePost(post)

      server.httpGet(
        path = "/posts/" + post.id,
        andExpect = Status.Ok,
        withJsonBody = "{\"posts\":[{\"id\":\"1\",\"content\":\"Content\",\"date\":\"2014-06-06 15:28:00\",\"author\":\"Pierre\"}]}"
      )
    }
  }

  "test status /posts/:id" should {
    "get the posts identified by the id (id unregistered)" in {
      server.httpGet(
        path = "/posts/DDD",
        withJsonBody = "{\n  \"Error\" : \"ID not recognized\"\n}"
      )
    }
  }

}
