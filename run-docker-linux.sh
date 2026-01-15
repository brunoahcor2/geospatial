#!/bin/bash

# Nome da imagem e container
IMAGE_NAME=geospatial
CONTAINER_NAME=geospatial-container

# Porta da aplicação
APP_PORT=8080

# Build da imagem
echo "Construindo a imagem Docker..."
docker build -t $IMAGE_NAME .

# Remove container antigo, se existir
if [ $(docker ps -a -q -f name=$CONTAINER_NAME) ]; then
    echo "Removendo container antigo..."
    docker rm -f $CONTAINER_NAME
fi

# Cria e roda o container
echo "Rodando o container..."
docker run -d \
    --name $CONTAINER_NAME \
    -p $APP_PORT:8080 \
    $IMAGE_NAME

echo "Aplicação disponível em http://localhost:$APP_PORT"
