@echo off
REM #
REM # This simple Windows batch script should help get you started using
REM # GetOpt Generator.
REM #
REM # NOTE: You will need to have 'delayed expansion' enabled in order to use
REM #       this test script.  You enable it via:
REM #
REM #       cmd.exe /V:ON
REM #
REM # NOTE: This script expects to run within the <getopt_root>\test folder
REM #

cd ..\
.\getopt.bat -x GetOpt.xml -e test.Model -j .\test\src -m test.GetOpt -c test.GetOptConfig
