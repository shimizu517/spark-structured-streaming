package shimizu517.kafkasparkstreaming.app;

import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.TimeoutException;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.sql.*;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.streaming.StreamingQuery;
import org.apache.spark.sql.streaming.StreamingQueryException;

/**
 * Hello world!
 *
 */
public class App {

  public static void main(String[] args)
    throws StreamingQueryException, TimeoutException {
    SparkSession spark = SparkSession
      .builder()
      .appName("EMRServerlessSparkExample")
      .getOrCreate();

    // Read data from an S3 bucket
    spark
      .read()
      .format("csv")
      .load("s3://test/input.csv")
      .createOrReplaceTempView("my_data");

    // Run a simple Spark SQL query
    spark.sql("SELECT COUNT(*) FROM my_data").show();

    // Write the output to an S3 bucket
    spark
      .sql("SELECT * FROM my_data")
      .write()
      .format("csv")
      .save("s3://test/output.csv");

    spark.stop();
  }
}
