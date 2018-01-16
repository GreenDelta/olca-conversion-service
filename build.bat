@echo off

SET version=0.0.2

FOR /f "tokens=2 delims==" %%a IN ('wmic OS Get localdatetime /value') DO SET "dt=%%a"
set "YYYY=%dt:~0,4%"
set "MM=%dt:~4,2%"
set "DD=%dt:~6,2%"
SET vdate="%version%_%YYYY%_%MM%_%DD%"
SET appname=olca-conv_%vdate%

echo Creating an uber-jar ...
call mvn clean package

echo Create the deployment package ...

if exist ".\target\%appname%.zip" del ".\target\%appname%.zip"
if exist ".\target\%appname%" rmdir /s /q ".\target\%appname%"
mkdir .\target\%appname%
mkdir .\target\%appname%\web
copy /Y .\target\server.jar .\target\%appname%\server.jar
robocopy .\deploy .\target\%appname% /e

if not exist ".\ui\build" (
    echo WARNING: Packaging without user interface;
    echo          build it first if you want to include it
) else (
    robocopy .\ui\build .\target\%appname%\web /e
)

cd target
jar -cMf .\%appname%.zip .\%appname%
cd ..

echo all done!

:end
