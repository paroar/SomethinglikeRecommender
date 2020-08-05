import akka.http.scaladsl.server.{Directives, Route}

trait Router {
  def route: Route
}

class RecommenderRouter(recommender: RecommenderRepository) extends Router with Directives with MoviesDirectives with ValidatorDirectives{
  import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
  import io.circe.generic.auto._

  override def route: Route = path("recommend" / IntNumber) { (userid: Int) =>
    get {
      validateWith(CheckUserValidator)(userid) {
        handleWithGeneric(recommender.getRecommendationsForUser(userid)) { rec =>
          complete(rec)
        }
      }
    }
  } ~ path("global") {
    withoutRequestTimeout{
      post{
        entity(as[CreateRating]) { createRating =>
          validateWith(CreateUserRating)(createRating) {
            println("training")
            MovieModel.trainModel()
            handleWithGeneric(recommender.getRecommendationsForUser(0)) { todos =>
              val db = new DB
              db.deleteRating(createRating)
              complete(todos)
            }
          }
        }
      }
    }
  }
}
