@echo off
SETLOCAL ENABLEDELAYEDEXPANSION

REM Nome da imagem e container
SET IMAGE_NAME=geospatial
SET CONTAINER_NAME=geospatial-container

REM Porta da aplicação
SET APP_PORT=8080

REM Build da imagem
echo Construindo a imagem Docker...
docker build -t %IMAGE_NAME% .

REM Remove container antigo, se existir
for /f "tokens=*" %%i in ('docker ps -a -q -f "name=%CONTAINER_NAME%"') do (
    echo Removendo container antigo...
    docker rm -f %%i
)

REM Cria e roda o container
echo Rodando o container...
docker run -d --name %CONTAINER_NAME% -p %APP_PORT%:8080 %IMAGE_NAME%

echo.
echo Aplicacao disponivel em http://localhost:%APP_PORT%
pause