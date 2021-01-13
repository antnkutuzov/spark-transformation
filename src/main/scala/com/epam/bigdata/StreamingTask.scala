package com.epam.bigdata

import org.apache.spark.sql._
import org.apache.spark.sql.functions.col

object StreamingTask {

  def main(args: Array[String]): Unit = {

    val sparkSession = SparkSession.builder()
      .master("local[*]")
      .appName("ExpediaTransformation")
      .getOrCreate()

    val expediaData = readExpediaData(sparkSession)
    val filteredExpedia = addColumnLargeFamily(expediaData)

    filteredExpedia
      .write
      .format("avro")
      .save("hdfs://host.docker.internal:9000/transformExpedia")
  }

  private def readExpediaData(sparkSession: SparkSession): DataFrame = {
    sparkSession
      .read
      .format("avro")
      .load("hdfs://host.docker.internal:9000/expedia")
  }

  private def addColumnLargeFamily(expediaData: DataFrame): DataFrame = {
    expediaData
      .where("srch_children_cnt > 0")
      .withColumn("large_family", col("srch_children_cnt").gt(2))
  }
}
