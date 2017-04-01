[![Analytic-](https://ga-beacon.appspot.com/UA-73781306-2/replication-backup-memcached)](https://github.com/igrigorik/ga-beacon)

# Clusterize

## Sandbox for clusterization

This branch uses:

1. **[Nginx](http://nginx.org/)** as a balancer with sticky sessions
2. **[MemcachedSessionManager](https://code.google.com/p/memcached-session-manager/)** approach of session replication.


```
    ./prepare.sh
    docker-compose up
```

Run http://localhost:8080/simple/index. Stop current node and refresh the page.
