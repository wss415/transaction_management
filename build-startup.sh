#!/bin/bash

NAME=transaction
IMG=bank-system/$NAME

docker stop $NAME
docker rm $NAME
docker rmi $IMG
docker build -t $IMG .
docker run -m 1g -p 8080:8080 --privileged=true --restart always --name $NAME -d $IMG