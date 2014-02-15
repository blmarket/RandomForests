package net.blmarket.rforest

import org.apache.spark.SparkContext._
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.rdd.RDD
import scala.util.Random

/**
 * Works same with MySplitter, with much less MR tasks
 */
object MySplitter2 {
  final val TRY_SPLIT: Int = 30

  def split(data: RDD[ClassedPoint]): Splitter = {
    val feature_length = data.first().features.length
    val f_idx = (0 until feature_length).toArray

    val minmax = data.map(x => x.features.map(y => (y,y))).reduce((x, y) => {
      x.zip(y).map(u => {
        def minn(a: Double, b: Double) = if(a.isNaN) b else if (b.isNaN) a else Math.min(a,b)
        def maxx(a: Double, b: Double) = if(a.isNaN) b else if (b.isNaN) a else Math.max(a,b)
        (minn(u._1._1, u._2._1), maxx(u._1._2, u._2._2))
      })
    })

    def randSplit: Splitter = {
      val pick = Random.nextInt(f_idx.length)
      val min_feature = minmax(pick)._1
      val max_feature = minmax(pick)._2

      val split_val = Random.nextDouble() * (max_feature - min_feature) + min_feature

      Splitter(a => {
        val v = a(pick)
        if (v.isNaN) None else Some(v >= split_val)
      })
    }

    val splits = Seq.fill(TRY_SPLIT)(randSplit)
    val splitted = data.map(x => {
      (splits.map(_.func(x.features)), x.label)
    })

    val scores = Gini.parimpurity(splitted)
    scores.zip(splits).minBy(_._1)._2
  }
}

