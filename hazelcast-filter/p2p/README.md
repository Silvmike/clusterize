[![Analytics](https://ga-beacon.appspot.com/UA-73781306-2/replication-nginx-hazelcast)](https://github.com/igrigorik/ga-beacon)

# Clusterize

## Sandbox for clusterization

This branch uses:

1. **[Nginx](http://nginx.org/)** as a balancer with sticky sessions
2. **[Hazelcast](http://hazelcast.org/) distributed maps** approach of session replication using **[P2P](http://docs.hazelcast.org/docs/3.3/manual/html/tomcatsessionreplication.html)** mode.

```
    ./prepare.sh
    docker-compose up
```

Run http://localhost:8080/simple/index. Stop current node and refresh the page.
