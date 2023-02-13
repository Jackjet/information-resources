#注意每个项目的名字都不一样，需要修改MY_PROJECT_NAME为自己项目的名称,否则在一个操作系统里使用同样的名字会导致互相影响
export MY_PROJECT_NAME_PORT="8090"
export MY_PROJECT_NAME_POSTGRE_URL="jdbc:postgresql://47.105.140.86:5432/single?characterEncoding=utf8&useSSL=false"
export MY_PROJECT_NAME_POSTGRE_USERNAME="postgres"
export MY_PROJECT_NAME_POSTGRE_PASSWORD="1234qwer1234asdf"
export MY_PROJECT_NAME_DRUID_PORTAL="/d1Project/druid/*"

count=$(ps -ef | grep "java" | grep "MY_JAR_NAME" | wc -l)
if [ $count -le 0 ]; then
echo "MY_JAR_NAME stop"
else
pid=$(ps aux | grep 'MY_JAR_NAME' |grep java| awk '{print $2}' | sort -n | head -n 1)
echo "MY_JAR_NAME running, kill pid:$pid"
kill $pid
fi
java  -Xms128M -Xmx512m -jar ./MY_JAR_NAME-0.0.1-SNAPSHOT.jar &
