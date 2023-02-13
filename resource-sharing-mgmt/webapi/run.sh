#注意每个项目的名字都不一样，需要修改RESOURCESHARINGMGMT_RESOURCESHARINGMGMT为自己项目的名称,否则在一个操作系统里使用同样的名字会导致互相影响
export RESOURCESHARINGMGMT_RESOURCESHARINGMGMT_PORT="8090"
export RESOURCESHARINGMGMT_RESOURCESHARINGMGMT_POSTGRE_URL="jdbc:postgresql://47.105.140.86:5432/single?characterEncoding=utf8&useSSL=false"
export RESOURCESHARINGMGMT_RESOURCESHARINGMGMT_POSTGRE_USERNAME="postgres"
export RESOURCESHARINGMGMT_RESOURCESHARINGMGMT_POSTGRE_PASSWORD="1234qwer1234asdf"
export RESOURCESHARINGMGMT_RESOURCESHARINGMGMT_DRUID_PORTAL="/d1Project/druid/*"

count=$(ps -ef | grep "java" | grep "resourcesharingmgmt" | wc -l)
if [ $count -le 0 ]; then
echo "resourcesharingmgmt stop"
else
pid=$(ps aux | grep 'resourcesharingmgmt' |grep java| awk '{print $2}' | sort -n | head -n 1)
echo "resourcesharingmgmt running, kill pid:$pid"
kill $pid
fi
java  -Xms128M -Xmx512m -jar ./resourcesharingmgmt-0.0.1-SNAPSHOT.jar &
