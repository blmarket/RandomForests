package net.blmarket

import org.apache.spark.SparkContext
import org.apache.spark.storage.StorageLevel

/**
 * Created by blmarket on 2014. 2. 12..
 */
object TestTree {
  def parseDouble(x: String): Double = {
    x match {
      case "NA" => Double.NaN
      case x => x.toDouble
    }
  }

  def main(args: Array[String]) {
    System.setProperty("spark.executor.memory", "1500m")
    val sc = new SparkContext("local[3]", "SparkCSV")
    val train = sc.textFile("train_v2.csv")
    val fields = train.first().split(",")

    def splitData(datum: Array[String]): (Double, Array[Double]) = {
      val loss: Int = if (parseDouble(datum.last) > 5.0) 1 else 0
      val others = datum.init.map(y => parseDouble(y))

      (loss, others)
    }

    val data = train.filter(x => !x.startsWith("id")).map(x => splitData(x.split(",")))
    println(data.first())
    // println(train.count)
    // println(fields.first())
    // println(data.count)
  }
}
