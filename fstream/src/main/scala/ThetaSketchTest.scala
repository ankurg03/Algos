import com.flipkart.fdsg.fstream.config.annotations.{Dimension, SetData, TimePartitions, TimeStamp}
import com.flipkart.fstream.processor.df.FStreamDataFlow
import com.flipkart.fstream.processor.jobs.FlipkartFStreamSparkJob

import scala.annotation.meta.field

object ThetaSketchTest extends FlipkartFStreamSparkJob[TestClass] {

  override def prepareExecutionFlow(fStreamDataFlow: FStreamDataFlow[TestClass]): Unit = {
        fStreamDataFlow.map[AggFactTest](
          payload => {
            AggFactTest(payload.dim, payload.sketch, payload.ts)
          }, "testAggFact"
        ).aggregate("thetaSketchAggregate")
  }
}
@TimePartitions(values = Array("daily"))
case class AggFactTest(
                        @(Dimension@field)(name = "dim") dim: String,
                        @(SetData@field)(name = "sketch")sketch: String,
                        @(TimeStamp@field) ts: Long
                      )