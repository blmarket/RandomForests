package net.blmarket.rforest

import org.apache.spark.rdd.RDD

/**
 * Build Multiple Trees
 *
 * Created by blmarket on 2014. 2. 15..
 */
case class RandomForests(val trees: List[DecisionTree]) extends Predictor {
  override def predict(features: Array[Double]): Option[Double] = {
    val goodTrees = trees.map(_.predict(features)).filter(_.isDefined).map(_.get)
    val len = goodTrees.length

    if(len == 0) None else Some(goodTrees.sum / len)
  }
}

object RandomForests {
  final val MAX_DEPTH: Int = 4

  def createRandomForests(data: RDD[ClassedPoint], maxDepth: Int = MAX_DEPTH): RandomForests = {
    val trees = Seq.fill(20)(TreeBuilder.build(data, maxDepth))
    RandomForests(trees.toList)
  }
}

