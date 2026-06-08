#!/bin/sh
#
# Gradle wrapper script pentru Linux/macOS
#

APP_NAME="Gradle"
APP_BASE_NAME=$(basename "$0")

# Resolve links
PRG="$0"
while [ -h "$PRG" ]; do
  ls=$(ls -ld "$PRG")
  link=$(expr "$ls" : '.*-> \(.*\)$')
  if expr "$link" : '/.*' > /dev/null; then
    PRG="$link"
  else
    PRG=$(dirname "$PRG")"/$link"
  fi
done
SAVED=$(pwd)
cd $(dirname "$PRG") > /dev/null
APP_HOME=$(pwd -P)
cd "$SAVED" > /dev/null

CLASSPATH=$APP_HOME/gradle/wrapper/gradle-wrapper.jar

# Detectează Java
if [ -n "$JAVA_HOME" ]; then
  JAVACMD="$JAVA_HOME/bin/java"
else
  JAVACMD="java"
fi

exec "$JAVACMD" \
  -classpath "$CLASSPATH" \
  org.gradle.wrapper.GradleWrapperMain \
  "$@"
