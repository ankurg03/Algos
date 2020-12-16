import subprocess
import getopt
from kafka import custom_logging

log = custom_logging.setup_logger("INFO")


def get_params_dict(args, options):
    opts, args = getopt.getopt(args,"",options)
    param_dict = {}
    for i in opts:
        param_dict[i[0].replace("--", "")] = i[1]
    return param_dict


def run(cmd, input=None):
    log.debug("Running cmd: " + cmd)
    if input is None:
        result = subprocess.run([cmd], stdout=subprocess.PIPE, shell=True)
    else:
        result = subprocess.run([cmd], stdout=subprocess.PIPE, shell=True, input=input.encode('utf-8'))
    if result.returncode == 0:
        log.debug(result.stdout.decode('utf-8').strip())
        return result.stdout.decode('utf-8').strip()
    else:
        log.error("shell command: {0} failed with returnCode: {1}".format(cmd, result.returncode))
        log.error("output {0}".format(result.stdout.decode("utf-8")))
        raise Exception("invalid input for command: {0}".format(cmd))


def get_lines_from_file(file_path: str):
    lines = []
    with open(file_path) as file:
        for line in file:
            lines.append(line.strip())
    file.close()
    return lines


def write_in_file(file_path, lines):
    file = open(file_path, 'w')
    for line in lines:
        file.write(line)
        file.write("\n")
    file.close()