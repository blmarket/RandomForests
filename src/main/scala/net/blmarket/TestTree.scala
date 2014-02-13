package net.blmarket

import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.SparkContext
import net.blmarket.rforest.{TreeBuilder, MySplitter}

object TestTree {
  def processLoan() {
    def parseDouble(x: String): Double = {
      x match {
        case "NA" => Double.NaN
        case x => x.toDouble
      }
    }

    val sc = new SparkContext("local[3]", "SparkCSV")
    val train = sc.textFile("train_v2.csv")
    val fields = train.first().split(",")

    def splitData(datum: Array[String]): (Double, Array[Double]) = {
      val loss: Int = if (parseDouble(datum.last) > 5.0) 1 else 0
      val others = datum.init.map(parseDouble(_))

      (loss, others)
    }
  }

  def main(args: Array[String]) {
    System.setProperty("spark.executor.memory", "1500m")
    val sc = new SparkContext("local[3]", "SparkCSV")
    val train = sc.textFile("iris.data")
    def parseIris(datum: Array[String]) = LabeledPoint(datum.head.toDouble, datum.tail.map(_.toDouble))

    val data = train.map(x => parseIris(x.split(",")))

    MySplitter.split(data)

    val tree = TreeBuilder.build(data, 1)

    val labelAndPreds = data.map { point =>
      val prediction = tree.predict(point.features)
      (point.label, prediction)
    }

    val trainErr = labelAndPreds.filter(r => r._1 != r._2).count.toDouble / data.count
    println("Training Error = " + trainErr)
    println(tree)
  }
}
