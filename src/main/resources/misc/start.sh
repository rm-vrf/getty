#!/bin/sh

# env
PORT=1025
MAX_THREAD=-1
MIN_THREAD=-1
MAX_IDLE_TIME=-1
LOG_LEVEL=INFO
CONTEXT_PATH=
REQUEST_HEADER_SIZE=-1
MAX_QUEUED=-1
WEB_ROOT=webapp
FILE_ENCODING=UTF-8
CHARSET=UTF-8
URI_ENCODING=UTF-8
ALLOW_LIST_DIRECTORY=true
INDEX_PAGES=index.html,index.htm,index.groovy

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

# set customer vars
if [ -r "$PRGDIR/setenv.sh" ]; then
  . "$PRGDIR/setenv.sh"
fi

if [ $JAVA_DIR ]; then
  export JAVA_HOME=$JAVA_DIR
  export CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar
fi

LIB_DIR=$PRGDIR/lib:$PRGDIR/lib/optional
WEB_ROOT=$PRGDIR/$WEB_ROOT
JAR_FILE=$PRGDIR/getty.jar

$JAVA_HOME/bin/java $JAVA_OPTS -Djava.ext.dirs=$LIB_DIR:$WEB_ROOT \
-Dfile.encoding=$FILE_ENCODING \
-Xbootclasspath/a:$WEB_ROOT \
-jar $JAR_FILE \
--port=$PORT
