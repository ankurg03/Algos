import subprocess
import getopt
from Kafka import custom_logging

log = custom_logging.setup_logger("INFO")


def get_params_dict(args, options):
    opts, args = getopt.getopt(args,"",options)
    param_dict = {}
    for i in opts:
        param_dict[i[0].replace("--", "")] = i[1]
    return param_dict


def run(cmd, input=None):
    print("Running cmd: " + cmd)
    if input is None:
        result = subprocess.run([cmd], stdout=subprocess.PIPE, shell=True)
    else:
        result = subprocess.run([cmd], stdout=subprocess.PIPE, shell=True, input=input.encode('utf-8'))
    if result.returncode == 0:
        print(result.stdout.decode('utf-8').strip())
        return result.stdout.decode('utf-8').strip()
    else:
        log.error("shell command: {0} failed with returnCode: {1}".format(cmd, result.returncode))
        log.error("output {0}".format(result.stdout.decode("utf-8")))
        raise Exception("invalid input for command: {0}".format(cmd))
