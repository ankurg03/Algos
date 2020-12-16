import enum

KAFKA_BIN="/Users/ankurgupta.p/Downloads/kafka_2.11-1.1.1/bin/"
# KAFKA_BIN="/usr/share/fk-fdp-kafka/bin/"
TOPIC_META_SCRIPT="kafka-topics.sh"
GET_PARTITIONS_CMD = " --zookeeper {} --topic {} --describe"
GET_OFFSET_KAKFA_CAT_CMD = "kafkacat -b {} -Q {}"
BROKER_PATH_IN_ZK="/brokers/ids/"


TOOLS = enum.Enum('AVAILABLE_TOOL', 'APACHE_KAKFKA CONFLUENT_KAFKA_CAT')
