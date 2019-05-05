package example

import org.apache.spark.sql.functions._
import org.apache.spark.sql.SparkSession

import org.json4s.DefaultFormats
import org.json4s.jackson.JsonMethods.parse

import example.model.{LocationMessage, MetadataMessage}

object Hello {

  val topic = "vessels/#"

  val brokerUrl = "wss://meri.digitraffic.fi:61619/mqtt"

  def main(args: Array[String]) {

    val spark = SparkSession.builder
      .master("local[*]")
      .appName("Simple Application")
      .getOrCreate()

    spark.sparkContext
      .setLogLevel("ERROR")

    import spark.implicits._

    val lines = spark.readStream
      .format("org.apache.bahir.sql.streaming.mqtt.MQTTStreamSourceProvider")
      .option("topic", topic)
      .option("username", "digitraffic")
      .option("password", "digitrafficPassword")
      .option("cleanSession", true)
      .option("persistence", "memory")
      .load(brokerUrl)
      .selectExpr(
        "CAST(topic AS STRING)",
        "CAST(payload AS STRING)"
      )
      .as[(String, String)]
      .map { row =>
        (row._1.split("/").last, row._2)
      }

    val locations = lines
      .filter(_._1 == "locations")
      .map { row =>
        implicit val format = DefaultFormats
        parse(row._2).extract[LocationMessage]
      }

    val metadata = lines
      .filter(_._1 == "metadata")
      .map { row =>
        implicit val format = DefaultFormats
        parse(row._2).extract[MetadataMessage]
      }

    val locationQuery = locations.writeStream
      .outputMode("append")
      .format("console")
      //.option("truncate", false)
      .start

    val query = metadata.writeStream
      .outputMode("append")
      .format("console")
      //.option("truncate", false)
      .start

    spark.streams.awaitAnyTermination
  }
}
