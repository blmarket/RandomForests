package net.blmarket

import org.scalatest.FunSuite
import org.apache.spark.SparkContext
import org.apache.spark.mllib.regression.LabeledPoint
import net.blmarket.rforest.{TrainErrorEstimation, TreeBuilder, ClassedPoint, MySplitter2}

class SplitterFunSuite extends FunSuite {
  test("Splitter works") {
    val sc = new SparkContext("local", "test")
    val train = sc.textFile("iris.data")

    def parseIris(datum: Array[String]) = ClassedPoint(datum.head.toInt, datum.tail.map(_.toDouble))

    val data = train.map(x => parseIris(x.split(",")))
    val tree = TreeBuilder.build(data, 3)

    TrainErrorEstimation.estimateError(data, tree)
  }
}
