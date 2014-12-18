@echo off

java -Djava.ext.dirs=.\lib;.\lib\optional;webapp;webapp\lib ^
-Xbootclasspath/a:webapp ^
-Dfile.encoding=UTF-8 ^
-jar getty.jar ^
--port=8080 ^
--max.thread=-1 ^
--min.thread=-1 ^
--max.idle=-1 ^
--log.level=DEBUG ^
--max.queued=-1 ^
--web.root=webapp ^
--file.encoding=UTF-8 ^
--uri.encoding=UTF-8 ^
--charset=UTF-8 ^
--list.directory=true ^
--index.pages=index.html,index.htm,index.groovy
