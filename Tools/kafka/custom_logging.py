import coloredlogs
import logging


def setup_logger(log_level):
    logger = logging.getLogger()
    fh = logging.FileHandler('script.log', mode='w')
    fh.setLevel(log_level)
    fh.setFormatter(logging.Formatter(
        fmt='%(asctime)s %(levelname)6s [%(filename)s:%(lineno)d] %(message)s',
        datefmt='%Y-%m-%d %H:%M:%S'
    ))
    logger.addHandler(fh)

    coloredlogs.DEFAULT_LEVEL_STYLES = {
        'info': {},
        'notice': {'color': 'magenta', 'bold': True},
        'critical': {'color': 'red', 'bold': True},
        'error': {'color': 'red', 'bold': True},
        'debug': {'color': 'green', 'bold': True},
        'warning': {'color': 'yellow', 'bold': True}
    }

    coloredlogs.DEFAULT_FIELD_STYLES = {
        'asctime': {'color': 'green'},
        'hostname': {'color':'magenta'},
        'levelname': {'color': '','bold':True},
        'filename': {'color': 'cyan'}
    }

    coloredlogs.install(
        fmt='%(asctime)s %(hostname)s %(levelname)6s [%(filename)s:%(lineno)d] %(message)s',
        logging=logger,
        level=log_level
    )

    return logger
