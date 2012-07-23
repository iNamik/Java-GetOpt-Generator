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
REM # You will need to configure the location of the GetOpt root folder.
REM #
REM # A working command-line example:
REM #
REM # -x GetOpt.xml -e test.Model -j .\test\src -m test.GetOpt -c test.GetOptConfig
REM #
REM # NOTE: The example assumes you will be executing the getopt.bat script
REM #       from within the distribution directory that contains the script.
REM #

REM # Point to your GetOpt root directory
SET wd=.

REM # Build the classpath from jars in the lib folder
SET cp=%wd%\properties
FOR %%i IN (%wd%\lib\*.jar) do SET cp=!cp!;%%i

REM # Build command-line parms
SET cl=
:loop
SET cl=!cl! "%1"
SHIFT
IF .%1==. GOTO end
GOTO loop
:end

REM # Call the main class
java -client -cp "!cp!" com.inamik.getopt.GetOpt !cl!
