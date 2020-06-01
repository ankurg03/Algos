import com.flipkart.fdsg.fstream.utils.HbaseUtility.makeHashedRowKey
import com.flipkart.fdsg.fstream.utils.JsonUtility._
import com.flipkart.fdsg.fstream.utils.types.FStreamDataTypes.{ColumnFamily, ColumnQualifier, ColumnValue}
import com.flipkart.fstream.integration.test.ifaces.{FStreamHbaseIntegrationTestSpec, FStreamIntegrationTestSuite}
import org.specs2.specification.BeforeAfterAll

import scala.io.Source

class ThetaSketchIntegrationTest extends FStreamIntegrationTestSuite
  with FStreamHbaseIntegrationTestSpec with BeforeAfterAll {
  val KAFKA_TOPIC= "thetaSketchTestTopic"

  override def beforeAll(): Unit = {
    val payloads = Source.fromInputStream(
      getClass.getResourceAsStream("data/thetaSketchTestPayloads.json")).getLines()

    var writeToKafka: Seq[Map[String, Any]] = Seq.empty[Map[String, Any]]
    for (payload <- payloads) {
      writeToKafka = writeToKafka :+ payload.getObject[Map[String, Any]].get
    }

    createTopic(KAFKA_TOPIC)

    writeToTopic(
      KAFKA_TOPIC, writeToKafka
    )

  }

  "ConversionFunnelTest" should {
    "validate ppv count " in {
      execute(ThetaSketchTest, Array("Stage", "config/ThetaSketchTestJob.yml"))

      var trafficAggs = getFromHBase(
        tableName = "probabilistic_data_tables:thetasketchtest",
        rowkeys = Seq(),
        columnFamilies = Seq("cf"),
        dimensions = Seq("dim", "dim1"),
        timePartitionStr = Some("20200601"),
        convertValuesToString = false
      )
      1 mustEqual(1)
    }
  }

  def getFromHBase(tableName: String, rowkeys: Seq[String],
                   columnFamilies: Seq[ColumnFamily],
                   convertValuesToString: Boolean,
                   dimensions: Seq[String] = Seq(),
                   timePartitionStr: Option[String] = None
                  ): Map[(ColumnFamily, ColumnQualifier), ColumnValue] = {
    val rowKeyList = List(makeHashedRowKey(rowkeys).get, timePartitionStr.getOrElse(""), makeHashedRowKey(dimensions).get).filter(_.nonEmpty)
    getFromHbase(
      tableName = tableName,
      rowkeys = rowKeyList,
      columnFamilies = columnFamilies,
      isHashedRowkey = false
    )
  }
  override def afterAll(): Unit = {

  }
}
