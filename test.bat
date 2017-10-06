@echo off
rmdir /S /Q allure-results
mvn clean test|more
copy "output.log" "logs\%date:/=-%_%time::=-%.log"