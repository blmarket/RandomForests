package net.blmarket

import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.SparkContext
import net.blmarket.rforest.{TreeBuilder, MySplitter}
import org.apache.spark.rdd.RDD

object TestTree {
  def trainAndTest(data: RDD[LabeledPoint]) {
    MySplitter.split(data)

    val tree = TreeBuilder.build(data, 1)

    val labelAndPreds = data.map {
      point =>
        val prediction = tree.predict(point.features)
        (point.label, prediction)
    }

    val trainErr = labelAndPreds.filter(r => r._1 != r._2).count.toDouble / data.count
    println("Training Error = " + trainErr)
    println(tree)
  }

  def processLoan() {
    def parseDouble(x: String): Double = {
      x match {
        case "NA" => Double.NaN
        case x => x.toDouble
      }
    }

    val sc = new SparkContext("local[3]", "SparkCSV")
    val train = sc.textFile("train0.csv")

    def splitData(datum: Array[String]): LabeledPoint = {
      val loss: Double = if (parseDouble(datum.last) > 5.0) 1.0 else 0.0
      val others = datum.init.map(parseDouble(_))

      LabeledPoint(loss, others)
    }

    val data = train.map(x => splitData(x.split(",")))
    trainAndTest(data)
  }

  def testIris() {
    val sc = new SparkContext("local[3]", "SparkCSV")
    val train = sc.textFile("iris.data")
    def parseIris(datum: Array[String]) = LabeledPoint(datum.head.toDouble, datum.tail.map(_.toDouble))

    val data = train.map(x => parseIris(x.split(",")))

    trainAndTest(data)
  }

  def main(args: Array[String]) {
    System.setProperty("spark.executor.memory", "1500m")

    processLoan()
  }
}
