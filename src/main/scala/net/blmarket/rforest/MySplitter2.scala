package net.blmarket.rforest

import org.apache.spark.SparkContext._
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.rdd.RDD
import scala.util.Random

/**
 * Works same with MySplitter, with much less MR tasks
 */
object MySplitter2 {
  def split(data: RDD[LabeledPoint]) {
    val feature_length = data.first().features.length
    val f_idx = (0 until feature_length).toArray

    def randSplit: Splitter = {
      val pick = Random.nextInt(f_idx.length)
      val kv = data.keyBy(_.features(pick)).cache()

      // FIXME: if we have last() func, we don't need to do same sort twice.
      val min_feature = kv.sortByKey(true).first()._1
      val max_feature = kv.sortByKey(false).first()._1

      val split_val = Random.nextDouble() * (max_feature - min_feature) + min_feature

      Splitter(a => a(pick) >= split_val)
    }

    val splits = Seq.fill(3)(randSplit)
    val splitted = data.map(x => {
      ( splits.map(_.func(x.features)), x.label )
    })

    Gini.parimpurity(splitted)
  }
}

