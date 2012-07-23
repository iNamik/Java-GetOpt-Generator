#!/bin/bash
# This simple unix shell script should help get you started using
# GetOpt Generator.
#
# NOTE: This script expects to run within the <getopt_root>/test folder
#

cd ../
./getopt.sh -x GetOpt.xml -e test.Model -j ./test/src -m test.GetOpt -c test.GetOptConfig
