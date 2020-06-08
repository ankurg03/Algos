import json
import re
from Kafka import constants, utility, custom_logging

log = custom_logging.setup_logger("INFO")


def get_partitions(zookeeper, topic):
    log.debug("Getting partitions for topic: {}".format(topic))
    cmd = (constants.KAFKA_BIN + constants.TOPIC_META_SCRIPT + constants.GET_PARTITIONS_CMD).format(zookeeper, topic)
    result = utility.run(cmd)
    lines = result.splitlines()
    partitions = []
    for line in lines:
        if "Partition:" in line:
            partitions.append(re.search('Partition: \d+', line).group().split()[1])
    return partitions


def create_query(topic, partitions, timestamp):
    query = ""
    for partition in partitions:
        query += " -t \"{}:{}:{}\"".format(topic, partition, timestamp)
    return query


def get_offset(brokers_with_ports, zookeeper, topic, timestamp, method=constants.TOOLS.CONFLUENT_KAFKA_CAT):
    topic_offsets = {topic :{}}
    partitions = get_partitions(zookeeper, topic)
    if method == constants.TOOLS.CONFLUENT_KAFKA_CAT:
        query = create_query(topic, partitions, timestamp)
        cmd = constants.GET_OFFSET_KAKFA_CAT_CMD.format(brokers_with_ports, query)
        result = utility.run(cmd)
        for offset_info in result.split("\n"):
            topic_offsets[topic][re.sub(r"[\[\]]", "", offset_info.split()[1])] = offset_info.split()[3]
    return json.dumps(topic_offsets, sort_keys=True)
