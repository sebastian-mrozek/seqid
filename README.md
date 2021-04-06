Sample curl commands
---
Create new sequence:
```
curl -X POST -d '{"namespace":"test1","name":"order-numbers","start":100000}' http://localhost:7000/sequence
```

List sequences:
```
curl http://localhost:7000/sequence
curl http://localhost:7000/sequence/{namespace}
```

Get sequence:
```
curl http://localhost:7000/sequence/{id}
```

Reset sequence to default start:
```
curl -X POST http://localhost:7000/sequence/{id}
```

Reset sequence to custom start:
```
curl -X PATCH -d '10' http://localhost:7000/sequence/{id}
```

Delete sequence:
```
curl -X DEL http://localhost:7000/sequence/{id}
```


TODO
---
- test config using external file
- test H2 as service   
- USe JIB to create container
  - allow local file based h2 config
  - allow remote h2 and postgres config  
- build simple UI as a separate project
- host with the web service

Optional:
- provide GraphQL API


