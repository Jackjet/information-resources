#注意每个项目的名字都不一样，需要修改MY_PROJECT_NAME为自己项目的名称,否则在一个操作系统里使用同样的名字会导致互相影响
count=$(ps -ef | grep "java" | grep "user_management" | wc -l)
if [ $count -le 0 ]; then
echo "user_management stop"
else
pid=$(ps aux | grep 'user_management' |grep java| awk '{print $2}' | sort -n | head -n 1)
echo "user_management running, kill pid:$pid"
kill $pid
fi
java  -Xms128M -Xmx512m -jar ./user_management-0.0.1-SNAPSHOT.jar &
