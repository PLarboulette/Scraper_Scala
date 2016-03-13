package perso.iadvize.database

import com.mongodb.async.client.MongoCollection
import org.mongodb.scala
import org.mongodb.scala.{Document, MongoDatabase, MongoClient}
import perso.iadvize.domain.Post

/**
  * Created by pierre on 12/03/16.
  */
object PostsMongo {


  // Use a Connection String
  val mongoClient: MongoClient = MongoClient()
  val database: MongoDatabase = mongoClient.getDatabase("iAdvize")




  def savePost (post : Post) : Unit = {
    val collection: scala.MongoCollection[Document] = database.getCollection("posts")
    collection.insertOne(Document("_id" -> post.id, "author" -> post.author, "date" -> post.date, "content" -> post.content))

    println("OK")

    mongoClient.close()
  }

}
