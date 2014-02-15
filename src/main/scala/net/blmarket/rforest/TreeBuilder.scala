package net.blmarket.rforest

import org.apache.spark.SparkContext._
import org.apache.spark.rdd.RDD
import org.apache.spark.mllib.regression.LabeledPoint

object TreeBuilder {
  def build(data: RDD[ClassedPoint], leftDepth: Int): DecisionTree = {
    if (leftDepth == 0) {
      // FIXME: rather use probability than select only one.
      val arr = data.groupBy(_.label).mapValues(x => x.size).collect()
      val label = if(arr.length == 0)
        0
      else if(arr.length == 1 || arr(0)._2 > arr(1)._2)
        arr(0)._1
      else
        arr(1)._1
      DecisionTreeLeaf(label)
    } else {
      val bestSplit = MySplitter2.split(data)
      val children = data.map(x => (bestSplit.func(x.features), x))
      val leftChild = children.filter(_._1.getOrElse(false)).map(_._2)
      val rightChild = children.filter(_._1.getOrElse(true) == false).map(_._2)

      DecisionTreeNode(bestSplit, build(leftChild, leftDepth - 1), build(rightChild, leftDepth - 1))
    }
  }
}

