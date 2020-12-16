import com.flipkart.fstream.processor.df.FStreamDataFlow
import com.flipkart.fstream.processor.jobs.FlipkartFStreamSparkJob

class KafkaToKafka extends FlipkartFStreamSparkJob[FDPPayload] {

  override def prepareExecutionFlow(fStreamDataFlow: FStreamDataFlow[FDPPayload]): Unit = {
    fStreamDataFlow
      .map(payload => payload.data, "mapId")
      .writeToSink("kafkaSink")
  }
}

case class FDPPayload(data: Data,
                      updatedAt: Long,
                      schemaVersion: String,
                      ingestedAt: Long,
                      test: Option[String])

case class Data (key: String, value: String)