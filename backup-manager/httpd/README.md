# Clusterize

## Sandbox for clusterization

1. **[Apache2](http://httpd.apache.org/) + [mod_jk](http://tomcat.apache.org/connectors-doc/)** sticky sessions.
2. **[BackupManager](http://tomcat.apache.org/tomcat-7.0-doc/cluster-howto.html)** approach of session replication.

An important thing: **jmvRoute** is specified via environment variable. See **docker-compose.yml** for details.

To play with do the following:

***Prerequisites**: Docker, Git installed.

	git clone https://github.com/Silvmike/clusterize.git

	git checkout replication-backup-jk

	git submodule init

	git submodule update


And for the first time: **sh ./build.sh** it will build required Docker images

To run images use: **sh ./run-docker.sh** it will run 3 containers with tomcat on board and 1 apache2+mod_jk container used as load balancer, then it will expose apache2 container port **8080** to your host port **8080**.

Run http://localhost:8080/simple/index. Stop current node and refresh the page.
