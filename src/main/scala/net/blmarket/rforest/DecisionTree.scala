package net.blmarket.rforest

abstract class DecisionTree { def predict(features: Array[Double]): Double }

/**
 * Internal node in Decision Tree, and has two children.
 * @param split splitter partial function. for given Features, it gives T/F classification
 * @param left world of TRUE(when you got TRUE from split func, you go here)
 * @param right world of FALSE
 */
case class DecisionTreeNode(split: Splitter, left: DecisionTree, right: DecisionTree) extends DecisionTree {
  override def predict(features: Array[Double]): Double = split.func(features) match {
    case None => -1.0
    case Some(true) => left.predict(features)
    case Some(false) => right.predict(features)
  }
}

case class DecisionTreeLeaf(label: Double) extends DecisionTree {
  override def predict(features: Array[Double]): Double = label
}

