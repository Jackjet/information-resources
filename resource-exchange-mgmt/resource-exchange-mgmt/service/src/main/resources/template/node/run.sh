#!/usr/bin/env bash
count=$(ps -ef | grep "java" | grep "{##jar##}" | wc -l)
if [ $count -le 0 ]; then
echo "{##jar##} is stop"
else
pid=$(ps aux | grep '{##jar##}' |grep java| awk '{print $2}' | sort -n | head -n 1)
echo "{##jar##} is running, kill pid:$pid"
kill $pid
fi
cd /root/websites/dcrun-node-service/{##appid##}
java -Xmx256m -Djava.compiler=NONE -jar {##jar.jar##} &
cd -
