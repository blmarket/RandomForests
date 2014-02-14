package net.blmarket.rforest

import org.apache.spark.SparkContext._
import org.apache.spark.rdd.RDD
import org.apache.spark.mllib.regression.LabeledPoint

object TreeBuilder {
  def build(data: RDD[LabeledPoint], leftDepth: Int): DecisionTree = {
    if (leftDepth == 0) {
      val arr = data.groupBy(_.label).mapValues(x => x.size).collect()
      val label = if(arr.length == 0)
        0
      else if(arr.length == 1 || arr(0)._2 > arr(1)._2)
        arr(0)._1
      else
        arr(1)._1
      DecisionTreeLeaf(label)
    } else {
      val (split, childs) = MySplitter.split(data)

      val child1 = childs.filter(_._1).map(_._2)
      val child2 = childs.filter(!_._1).map(_._2)

      DecisionTreeNode(split, build(child1, leftDepth - 1), build(child2, leftDepth - 1))
    }
  }
}

