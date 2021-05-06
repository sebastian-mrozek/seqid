Sample curl commands
---
Create new sequence:
```
curl -X POST -d '{"namespace":"test1","name":"order-numbers","start":123,"length": 10,"prefix":"INV-",suffix":"-ON"}' http://localhost:7000/sequence
```

List sequences:
```
curl http://localhost:7000/sequence
curl http://localhost:7000/sequence?namespace={namespace}
```

Get sequence:
```
curl http://localhost:7000/sequence/{id}
```

Get next value from sequence:
```
curl http://localhost:7000/sequence/{id}/next
```

Reset sequence to default start:
```
curl -X PATCH http://localhost:7000/sequence/{id}
```

Reset sequence to custom start (10):
```
curl -X PATCH -d '{"start":10}' http://localhost:7000/sequence/{id}
```

Delete sequence:
```
curl -X DEL http://localhost:7000/sequence/{id}
```


TODO
---
- UI: Complete error handling
- display details and 'get next' url
- allow sequence's name and namespace rename using PATCH
- Add confirmation dialog for deleting and resetting
- test config using external file
- test H2 as service (potential caching issue with h2 on disk) - check h2 server mode
- configure for postgres
- refactor persistence to store JSON objects rather than a relational data 
- USe JIB to create container
  - allow local file based h2 config
  - allow remote h2 and postgres config  
- host with the web service
- UI: searching / filtering by namespace


Optional:
- provide GraphQL API


