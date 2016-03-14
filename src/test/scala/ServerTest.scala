/**
  * Created by pierre on 14/03/16.
  */

import com.twitter.finatra.http.test.EmbeddedHttpServer
import com.twitter.inject.server.FeatureTest
import perso.iadvize.Server


class ServerTest extends FeatureTest {

  override val server = new EmbeddedHttpServer(new Server)









}
