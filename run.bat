@ECHO off
SETLOCAL ENABLEEXTENSIONS ENABLEDELAYEDEXPANSION

IF %1.==. GOTO startup
IF %1 EQU 2 GOTO twoPlayer
IF %1 EQU 4 GOTO fourPlayer

:startup
SET /p playerCount= Would you like to play a 2 player game or 4:

ECHO !playerCount!

IF !playerCount! EQU 2 GOTO twoPlayer
IF !playerCount! EQU 4 GOTO fourPlayer

ECHO Invalid input given! Please enter 2 or 4.
GOTO startup

:twoPlayer

ECHO Starting up 2 Servers

START cmd /K java -cp build/libs/Quoridor-1.0.jar server.EchoServer --port 7070
START cmd /K java -cp build/libs/Quoridor-1.0.jar server.EchoServer --port 7171

REM ping 127.0.0.1>nul

ECHO Starting up Client

cmd /K java -cp build/libs/Quoridor-1.0.jar client.CMT localhost:7070 localhost:7171

GOTO end

:fourPlayer

ECHO Starting up 4 Servers

START cmd /C java -cp build/libs/Quoridor-1.0.jar server.EchoServer --port 7070
START cmd /C java -cp build/libs/Quoridor-1.0.jar server.EchoServer --port 7171
START cmd /C java -cp build/libs/Quoridor-1.0.jar server.EchoServer --port 7272
START cmd /C java -cp build/libs/Quoridor-1.0.jar server.EchoServer --port 7373

ECHO Starting up Client

REM ping 127.0.0.1>nul

cmd /C java -cp build/libs/Quoridor-1.0.jar client.CMT localhost:7070 localhost:7171 localhost:7272 localhost:7373
GOTO end

:end
EXIT 0