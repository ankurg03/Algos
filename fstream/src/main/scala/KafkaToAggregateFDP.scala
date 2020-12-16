import com.flipkart.fdsg.fstream.config.annotations.{Dimension, Increment, TimePartitions, TimeStamp}
import com.flipkart.fstream.processor.df.FStreamDataFlow
import com.flipkart.fstream.processor.jobs.FlipkartFStreamSparkJob

import scala.annotation.meta.field

object KafkaToAggregateFDP extends FlipkartFStreamSparkJob[FDPPayload2] {

  override def prepareExecutionFlow(fStreamDataFlow: FStreamDataFlow[FDPPayload2]): Unit = {
        fStreamDataFlow.map[AggFactTest2](
          payload => {
            AggFactTest2("INC", 1L, 1596795971000L)
          }, "testAggFact"
        ).aggregate("putAggregate")
  }
}
@TimePartitions(values = Array("daily"))
case class AggFactTest2(
                        @(Dimension@field)(name = "field1") field1: String,
                        @(Increment@field)(name = "field2")field2: Long,
                        @(TimeStamp@field) ts: Long
                      )

case class FDPPayload2(data: Data2,
                      updatedAt: Long,
                      schemaVersion: String,
                      ingestedAt: Long,
                      test: Option[String])

case class Data2(key: String, value: String)

//sudo -u hdfs /var/lib/fk-pf-spark/bin/spark-submit --class com.flipkart.fstream.cdm.order.ForwardUnitFactJob --name "test_job" --master yarn-cluster --driver-memory 2g --executor-memory 1024m --conf spark.executor.cores=1 --conf spark.yarn.executor.memoryOverhead=1024 --conf spark.yarn.driver.memoryOverhead=1024 --conf spark.jars=/grid/1/fstream/lib/1.5/fstream.jar /grid/1/fstream/consumers/test-fstream.jar Prod KafkaToAggregate.yml