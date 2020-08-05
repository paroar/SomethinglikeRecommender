import doobie._
import doobie.implicits._
import doobie.util.ExecutionContexts
import cats.effect._

object DB {

  def connection() = {

    implicit val cs = IO.contextShift(ExecutionContexts.synchronous)

    val xa = Transactor.fromDriverManager[IO](
    "org.postgresql.Driver",
    "jdbc:postgresql://postgres:5432/movielens",
    "postgres",
    "password",
    Blocker.liftExecutionContext(ExecutionContexts.synchronous)
    )
    xa
  }

}

class DB {

  def getMovieNames(): Map[Int, String] = {
    sql"select movieid, title from movies"
      .query[(Int,String)]
      .to[List]
      .transact(DB.connection)
      .unsafeRunSync
      .toMap
  }

  def userHasRatings(userid: Int): Boolean = {
    val exists = sql"select exists(select 1 from ratings where userid=$userid)"
      .query[Boolean]
      .to[List]
      .transact(DB.connection)
      .unsafeRunSync

    exists(0)
  }

  def insertRating(createRating: CreateRating) = {
    val timestamp = System.nanoTime()
    sql"insert into ratings(userid, movieid, rating, timestamp) values(${createRating.userid}, ${createRating.movieid}, ${createRating.rating}, $timestamp)"
      .update
      .run
      .transact(DB.connection)
      .unsafeRunSync
  }

  def deleteRating(createRating: CreateRating) = {
    sql"delete from ratings where userid=${createRating.userid}"
      .update
      .run
      .transact(DB.connection)
      .unsafeRunSync
  }

}
