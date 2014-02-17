package net.blmarket.rforest

import org.apache.spark.SparkContext._
import org.apache.spark.rdd.RDD

object TreeBuilder {
  def build(data: RDD[ClassedPoint], leftDepth: Int): DecisionTree = {
    val cnt = data.count()
    if (cnt == 0)
      DecisionTreeLeaf(None)
    else {
      if (leftDepth == 0) {
        val sum = data.map(_.label.toLong).reduce(_+_)

        DecisionTreeLeaf(Some(sum.toDouble / cnt))
      } else {
        val bestSplit = MySplitter2.split(data)
        val children = data.map(x => (bestSplit.func(x.features), x))
        val leftChild = children.filter(_._1.getOrElse(false)).map(_._2)
        val rightChild = children.filter(_._1.getOrElse(true) == false).map(_._2)

        DecisionTreeNode(bestSplit, build(leftChild, leftDepth - 1), build(rightChild, leftDepth - 1))
      }
    }
  }
}

