@echo off

if "%1"=="hide" goto CmdBegin
start mshta vbscript:createobject("wscript.shell").run("""%~0"" hide",0)(window.close)&&exit
:CmdBegin

for /f %%a in (.pid) do (
	set pid=%%~a
	goto :Show
)

:Show
echo %pid%

for /f "delims=" %%t in ('tasklist ^| findstr %pid%') do set info=%%t
echo %info%

if "%info%"=="" (
	echo %pid% is shutdown
) else (
	echo %pid% is running, kill it
	taskkill /PID %pid% /t /f
)

cd %install_path%
java -Xmx256m -Djava.compiler=NONE -jar {##jar.jar##} &

exit