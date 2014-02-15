package net.blmarket

import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.SparkContext
import net.blmarket.rforest.{TreeBuilder, MySplitter}
import org.apache.spark.rdd.RDD

object TestTree {
  final val MAX_DEPTH: Int = 1

  def trainAndTest(data: RDD[LabeledPoint]) {
    MySplitter.split(data)

    val tree = TreeBuilder.build(data, MAX_DEPTH)

    val labelAndPreds = data.map {
      point =>
        val prediction = tree.predict(point.features)
        (point.label, prediction)
    }

    val trainErr = labelAndPreds.filter(r => r._1 != r._2).count.toDouble / data.count
    println("Training Error = " + trainErr)
    println(tree)
  }

  def processLoan(file: String) {
    def parseDouble(x: String): Double = {
      x match {
        case "NA" => Double.NaN
        case x => x.toDouble
      }
    }

    val sc = new SparkContext("local", "SparkCSV", "", List("target/scala-2.10/randomforests_2.10-0.1.0.jar"))
    val train = sc.textFile(file)

    def splitData(datum: Array[String]): LabeledPoint = {
      val loss: Double = if (parseDouble(datum.last) > 5.0) 1.0 else 0.0
      val others = datum.init.map(parseDouble(_))

      LabeledPoint(loss, others)
    }

    val data = train.map(x => splitData(x.split(","))).persist()
    trainAndTest(data)
  }

  def main(args: Array[String]) {
    System.setProperty("spark.executor.memory", "1g")

    println("Usage: [file_path]")
    processLoan("train0.csv")
  }
}
