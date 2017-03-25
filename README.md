[![Analytics](https://ga-beacon.appspot.com/UA-73781306-2/welcome-page)](https://github.com/igrigorik/ga-beacon)

# Clusterize
A sandbox for clusterzation

## BackupManager

According to [documentation](https://tomcat.apache.org/tomcat-7.0-doc/cluster-howto.html#For_the_impatient): 

This manager only replicates the session data to one backup node, and only to nodes that have the application deployed. Downside of the BackupManager: not quite as battle tested as the delta manager.

Important:

* Sessions have a primary node and a backup node
* Need to use sticky sessions
* Backup node selected on a round-robin basis from all other nodes. So, **there is NOT a single backup node**
* Every node knows the primary node and backup node for every
session
* Network traffic proportional to the number of nodes
* Failover is more complicated

Use case:

Suppose we've got a cluster of 4 nodes: A, B, C, D. And suppose that node D fails.
Assume, out user makes new request, and his request comes to one of alive nodes:

1. Sessions will be distribtued to other nodes as soon as node failure is detected

2. There are 2 cases:

 * If new node was backup node, it becomes primary, and a new backup node is selected, session is replicated to new backup node.
 * If new node wasn't backup it becomes primary, backup node remains the same, session is copied from the backup node.

This solution offers a guaranteed session data replication.

[Replication via BackupManager (nginx as a balancer, farm deployer)](https://github.com/Silvmike/clusterize/tree/master/backup-manager/nginx)

[Replication via BackupManager (mod_jk as a balancer, farm deployer)](https://github.com/Silvmike/clusterize/tree/master/backup-manager/httpd)

## Memcached Backup Session Manager

Source: [memcached-session-manager](https://code.google.com/p/memcached-session-manager/)

According to [this](https://code.google.com/p/memcached-session-manager/):

The memcached session manager installed in a tomcat holds all sessions locally in the own jvm, just like the StandardManager does it as well.

Additionally, after a request was finished, the session (only if existing) is additionally sent to a memcached node for backup.

When the next request for this session has to be served, the session is locally available and can be used, after this second request is finished the session is updated in the memcached node.

Now imagine the tomcat dies.

The next request will be routed to another tomcat. This tomcat (in more detail the memcached session manager) is asked for a session he does not know. He will now lookup the session in the memcached node (based on an id that was appended to the sessionId when the session was created). He will fetch the session from memcached and store the session locally in its own jvm - he is responsible for that session from now on. After the tomcat served this request of course he also updates the session in the memcached node. You see tomcat failover is handled completely.

***Comparing to SimpleTcpCluster***

The DeltaManager does an all-to-all replication which limits scalability. The BackupManager (according to the docs not quite as battle tested as the delta manager) does a replication to another tomcat, which requires special configuration in the load balancer to support this.

memcached-session-manager supports non-sticky sessions (not supported by Delta/BackupManager) and provides session locking to handle concurrent requests served by different tomcats.

DeltaManager/BackupManager use java serialization, memcached-session-manager comes with pluggable serialization strategies and e.g. one implementation that is based on kryo.

When a new session is created the memcached-session-manager **selects the memcached node randomly**.

[Replication via MemcachedBackupManager (nginx as a balancer)](https://github.com/Silvmike/clusterize/tree/replication-backup-memcached)

[Replication via MemcachedBackupManager (mod_jk as a balancer, hybrid mod_jk+tomcat container)](https://github.com/Silvmike/clusterize/tree/replication-memcached-hybrid-node)

## DeltaManager

According to [documentation](https://tomcat.apache.org/tomcat-7.0-doc/cluster-howto.html#For_the_impatient):

DeltaManager enables all-to-all session deltas replication. By all-to-all we mean that the session gets replicated to all the other nodes in the cluster. This works great for smaller cluster but **we don't recommend it for larger clusters(a lot of Tomcat nodes)**. Also when using the delta manager it will replicate to all nodes, even nodes that don't have the application deployed. To get around this problem, you'll want to use the **BackupManager**.

This solution offers a guaranteed session data replication.

[Replication via DeltaManager (nginx as a balancer, farm deployer, dynamic membership)](https://github.com/Silvmike/clusterize/tree/replication-delta)

[Replication via DeltaManager (nginx as a balancer, farm deployer, static membership)](https://github.com/Silvmike/clusterize/tree/replication-delta-static)

## Persistent Manager with JDBC Store

Sticky Session with a persistence manager and a JDBC-based store, which is already built into Tomcat. The idea is to stored session data thus in the event of a failure the session data can be retrieved. Using a shared store all the Tomcat instances has access to the session data. However **Tomcat will not guarantee when a sessions data will be persisted** to the store, thus you could have a case where a Tomcat instance crashes but the session data was not written to the store.

[Session passivation using PersistentManager (nginx as a balancer)](https://github.com/Silvmike/clusterize/tree/persistent-postgresql)

## Hazelcast session replication using filter approach

[Session replication using Hazelcast distributed map in P2P mode (filter approach, nginx as a balancer)](https://github.com/Silvmike/clusterize/tree/replication-nginx-hazelcast)

[Session replication using Hazelcast distributed map in Client-Server mode (filter approach, nginx as a balancer)](https://github.com/Silvmike/clusterize/tree/replication-nginx-hazelcast-cs)

## Hazelcast Session Manager

[Session replication using Hazelcast distributed map in P2P mode (session manager approach, nginx as a balancer)](https://github.com/Silvmike/clusterize/tree/replication-nginx-hazelcast-tomcat)

[Session replication using Hazelcast distributed map in Client-Server mode (session manager approach, nginx as a balancer)](https://github.com/Silvmike/clusterize/tree/replication-nginx-hazelcast-cs-tomcat)

For more information see branches.
Enjoy.






## Important things to know

#### 1. According to [documentation](http://tomcat.apache.org/tomcat-7.0-doc/config/context.html#Parallel_deployment):

Parallel deployment is a function of the Context Container. The Context element represents a web application, which in turn specifies the context path to a particular Web Application Archive (WAR) file that is the application logic. Parallel deployment allows you to deploy multiple versions of a web application with the same context path concurrently. When choosing what version of the application for any given request, Tomcat will:

1. Route new requests to the latest version, so new sessions go to the new application.

2. If session information is in the request, check the session manager for a matching version, so existing sessions will go to the original application for the request.

3. If session information is in the request, but the corresponding application is no longer present, **it will route to the latest version**.


**NOTE:** It actually means that if one of your nodes fails before new version of your application is deployed to all of instances, there is a chance that this new session being replicated to failover node, will cause crashes and undesirable behaviours.

#### 2. Static membership is also allowed

* For more information see [this](https://tomcat.apache.org/tomcat-7.0-doc/config/cluster-interceptor.html#Static_Membership). Configuration example can be found [here](https://github.com/Silvmike/clusterize/tree/replication-delta-static)

* Multicast messaging isn't required for SimpleTcpCluster:

  - Multicast isn't used when replicating sessions. It is used only for dynamic membership. For more information see [this](https://tomcat.apache.org/tomcat-7.0-doc/tribes/introduction.html#Feature%20Overview)
  - BTW, it is possible to use multicast for [messaging](https://tomcat.apache.org/tomcat-7.0-doc/api/org/apache/catalina/tribes/Channel.html#SEND_OPTIONS_MULTICAST)

#### 3. Using *memcached-session-manager* be aware of that it depends on load balancer in *sticky*-mode. So it is possible to get inconsistent session data.

* For more details have a look at:

  - [this](https://github.com/magro/memcached-session-manager/blob/master/core/src/main/java/de/javakaffee/web/msm/MemcachedSessionService.java#L627-L630)
  - and [this](https://github.com/magro/memcached-session-manager/blob/master/core/src/main/java/de/javakaffee/web/msm/MemcachedSessionService.java#L725-L794)

* Use AJP Connector based load balancer
