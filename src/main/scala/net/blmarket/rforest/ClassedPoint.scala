package net.blmarket.rforest

case class ClassedPoint(id: Long, label: Int, features: Array[Double]) {
  override def toString: String = {
    "ClassedPoint(%s, %s)".format(label, features.mkString("[", ", ", "]"))
  }
}
