# Clusterize

## Sandbox for clusterization

1. **[Apache2](http://httpd.apache.org/) + [mod_jk](http://tomcat.apache.org/connectors-doc/)** sticky sessions.
2. **[BackupManager](http://tomcat.apache.org/tomcat-7.0-doc/cluster-howto.html)** approach of session replication.

An important thing: **jmvRoute** is specified via environment variable. See **docker-compose.yml** for details.

To play with do the following:

```
    docker-compose up
```

Run http://localhost:8080/simple/index. Stop current node and refresh the page.
