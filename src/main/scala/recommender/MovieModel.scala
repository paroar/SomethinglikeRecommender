import org.apache.spark.sql.SparkSession
import org.apache.spark.ml.recommendation.ALS
import org.apache.log4j.{Level, Logger}


object MovieModel{

  def trainModel() = {

    Logger.getLogger("org").setLevel(Level.ERROR)

    val spark = SparkSession
      .builder
      .appName("ALS")
      .master("local[*]")
      .getOrCreate()

    val ratings = spark.read
      .format("jdbc")
      .option("url", "jdbc:postgresql://postgres:5432/movielens")
      .option("dbtable", "ratings")
      .option("user", "postgres")
      .option("password", "password")
      .load()

    val als = new ALS()
      .setMaxIter(5)
      .setRegParam(0.01)
      .setImplicitPrefs(true)
      .setUserCol("userid")
      .setItemCol("movieid")
      .setRatingCol("rating")

    val model = als.fit(ratings)

    model.setColdStartStrategy("drop")

    model.write.overwrite().save(s"./moviemodel")

    println("model saved")
  }
}
