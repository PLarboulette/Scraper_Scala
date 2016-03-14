import com.twitter.finagle.httpx.Status
import com.twitter.finatra.http.test.EmbeddedHttpServer
import com.twitter.inject.server.FeatureTest
import perso.iadvize.Server
import perso.iadvize.database.PostsRedis
import perso.iadvize.domain.Post
import perso.iadvize.services.PostsService

/**
  * Created by pierre on 14/03/16.
  */
class PostsOtherFiltersTest extends FeatureTest {

  override val server = new EmbeddedHttpServer(new Server)

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

  "test status /posts?from=XXX" should {
    "get the posts which have a date more recent than the From parameter (Error testing)" in {
      PostsRedis.clearDatabase()
      val post: Post = Post("1", "Content", "2014-06-06 15:28:00", "Pierre")
      PostsRedis.savePost(post)
      server.httpGet(
        path = "/posts?from=2016-54-322",
        withJsonBody = "{\n  \"Date\": \"Incorrect format for From input. Enter a date with the following format : YYYY-MM-DD\"\n}"
      )
    }
  }

  "test status /posts?to=XXX" should {
    "get the posts which have a older date than the To parameter (Error testing)" in {
      PostsRedis.clearDatabase()
      val post: Post = Post("1", "Content", "2014-06-06 15:28:00", "Pierre")
      PostsRedis.savePost(post)
      server.httpGet(
        path = "/posts?to=2016-54-322",
        withJsonBody = "{\n  \"Date\": \"Incorrect format for To input. Enter a date with the following format : YYYY-MM-DD\"\n}"
      )
    }
  }

  "test status /posts?author=XXX" should {
    "get the posts identified by their author (Test with several posts)" in {
      PostsRedis.clearDatabase()
      val post: Post = Post("1", "Content", "2014-06-06 15:28:00", "Pierre")
      val Post2: Post = Post("1", "Content", "2014-06-06 15:28:00", "Dark Vador")
      val Post3: Post = Post("1", "Content", "2014-06-06 15:28:00", "Luke Skywalker")
      PostsRedis.savePost(post)
      PostsRedis.savePost(Post2)
      PostsRedis.savePost(Post3)

      server.httpGet(
        path = "/posts?author=Pierre",
        andExpect = Status.Ok,
        withJsonBody = "{\"posts\":[{\"id\":\"1\",\"content\":\"Content\",\"date\":\"2014-06-06 15:28:00\",\"author\":\"Pierre\"}],\"count\":1}"
      )
      new PostsService().getPosts(Some("Pierre"), None, None).size should be(1)
    }
  }

  "test status /posts?from=XXX" should {
    "get the posts after a given date (Test with several posts)" in {
      PostsRedis.clearDatabase()
      val post: Post = Post("1", "Content", "2014-06-06 15:28:00", "Pierre")
      val Post2: Post = Post("2", "Content", "2014-06-09 15:28:00", "Dark Vador")
      val Post3: Post = Post("3", "Content", "2014-06-12 15:28:00", "Luke Skywalker")
      PostsRedis.savePost(post)
      PostsRedis.savePost(Post2)
      PostsRedis.savePost(Post3)

      server.httpGet(
        path = "/posts?from=2014-06-10",
        andExpect = Status.Ok,
        withJsonBody = "{\"posts\":[{\"id\":\"3\",\"content\":\"Content\",\"date\":\"2014-06-12 15:28:00\",\"author\":\"Luke Skywalker\"}],\"count\":1}"
      )
      new PostsService().getPosts(None, Some("2014-06-10"), None).size should be(1) // Only one after the 10
    }
  }

  "test status /posts?from=XXX" should {
    "get the posts before a given date  (Test with several posts)" in {
      PostsRedis.clearDatabase()
      val post: Post = Post("1", "Content", "2014-06-06 15:28:00", "Pierre")
      val Post2: Post = Post("2", "Content", "2014-06-09 15:28:00", "Dark Vador")
      val Post3: Post = Post("3", "Content", "2014-06-12 15:28:00", "Luke Skywalker")
      PostsRedis.savePost(post)
      PostsRedis.savePost(Post2)
      PostsRedis.savePost(Post3)

      server.httpGet(
        path = "/posts?to=2014-06-10",
        andExpect = Status.Ok,
        withJsonBody = "{\n  \"posts\" : [\n    {\n      \"id\" : \"2\",\n      \"content\" : \"Content\",\n      \"date\" : \"2014-06-09 15:28:00\",\n      \"author\" : \"Dark Vador\"\n    },\n    {\n      \"id\" : \"1\",\n      \"content\" : \"Content\",\n      \"date\" : \"2014-06-06 15:28:00\",\n      \"author\" : \"Pierre\"\n    }\n  ],\n  \"count\" : 2\n}"
      )
      new PostsService().getPosts(None, None, Some("2014-06-10")).size should be(2) // Only one after the 10 --> Or Two before
    }
  }

  "test status /posts?from=XXX&to=YYY" should {
    "get the posts before a given date  (Test with several posts)" in {
      PostsRedis.clearDatabase()
      val post: Post = Post("1", "Content", "2014-06-06 15:28:00", "Pierre")
      val Post2: Post = Post("2", "Content", "2014-06-09 15:28:00", "Dark Vador")
      val Post3: Post = Post("3", "Content", "2014-06-12 15:28:00", "Luke Skywalker")
      PostsRedis.savePost(post)
      PostsRedis.savePost(Post2)
      PostsRedis.savePost(Post3)

      server.httpGet(
        path = "/posts?from=2014-06-08&to=2014-06-11",
        andExpect = Status.Ok,
        withJsonBody = "{\"count\":1,\"posts\":[{\"author\":\"Dark Vador\",\"content\":\"Content\",\"date\":\"2014-06-09 15:28:00\",\"id\":\"2\"}]}"
      )
      new PostsService().getPosts(None, Some("2014-06-08"), Some("2014-06-11")).size should be(1) // Only one between the two dates
    }
  }
}
