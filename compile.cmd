@echo off
setlocal
set params=%*
set params=%params:\=/%
javac -cp . %params%