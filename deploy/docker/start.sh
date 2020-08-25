#!/bin/bash

if [[ -z $HOST ]]
then
    export HOST=0.0.0.0
fi
if [[ -z $PORT ]]
then
    export PORT=80
fi
if [[ -z $WORKSPACE_DIR ]]
then
    export WORKSPACE_DIR=/app/workspace
fi
if [[ -z $DERBY_DIR ]]
then
    export DERBY_DIR=/app/database
fi
if [[ -z $UI_DIR ]]
then
    export UI_DIR=/app/ui/build
fi
if [[ -z $MAPPINGS_DIR ]]
then
    export MAPPINGS_DIR=/app/mappings
fi

envsubst '${HOST},${PORT},${WORKSPACE_DIR},${DERBY_DIR},${UI_DIR},${MAPPINGS_DIR}' < /app/config.json.template > /app/config.json

cat /app/config.json

java -jar server.jar -Xmx2G -Xms16m -XX:+UnlockExperimentalVMOptions -XX:+UseShenandoahGC -XX:ShenandoahGCHeuristics=compact -XX:ShenandoahUncommitDelay=1000 -XX:ShenandoahGuaranteedGCInterval=10000 -verbose:gc