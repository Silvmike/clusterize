[![Analytics](https://ga-beacon.appspot.com/UA-73781306-2/replication-delta-static)](https://github.com/igrigorik/ga-beacon)

# Clusterize

## Sandbox for clusterization

This branch uses:

1. **[Nginx](http://nginx.org/)** as a balancer with sticky sessions
2. **[DeltaManager](http://tomcat.apache.org/tomcat-7.0-doc/cluster-howto.html)** approach of session replication with static [membership](https://tomcat.apache.org/tomcat-7.0-doc/config/cluster-interceptor.html#Static_Membership).

```
    docker-compose up
```

Run http://localhost:8080/simple/index. Stop current node and refresh the page.
