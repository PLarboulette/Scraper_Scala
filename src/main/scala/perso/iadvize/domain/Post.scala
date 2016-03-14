package perso.iadvize.domain

/**
  * Created by pierre on 12/03/16.
  */
case class Post (
  id : String,
  content : String,
  date : String,
  author : String
                ) extends Ordered[Post] {


  def compare(that: Post): Int = (that.date) compare (this.date)

}

