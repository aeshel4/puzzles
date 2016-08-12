@echo off
setlocal
set params=%*
set params=%params:\=/%
java -cp . %params%