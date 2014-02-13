package net.blmarket

import org.apache.spark.SparkContext._
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.rdd.RDD
import scala.util.Random

object Gini {
  def impurity(splits: RDD[(Boolean, LabeledPoint)]): Double = {
    splits.groupByKey().map(split => {
      split._2.groupBy(_.label).values
    })
    0
  }
}

object MySplitter {
  // Remind, calculating unnormalized data is useless.
  def calcVariance(data: RDD[Double]): Double = {
    val sum = data.reduce((a, b) => a + b)
    val sqsum = data.map(x => x * x).reduce((a, b) => a + b)

    sum*sum - sqsum
  }

  def split(data: RDD[LabeledPoint]): Unit = {
    val feature_length = data.first().features.length
    val f_idx = (0 until feature_length).toArray

    for (i <- 1 to 10) {
      val pick = Random.nextInt(f_idx.length)
      val kv = data.keyBy(_.features(pick))

      val min_feature = kv.sortByKey(true).first()._1
      val max_feature = kv.sortByKey(false).first()._1

      val split_val = Random.nextDouble() * (max_feature - min_feature) + min_feature

      val splits = data.keyBy(_.features(pick) >= split_val)

      println(Gini.impurity(splits))
    }
  }
}
