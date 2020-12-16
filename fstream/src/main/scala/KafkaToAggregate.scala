import com.fasterxml.jackson.databind.JsonNode
import com.flipkart.fdsg.fstream.config.annotations.{Dimension, Increment, TimePartitions, TimeStamp}
import com.flipkart.fstream.fdp.schema.FDPType
import com.flipkart.fstream.processor.df.FStreamDataFlow
import com.flipkart.fstream.processor.jobs.FlipkartFStreamSparkJob

import scala.annotation.meta.field

object KafkaToAggregate extends FlipkartFStreamSparkJob[FDPObject] {

  override def prepareExecutionFlow(fStreamDataFlow: FStreamDataFlow[FDPObject]): Unit = {
        fStreamDataFlow.map[AggFactTest](
          payload => {
            AggFactTest("abc", 1L, payload.eventTime)
          }, "testAggFact"
        ).aggregate("putAggregate")
  }
}
@TimePartitions(values = Array("daily"))
case class AggFactTest(
                        @(Dimension@field)(name = "field1") field1: String,
                        @(Increment@field)(name = "field2")field2: Long,
                        @(TimeStamp@field) ts: Long
                      )

case class FDPObject(ingestedAt: Long,
                     eventId: String,
                     eventTime: Long,
                     schemaVersion: String,
                     test: Option[String]) extends FDPType



//sudo -u hdfs /var/lib/fk-pf-spark/bin/spark-submit --class com.flipkart.fstream.cdm.order.ForwardUnitFactJob --name "test_job" --master yarn-cluster --driver-memory 2g --executor-memory 1024m --conf spark.executor.cores=1 --conf spark.yarn.executor.memoryOverhead=1024 --conf spark.yarn.driver.memoryOverhead=1024 --conf spark.jars=/grid/1/fstream/lib/1.5/fstream.jar /grid/1/fstream/consumers/test-fstream.jar Prod KafkaToAggregate.yml