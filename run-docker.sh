#!/bin/sh
cd tomcat-docker
docker run -d --name=node01.tomcat -v $(readlink -f webapps):/usr/share/webapps silvmike/tomcat
docker run -d --name=node02.tomcat -v $(readlink -f webapps):/usr/share/webapps silvmike/tomcat
docker run -d --name=node03.tomcat -v $(readlink -f webapps):/usr/share/webapps silvmike/tomcat
echo "Tomcat instances are running..."
sh update-app.sh
docker run -d --link=node01.tomcat:node01.hardcoders.ru \
              --link=node02.tomcat:node02.hardcoders.ru \
              --link=node03.tomcat:node03.hardcoders.ru \
              -p 8080:8080 silvmike/nginx
echo "Nginx as load balancer is running. Exposed port: 8080"
