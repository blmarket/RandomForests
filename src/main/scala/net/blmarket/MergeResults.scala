package net.blmarket

import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import scala.collection.JavaConversions._

object MergeResults {
  def main(args: Array[String]) {
    val sc = new SparkContext("local[3]", "SparkCSV", "", List("target/scala-2.10/randomforests_2.10-0.1.0.jar"))

    val pat = new scala.util.matching.Regex("""\(([0-9]*),Some\((.*)\)\)""", "id", "value")

    val r6 = sc.textFile("result6").map(_ match { case pat(id, value) => (id, value.toDouble) })
    val r7 = sc.textFile("result7").map(_ match { case pat(id, value) => (id, value.toDouble) })

    val rr = r6.zip(r7).map(x => (x._1._1, x._1._2 + x._2._2))

    println(r6.first())
    println(r7.first())
    println(rr.first())

    // val r6 = sc.textFile("result6")
    // val r7 = sc.textFile("result7")

    // println(r6.first())
  }
}
