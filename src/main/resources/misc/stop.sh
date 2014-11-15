#!/bin/sh

kill -15 `ps -ef | grep java | grep getty | awk '{print $2}'`
