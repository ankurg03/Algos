try:
    import simplejson as json
except ImportError:
    import json

from kazoo.client import KazooClient
from kafka import constants, custom_logging


log = custom_logging.setup_logger("INFO")


def init_client(zk_path: str, read_only=True):
    log.info("Initializing zk client")
    zk: KazooClient = KazooClient(hosts=zk_path, read_only=read_only)
    zk.start()
    return zk


def get_kafka_brokers(zk: KazooClient):
    log.info("Getting broker_host:port")
    brokers: list = []
    broker_ids: list = zk.get_children(constants.BROKER_PATH_IN_ZK)
    if broker_ids is not None:
        for broker_id in broker_ids:
            if broker_id is not None:
                broker_info_str = zk.get("{}{}".format(constants.BROKER_PATH_IN_ZK, broker_id))[0].decode("utf-8").strip()

                broker_info = json.loads(broker_info_str)
                brokers.append("{}:{}".format(broker_info["host"], broker_info["port"]))
    return brokers
