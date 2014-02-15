package net.blmarket.rforest

import org.apache.spark.rdd.RDD

/**
 * Created by blmarket on 2014. 2. 15..
 */
object TrainErrorEstimation {
  def estimateError(data: RDD[ClassedPoint], tree: DecisionTree) {
    val labelAndPreds = data.map {
      point =>
        val prediction = tree.predict(point.features)
        (point.label, prediction)
    }

    val totalCount = labelAndPreds.count()
    val failCount = labelAndPreds.filter(_._2.isEmpty).count()

    val normalCount = totalCount - failCount

    val good = labelAndPreds.filter(r => r._2.isDefined && r._1 == r._2.get).count()

    println(tree)
    println("Total Count = " + totalCount)
    println("Fail to predict = " + failCount)
    println("Error rate = " + (1.0 - (good.toDouble / normalCount)))
  }
}
