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

Run as a docker container (data will not be persisted between container restarts yet)
```
docker run --publish 7000:7000 softwaremrozek/seqid
```
The folder `docker-compose` contains 2 files with examples of docker configuration:
1. Service using an embedded h2 database with a local `db` folder bound to the db folder inside docker container where h2 database files will be stored.
   ```
   docker compose -f embedded-h2.yml up
   ```
2. Service using an external postgres with password defined in an environment variable `DB_PASS` (below supplied via `.env.local` file, not committed) 
   ```
   docker compose -f external-postgres.yml --env-file .env.local up
   ```

TODO
---
- OPS: allow local file based h2 config
- OPS: configure and test for external postgres
- Docker: test remote postgres config
- UI: Complete error handling (error codes from the server?)
- UI: Add confirmation dialog for deleting and resetting
- UI: searching / filtering by namespace
- UI: layout on small screens
- UX: allow sequence's name and namespace rename using PATCH
- CODE: refactor persistence to store JSON objects rather than a relational data 
- OPS: graalvm - generate native image
- OPS: hots in the cloud

Optional:
- OPS: provide GraphQL API


