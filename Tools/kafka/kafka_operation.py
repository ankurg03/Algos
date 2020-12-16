import random
import sys
from kafka import utility, kafka_utility, zk_utility, custom_logging

log = custom_logging.setup_logger("INFO")

if __name__ == "__main__":

    log.info("Getting params")
    params_dict = utility.get_params_dict(sys.argv[1:],
                                          ["zookeeper=", "topics=", "topic_file=", "timestamp=", "output_file="])

    log.info("Getting brokers from zookeeper")
    zk_client = zk_utility.init_client(params_dict["zookeeper"])
    brokers = zk_utility.get_kafka_brokers(zk_client)

    log.info("Building topic list")
    topic_lst = []
    if "topics" in params_dict:
        topic_lst += params_dict["topics"].split(",")
    if "topic_file" in params_dict:
        topic_lst += utility.get_lines_from_file(params_dict["topic_file"])

    log.info("Getting offsets")
    topic_offsets: list = []
    for topic in topic_lst:
        offsets = kafka_utility.get_offset(random.choice(brokers),
                                           params_dict["zookeeper"],
                                           topic,
                                           params_dict["timestamp"])
        topic_offsets.append(offsets.strip())

    if "output_file" in params_dict:
        log.info("Writing output in the file {}".format(params_dict["output_file"]))
        utility.write_in_file(params_dict["output_file"], topic_offsets)
    else:
        print(topic_offsets)
