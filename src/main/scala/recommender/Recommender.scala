import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession
import recommender.Load

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.concurrent.Future

trait Recommender {
  def getRecommendationsForUser(userid: Int): Future[UserRecommendations]
}

class RecommenderRepository extends Recommender {

  def getRecommendationsForUser(userid: Int): Future[UserRecommendations] = {

    Logger.getLogger("org").setLevel(Level.ERROR)

    val spark = SparkSession
      .builder()
      .appName("GlobalRecommender")
      .master("local")
      .getOrCreate()

    import spark.implicits._

    val model = Load.getModel()

    val users = Seq(userid).toDF("userid")

    val recommendations = model
      .recommendForUserSubset(users, 10)
      .select(col = "recommendations.movieid")
      .collect()
      .toSeq

    var moviesid: Seq[Int] = Seq()
    for (rec <- recommendations){
      val recs = rec(0)
      val temp = recs.asInstanceOf[mutable.WrappedArray[Int]]
      for (r <- temp){
        moviesid = moviesid :+ r
      }
    }

    println(moviesid)

    Future.successful(UserRecommendations(userid, moviesid))

  }

}
