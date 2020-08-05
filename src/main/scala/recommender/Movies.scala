case class Movie(movieid: Int, title: String, recommendations: Seq[String])
case class UserRecommendations(userid: Int, recommendations: Seq[Int])
case class CreateRating(userid: Int, movieid: Int, rating: Float)