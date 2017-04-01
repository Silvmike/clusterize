#!/bin/sh
mkdir -p tomcat/lib
mvn dependency:copy-dependencies -DoutputDirectory=tomcat/lib
