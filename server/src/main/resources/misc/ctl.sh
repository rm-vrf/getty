#!/bin/sh

#unset DYLD_LIBRARY_PATH

# resolve links - $0 may be a softlink
PRG="$0"
while [ -h "$PRG" ] ; do
  ls=`ls -ld "$PRG"`
  link=`expr "$ls" : '.*-> \(.*\)$'`
  if expr "$link" : '/.*' > /dev/null; then
    PRG="$link"
  else
    PRG=`dirname "$PRG"`/"$link"
  fi
done
PRGDIR=`dirname "$PRG"`

JAVA_OPTS=
ADDRESS=
FILE_ENCODING=UTF-8
TMP_DIR=$PRGDIR/../tmp
GT_PIDFILE=$TMP_DIR/getty.pid
LIB_DIR=$PRGDIR/../lib
BASE_DIR=$PRGDIR/..
MAIN_CLASS=cn.batchfile.getty.server.Main
APPS_DIR=$PRGDIR/../webapps

CP=.
for i in `ls $LIB_DIR`
do
  CP=$CP:$LIB_DIR/$i
done

# set customer vars
if [ -r "$PRGDIR/setenv.sh" ]; then
  . "$PRGDIR/setenv.sh"
fi

if [ -z "$JAVA_HOME" ]; then
    CMD=java
else
    CMD="$JAVA_HOME/bin/java"
fi

GT_START="$CMD $JAVA_OPTS -classpath $CP \
    -Dfile.encoding=$FILE_ENCODING \
    -Djava.tmp.dir=$TMP_DIR \
    -Djava.io.tmpdir=$TMP_DIR \
    $MAIN_CLASS \
    --base-dir=$BASE_DIR \
    --apps-dir=$APPS_DIR \
    --pid-file=$GT_PIDFILE"
#echo $GT_START
#exit

GT_STATUS=""
GT_PID=""
PID=""
ERROR=0

get_pid() {
    PID=""
    PIDFILE=$1
    # check for pidfile
    if [ -f "$PIDFILE" ] ; then
        PID=`cat $PIDFILE`
    fi
}

get_gt_pid() {
    get_pid $GT_PIDFILE
    if [ ! "$PID" ]; then
        return
    fi
    if [ "$PID" -gt 0 ]; then
        GT_PID=$PID
    fi
}

is_service_running() {
    PID=$1
    if [ "x$PID" != "x" ] && kill -0 $PID 2>/dev/null ; then
        RUNNING=1
    else
        RUNNING=0
    fi
    return $RUNNING
}

is_gt_running() {
    get_gt_pid
    is_service_running $GT_PID
    RUNNING=$?
    if [ $RUNNING -eq 0 ]; then
        GT_STATUS="service not running"
    else
        GT_STATUS="service already running"
    fi
    return $RUNNING
}

start_gt() {
    is_gt_running
    RUNNING=$?
    if [ $RUNNING -eq 1 ]; then
        echo "$0 $ARG: service (pid $GT_PID) already running"
	exit
    fi
    nohup $GT_START >/dev/null 2>&1 &
    COUNTER=40
    while [ $RUNNING -eq 0 ] && [ $COUNTER -ne 0 ]; do
        echo "."
        COUNTER=`expr $COUNTER - 1`
        sleep 3
        is_gt_running
        RUNNING=$?
    done
    echo "."
    
    if [ $RUNNING -eq 0 ]; then
        ERROR=1
    fi
    if [ $ERROR -eq 0 ]; then
	    echo "$0 $ARG: service started"
	    sleep 2
    else
	    echo "$0 $ARG: service could not be started"
	    ERROR=3
    fi
}

stop_gt() {
    NO_EXIT_ON_ERROR=$1
    is_gt_running
    RUNNING=$?
    if [ $RUNNING -eq 0 ]; then
        echo "$0 $ARG: $GT_STATUS"
        if [ "x$NO_EXIT_ON_ERROR" != "xno_exit" ]; then
            exit
        else
            return
        fi
    fi
	
	kill $GT_PID

    COUNTER=40
    while [ $RUNNING -eq 1 ] && [ $COUNTER -ne 0 ]; do
        echo "."
        COUNTER=`expr $COUNTER - 1`
        sleep 3
        is_gt_running
        RUNNING=$?
    done
    echo "."

    is_gt_running
    RUNNING=$?
    if [ $RUNNING -eq 0 ]; then
            echo "$0 $ARG: service stopped"
            rm -f $GT_PIDFILE
        else
            echo "$0 $ARG: service could not be stopped"
            ERROR=4
    fi
}

cleanpid() {
    rm -f $GT_PIDFILE
}

if [ "x$1" = "xstart" ]; then
    start_gt
elif [ "x$1" = "xstop" ]; then
    stop_gt
elif [ "x$1" = "xstatus" ]; then
    is_gt_running
    echo "$GT_STATUS"
elif [ "x$1" = "xcleanpid" ]; then
    cleanpid
elif [ "x$1" = "xdeamon" ]; then
    is_gt_running
    RUNNING=$?
    if [ $RUNNING -eq 1 ]; then
        echo "$0 $ARG: service (pid $GT_PID) already running"
    else
        echo "$0 $ARG: service (pid $GT_PID) not running"
        start_gt
    fi
fi

exit $ERROR
