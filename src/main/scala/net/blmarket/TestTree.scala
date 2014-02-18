package net.blmarket

import org.apache.spark.SparkContext
import net.blmarket.rforest.{RandomForests, TrainErrorEstimation, ClassedPoint, TreeBuilder}
import org.apache.spark.rdd.RDD

object TestTree {
  final val MAX_DEPTH: Int = 4
  final val PREFIX: String = ""

  def processLoan(trainFile: String, testFile: String, classCut: Int) {
    def parseDouble(x: String): Double = {
      x match {
        case "NA" => Double.NaN
        case x => x.toDouble
      }
    }

    val sc = new SparkContext("spark://ec2-176-34-3-175.ap-northeast-1.compute.amazonaws.com", "SparkCSV", "/root/spark", List("target/scala-2.10/randomforests_2.10-0.1.0.jar"))
    val train = sc.textFile(trainFile)

    def splitData(datum: Array[String]): ClassedPoint = {
      val id = datum.head.toLong
      val loss: Int = if (datum.last.toInt >= classCut) 1 else 0
      val others = datum.init.tail.map(parseDouble(_))

      ClassedPoint(id, loss, others)
    }

    def parseTest(datum: Array[String]): (Long, Array[Double]) = {
      val id = datum.head.toLong
      val features = datum.tail.map(parseDouble(_))

      (id, features)
    }

    val data = train.map(x => splitData(x.split(","))).cache()

    val rf = RandomForests.createRandomForests(data, MAX_DEPTH)
    TrainErrorEstimation.estimateError(data, rf)

    // data.map(x => (x.id, x.label, rf.predict(x.features).getOrElse(-1.0))).saveAsTextFile("result.txt")

    val testData = sc.textFile(testFile).map(x => parseTest(x.split(",")))
    val predictResult = testData.map(x => (x._1, rf.predict(x._2)))

    predictResult.saveAsTextFile(PREFIX + "result" + System.currentTimeMillis())
  }

  def main(args: Array[String]) {
    System.setProperty("spark.executor.memory", "4g")

    println("Usage: [file_path]")
    processLoan(PREFIX + args(0), PREFIX + args(1), args(2).toInt)
  }
}
