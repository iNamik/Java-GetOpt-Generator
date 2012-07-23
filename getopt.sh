#!/bin/bash
#
# This simple unix shell script should help get you started using
# GetOpt Generator.
#
# You will need to configure the location of the GetOpt root folder.
#
# A working command-line example:
#
# -x GetOpt.xml -e test.Model -j ./test/src -m test.GetOpt -c test.GetOptConfig
#
# NOTE: The example assumes you will be executing the getopt.sh script
#       from within the distribution directory that contains the script.
#

# Point to your GetOpt root directory
wd="."

# Build the classpath from jars in the lib folder
cp="$wd/properties"
for i in `ls -1 $wd/lib/*.jar`
do
	cp="$cp:$i"
done

# Call the main class
java -client -cp "$cp" com.inamik.getopt.GetOpt "$@"
