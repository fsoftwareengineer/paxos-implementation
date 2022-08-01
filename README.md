# paxos-implementation
Distributed Key-Value store implementation using Paxo's algorithm.

- Currently using local storage partitioned via directories so they can run locally.

## How to run

```
$ cd paxos-implementation
$ ./gradlew bootRun --args='--serverName=<SERVER_NAME> --server.port=<PORT>'
```

E.g, 

Terminal 1
```
$ ./gradlew bootRun --args='--serverName=Athens --server.port=8080'
```

Terminal 2
```
$ $ ./gradlew bootRun --args='--serverName=Byzantium --server.port=8081'
```


### Testing
For now, I tested using [Postman](https://www.postman.com/) and manually tested each scenario in MartinFowler's article.
Still contemplating on how to unit-test.


### References:
https://martinfowler.com/articles/patterns-of-distributed-systems/paxos.html
https://lamport.azurewebsites.net/pubs/paxos-simple.pdf