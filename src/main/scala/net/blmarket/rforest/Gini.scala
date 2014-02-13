package net.blmarket.rforest

import org.apache.spark.SparkContext._
import org.apache.spark.rdd.RDD
import org.apache.spark.mllib.regression.LabeledPoint

object Gini {
  /**
   * Calculates Gini impurity.
   *
   * @param splits Splitted RDD with Boolean label.
   * @return Gini impurity value
   */
  def impurity(splits: RDD[(Boolean, LabeledPoint)]): Double = {
    val r1 = splits.groupByKey().map(split => {
      // one child.
      val groupsum = split._2.groupBy(_.label).values.foldLeft(0.0)((a, b) => a + b.size.toDouble * b.size) // split using labels.
      val sz = split._2.size
      val gini = 1.0 - groupsum / sz / sz
      (sz, gini * sz)
    })
    val total = r1.reduce((a, b) => (a._1 + b._1, a._2 + b._2))
    total._2 / total._1
  }
}


