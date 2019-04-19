package com.hpe.utils

import java.util.Properties

import org.apache.spark.sql.types.{IntegerType, StringType, StructField, StructType}
import org.apache.spark.sql.{Row, SQLContext, SaveMode}
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable.ListBuffer

object DBFriend {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
    conf.setAppName("friendSQL")
    conf.setMaster("local[*]")

    val sc = new SparkContext(conf)
    val SQLContext = new SQLContext(sc)

    var df = SQLContext.read.format("jdbc")
      .option("url", "jdbc:mysql://localhost:3306/hadoop?serverTimezone=UTC&useUnicode=true&characterEncoding=utf8&useSSL=false")
      .option("driver", "com.mysql.cj.jdbc.Driver")
      .option("dbtable", "user")
      .option("user", "root")
      .option("password", "root")
      .load()

    // 把RDD[ROW]变为(该用户，好友List)
    val result = df.rdd.flatMap(x => {
      val strs = x.getString(6).split(",")
      var buffer = new ListBuffer[(String, Int)]
      for (index <- 0 until strs.length) {
        buffer.+=((compareTo(x.getString(1), strs(index)), 0))
        if (index < strs.length - 1) {
          for (i <- index + 1 until strs.length) {
            buffer.+=((compareTo(strs(i), strs(index)), 1))
          }
        }
      }
      buffer
    }).groupByKey().filter(!_._2.toList.contains(0)).map(x => {
      val iterator = x._2.iterator
      var sum = 0
      while (iterator.hasNext) sum += iterator.next()
      (sum, x._1)
    }).sortByKey()


    // rdd to dataframe
    val schemaString = "number:Integer relation:String"
    val rowRDD = result.map(p => {
      Row(p._1.toInt, p._2)
    })
    val schema = StructType(schemaString.split(" ").map(fieldName => {
      StructField(fieldName.split(":")(0),
        if (fieldName.split(":")(1).equals("String")) StringType else IntegerType,
        true)
    })
    )
    val peopleDataFrame = SQLContext.createDataFrame(rowRDD, schema)
    peopleDataFrame.printSchema()

    val connectionProperties2 = new Properties()
    connectionProperties2.setProperty("user", "root"); // 设置用户名
    connectionProperties2.setProperty("password", "root"); // 设置密码

    peopleDataFrame.write.mode(SaveMode.Overwrite).jdbc("jdbc:mysql://localhost:3306/hadoop?serverTimezone=UTC&useUnicode=true&characterEncoding=utf8&useSSL=false", "rela", connectionProperties2)
    sc.stop()
  }

  def compareTo(name1: String, name2: String) = {
    val c = name1.compareTo(name2)
    if (c > 0) name1 + "_" + name2
    else name2 + "_" + name1
  }
}
