trait Validator[T] {
  def validate(t: T): Option[ApiError]
}

object CheckUserValidator extends Validator[Int] {

  def validate(userid: Int): Option[ApiError] = {
    val db = new DB
    if (!db.userHasRatings(userid))
      Some(ApiError.todoNotFound(userid))
    else
      None
  }
}

object CreateUserRating extends Validator[CreateRating] {

  def validate(createRating: CreateRating): Option[ApiError] = {
    val db = new DB
    db.insertRating(createRating)
    if (!db.userHasRatings(createRating.userid))
      Some(ApiError.todoNotFound(createRating.userid))
    else
      None
  }
}