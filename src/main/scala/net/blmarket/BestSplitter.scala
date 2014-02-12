package net.blmarket

import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.rdd.RDD
import scala.util.Random

object BestSplitter {
  def split(data: RDD[LabeledPoint]): Unit = {
    val feature_length = data.first().features.length
    val f_idx = (0 until feature_length).toArray

    for(i <- 1 to 10) {
      val idx2 = Random.shuffle(f_idx)
    }

    println(f_idx.toList)
  }
}
