@echo off

setlocal EnableDelayedExpansion
pushd .

rem These variables are essential
set src_list=src
set lib_list=libsrc
set jar_name=Talide
set main_class=talide.Talide

set "to_compile= "

set param=%1
if "%param%"=="" set param=all

if "%param%"=="clean" (
  call :make_clean
) else if "%param%"=="class" (
  call :make_lib
  if errorlevel 1 (
		echo Fail to compile library sources. Cancel following steps...
		popd
		goto :eof
	)
  call :make_src
  if errorlevel 1 echo Fail to compile sources.
) else if "%param%"=="wrap" (
  call :wrap
) else if "%param%"=="all" (
  call :make_lib
  if errorlevel 1 (
		echo Fail to compile library sources. Cancel following steps...
		popd
		goto :eof
	)
  call :make_src
  if errorlevel 1 (
		echo Fail to compile sources. Cancel following steps...
		popd
		goto :eof
	)
  call :wrap
) else (
  echo Error: unrecognized command
)
popd
endlocal
goto :eof

:make_clean
setlocal
del /q %jar_name%.jar
cd bin
del /s /q *
for /f %%d in ('dir /b /ad') do (
  echo Removing Directory ./bin/%%d...
  rd /s /q %%d
)
endlocal
goto :eof

:make_lib
setlocal
echo Compiling library sources...
for %%s in (%lib_list%) do call :anlyz_subdir %%s
set to_compile=!to_compile:%cd%\=!
javac -d ./bin/ !to_compile:.=*.java!
endlocal
goto :eof

:make_src
setlocal
echo Compiling sources...
for %%s in (%src_list%) do call :anlyz_subdir %%s
set to_compile=!to_compile:%cd%\=!
javac -d ./bin/ -cp ./bin/ !to_compile:.=*.java!
endlocal
goto :eof

:wrap
setlocal
echo Creating manifest file...
set mf_name=mf_%jar_name%
if exist %mf_name% del /s /q %mf_name%>nul
echo Manifest-Version: 1.0 > %mf_name%
echo Main-Class: %main_class% >> %mf_name%
echo Wrapping...
jar cfm %jar_name%.jar %mf_name% -C bin/ .
jar uf %jar_name%.jar img
del /s /q %mf_name%>nul
endlocal
goto :eof

:anlyz_subdir
setlocal EnableDelayedExpansion
for /r %1 %%i in (.) do set to_compile=!to_compile! %%i
endlocal & set to_compile=%to_compile%
goto :eof