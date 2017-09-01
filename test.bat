@echo off
rmdir /S /Q allure-results
mvn clean test|more
copy "output.log" "%date:/=-%_%time::=-%.log"