/**
  * Created by pierre on 14/03/16.
  */

import com.twitter.finagle.httpx.Status
import com.twitter.finatra.http.test.EmbeddedHttpServer
import com.twitter.inject.server.FeatureTest
import perso.iadvize.Server
import perso.iadvize.database.PostsRedis
import perso.iadvize.domain.Post
import perso.iadvize.services.PostsService


class ServerTest extends FeatureTest {

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

  "test status /posts/:id" should {
    "get the posts identified by the id (id registered)" in {
      PostsRedis.clearDatabase()
      val post: Post = Post("1", "Content", "2014-06-06 15:28:00", "Pierre")
      PostsRedis.savePost(post)

      server.httpGet(
        path = "/posts/" + post.id,
        andExpect = Status.Ok,
        withJsonBody = "{\"posts\":{\"id\":\"1\",\"content\":\"Content\",\"date\":\"2014-06-06 15:28:00\",\"author\":\"Pierre\"}}"
      )
    }
  }

  "test status /posts/:id" should {
    "get the posts identified by the id (id unregistered)" in {
      server.httpGet(
        path = "/posts/DDD",
        andExpect = Status.Unauthorized
      )
    }
  }

  "test status /posts?author=XXX" should {
    "get the posts identified by their author" in {
      PostsRedis.clearDatabase()
      val post: Post = Post("1", "Content", "2014-06-06 15:28:00", "Pierre")
      PostsRedis.savePost(post)
      server.httpGet(
        path = "/posts?author=Pierre",
        andExpect = Status.Ok,
        withJsonBody = "{\"posts\":[{\"id\":\"1\",\"content\":\"Content\",\"date\":\"2014-06-06 15:28:00\",\"author\":\"Pierre\"}],\"count\":1}"
      )
    }

    "test status /posts?author=XXX" should {
      "get the posts identified by their author (author unregistered)" in {
        PostsRedis.clearDatabase()
        val post: Post = Post("1", "Content", "2014-06-06 15:28:00", "Pierre")
        PostsRedis.savePost(post)
        server.httpGet(
          path = "/posts?author=Vador",
          andExpect = Status.Ok,
          withJsonBody = "{\"posts\":[],\"count\":0}"
        )
      }
    }

  }


}
