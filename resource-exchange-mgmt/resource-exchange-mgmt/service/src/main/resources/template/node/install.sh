#!/usr/bin/env bash
DIR=/root/websites/dcrun-node-service/{##appid##}

if [ ! -d "$DIR" ]; then
  mkdir -p "$DIR"
fi

# wget download url
URL={##URL##}

cd $DIR
wget $URL #-O $FILE -o $LOGFILE

echo "unzip file"
unzip {##fileName##}

chmod 777 run.sh

echo "exec run.sh"
./run.sh

echo "The installation is complete!!!"
