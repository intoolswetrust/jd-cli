@echo off
set "DIRNAME=%~dp0%"

java -jar "%DIRNAME%jd-cli.jar" %*
