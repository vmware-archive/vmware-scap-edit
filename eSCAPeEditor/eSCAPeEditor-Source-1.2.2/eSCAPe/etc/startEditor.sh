#!/bin/sh
# javaw must be in the path, e.g c:\java\bin
javaw -XX:MaxPermSize=128m -Xmx1024m -Xms40m -jar ${project.name}-${project.version}.jar $* &
