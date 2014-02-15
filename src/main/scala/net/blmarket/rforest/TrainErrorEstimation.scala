package net.blmarket.rforest

import org.apache.spark.rdd.RDD

/**
 * Created by blmarket on 2014. 2. 15..
 */
object TrainErrorEstimation {
  def estimateError(data: RDD[ClassedPoint], tree: Predictor) {
    val labelAndPreds = data.map {
      point =>
        val prediction: Int = tree.predict(point.features) match {
          case None => -1
          case Some(p) =>
            if (p < 0.5) 0 else 1
        }
        (point.label, prediction)
    }

    val totalCount = labelAndPreds.count()
    val failCount = labelAndPreds.filter(_._2 < 0).count()

    val normalCount = totalCount - failCount

    val good = labelAndPreds.filter(r => r._1 == r._2).count()

    println(tree)
    println("Total Count = " + totalCount)
    println("Fail to predict = " + failCount)
    println("Error rate = " + (1.0 - (good.toDouble / normalCount)))

    val predxy = (x:Int, y:Int) => labelAndPreds.filter(v => v._1 == x && v._2 == y).count()
    println("Label 0, Pred 0 : " + predxy(0, 0))
    println("Label 0, Pred 1 : " + predxy(0, 1))
    println("Label 1, Pred 0 : " + predxy(1, 0))
    println("Label 1, Pred 1 : " + predxy(1, 1))
  }
}
