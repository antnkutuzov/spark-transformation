package com.epam.bigdata

import java.util

import org.apache.spark.sql.types.{IntegerType, StructField, StructType}
import org.apache.spark.sql.{SparkSession, _}
import org.junit.runner.RunWith
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class TransformationTaskTest extends AnyFunSuite {

  test("Test transformation expedia data") {
    val sparkSession = SparkSession.builder()
      .master("local")
      .appName("ExpediaTransformationTest")
      .config("spark.driver.memory", "471859200")
      .getOrCreate()

    val result = TransformationTask.addColumnLargeFamily(expediaData(sparkSession))
    assertResult(result.columns)(Seq("hotel_id", "srch_children_cnt", "large_family"))
    assert(result.count() == 3)
    assert(result.take(3).containsSlice(Seq(
      Row(1, 1, false),
      Row(2, 3, true),
      Row(4, 2, false)
    )))
  }

  private def expediaData(sparkSession: SparkSession) = {
    val rows = util.Arrays.asList(
      Row(1, 1),
      Row(2, 3),
      Row(3, 0),
      Row(4, 2)
    )
    val schema = StructType(Seq(StructField("hotel_id", IntegerType), StructField("srch_children_cnt", IntegerType)))
    sparkSession.createDataFrame(rows, schema)
  }
}
