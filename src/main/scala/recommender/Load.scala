package recommender

import org.apache.spark.ml.recommendation.ALSModel

trait LoadModule {
  def getModel(): ALSModel
}

object Load extends LoadModule {

  override def getModel() = ALSModel.load("./moviemodel")

}
