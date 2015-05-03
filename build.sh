#!/bin/sh
cd tomcat-docker && docker build -t silvmike/tomcat .
cd ../docker-nginx-sticky && docker build -t silvmike/nginx .
