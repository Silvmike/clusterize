[![Analytics](https://ga-beacon.appspot.com/UA-73781306-2/persistent-postgresql)](https://github.com/igrigorik/ga-beacon)

# Clusterize

## Sandbox for clusterization

This branch uses:

1. **[Nginx](http://nginx.org/)** as a balancer with sticky sessions
2. **[PersistentManager](https://tomcat.apache.org/tomcat-7.0-doc/config/manager.html#Nested_Components)** approach of session passivation.

```
    ./prepare.sh
    docker-compose up
```

Run http://localhost:8080/simple/index. Stop current node and refresh the page. PostgreSQL port 5432 is exposed.
