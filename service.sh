#!/bin/bash
DIR=./target
DATE1=$(date +%Y%m%d)
APP_GREP=winstar-cbc-platform-api
function start(){
  cd $DIR
    n=`ps -ef|grep $APP_GREP|grep -v grep|wc -l`
    if [ 0 -ne $n ];then
        kill -9 `ps -ef | grep $APP_GREP|grep -v grep| awk  '{print $2}'`
    fi
  if [ -f *.jar ];then
    APP=$(ls *.jar)
  else
    echo "APP NOT FOUND!"
    exit 1
  fi
    nohup java -jar $APP --spring.profiles.active=default > /dev/null 2>&1 &
    sleep 2
    echo -e 'starting  ............... \033[32m [ ok ] \033[0m'
}

function stop(){
  cd $DIR
   kill -9 `ps -ef | grep $APP_GREP | grep -v grep | awk  '{print $2}'`
  if [ $? == 0 ];then
    echo -e "closed PID:$PID ....... \033[32m [ ok ] \033[0m"
  fi
  if [ -f nohup.out ];then
    rm -rf nohup.out
  fi
}

function update() {
  DATE2=$(date +%Y%m%d-%H:%M)
  cd $DIR
  mkdir -p backup/$DATE1/app
  for app in `ls *.jar`;do
    mv $app backup/$DATE1/app/$app-$DATE2
  done
}

case $1 in
'start')
  start
  ;;
'stop')
  stop
  ;;
'update')
  stop
  update
  ;;
'restart')
  stop & >/dev/null
  start
  ;;
*)
  echo "用法：$0 [start | stop | restart | update]"
esac
