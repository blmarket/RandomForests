package net.blmarket.rforest

import org.apache.spark.SparkContext._
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.rdd.RDD
import scala.util.Random
import scala.None

case class Splitter(func: (Array[Double] => Option[Boolean]))

/*
object MySplitter {
  // Remind, calculating variance of unnormalized data is useless.
  def calcVariance(data: RDD[Double]): Double = {
    val sum = data.reduce((a, b) => a + b)
    val sqsum = data.map(x => x * x).reduce((a, b) => a + b)

    sum * sum - sqsum
  }

  def split(data: RDD[LabeledPoint]): (Splitter, RDD[(Boolean, LabeledPoint)]) = {
    val feature_length = data.first().features.length
    val f_idx = (0 until feature_length).toArray

    def randSplit: Splitter = {
      val pick = Random.nextInt(f_idx.length)
      val kv = data.keyBy(_.features(pick)).cache()

      // FIXME: if we have last() func, we don't need to do same sort twice.
      val min_feature = kv.sortByKey(true).first()._1
      val max_feature = kv.sortByKey(false).first()._1

      val split_val = Random.nextDouble() * (max_feature - min_feature) + min_feature

      Splitter(a => {
        val v = a(pick)
        if (v.isNaN) None else Some(v >= split_val)
      })
    }

    def trysplit = {
      val splitFunc = randSplit
      val splits = data.keyBy(x => splitFunc.func(x.features))

      (Gini.impurity(splits), splitFunc, splits)
    }

    val minsplit = (1 to 10).map(_ => trysplit).minBy(_._1)
    println(minsplit)
    (minsplit._2, minsplit._3)
  }
}
*/
