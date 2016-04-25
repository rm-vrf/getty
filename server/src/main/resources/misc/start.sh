#!/bin/sh

# env
JAVA_OPTS=
PORT=1025
MAX_THREAD=-1
MIN_THREAD=-1
MAX_IDLE=-1
LOG_LEVEL=INFO
MAX_QUEUED=-1
WEB_ROOT=webapp
FILE_ENCODING=UTF-8
URI_ENCODING=UTF-8
CHARSET=UTF-8
LIST_DIRECTORY=true
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

WEB_ROOT=$PRGDIR/$WEB_ROOT
LIB_DIR=$PRGDIR/lib:$PRGDIR/lib/optional:$WEB_ROOT:$WEB_ROOT/lib
JAR_FILE=$PRGDIR/getty.jar

java $JAVA_OPTS -Djava.ext.dirs=$LIB_DIR \
-Xbootclasspath/a:$WEB_ROOT \
-Dfile.encoding=$FILE_ENCODING \
-jar $JAR_FILE \
--port=$PORT \
--max.thread=$MAX_THREAD \
--min.thread=$MIN_THREAD \
--max.idle=$MAX_IDLE \
--log.level=$LOG_LEVEL \
--max.queued=$MAX_QUEUED \
--web.root=$WEB_ROOT \
--file.encoding=$FILE_ENCODING \
--uri.encoding=$URI_ENCODING \
--charset=$CHARSET \
--list.directory=$LIST_DIRECTORY \
--index.pages=$INDEX_PAGES
