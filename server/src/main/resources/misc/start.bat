@echo off

java -Djava.ext.dirs=..\lib -Dfile.encoding=UTF-8 -Djava.tmp.dir=..\tmp -Djava.io.tmpdir=..\tmp cn.batchfile.getty.server.Main --base-dir=..\ --apps-dir=..\webapps
