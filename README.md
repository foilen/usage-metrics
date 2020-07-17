# About

To report usage metrics of different resources on a machine.

License: The MIT License (MIT)


# Projects

- usage-metrics-agent: The agent that runs on all the machines to grab the metrics and to send them to the central service.
- usage-metrics-central: The web service that receives the metrics and generate reports by user.

# Usage

## Host API Key

The central application has a *hostKeySalt* that is used to generate the key. Like that, it is possible to add new hosts by
just letting them start sending information without having to whitelist them (if they have the derived key, then they are allowed).

If you do not provide a *hostKeySalt*, no key is needed.  

It is using `sha256(hostKeySalt + hostname)`.

Example:
- hostKeySalt: 37D5C
- hostname: h1.example.com
- hostnameKey: 22f606813cd8b680aed4282cdf4fe357b435ddd2777dc5619c658de56461f886

To generate it in Linux:
```
echo -n 37D5Ch1.example.com | sha256sum
```

## Compile

```
./create-local-release.sh
```

## MongoDB for testing

```
# Execute
docker run --rm --detach \
  --publish 27017:27017 \
  --name usage-mongo \
   mongo:4.0.12

# MongoShell
docker exec -ti usage-mongo mongo
	use usage
	show collections

# Add API user (test / test)
docker exec -i usage-mongo mongo << _EOF
  use usage
  db.apiUser.update({_id: 'test'}, 
  	{_id: 'test', authPasswordHash: '$(echo -n test | sha256sum | cut -d' ' -f 1)'}, 
  	{upsert: true})
_EOF

# Show all users
docker exec -i usage-mongo mongo << _EOF
  use usage
  db.apiUser.find()
_EOF

# Add owner
docker exec -i usage-mongo mongo << _EOF
  use usage
  db.ownerMapping.update({_id: 'infra'}, 
  	{_id: 'infra', ownerUsageResourceMappings: [] }, 
  	{upsert: true})
_EOF

# Add Mapping
docker exec -i usage-mongo mongo << _EOF
  use usage
  db.ownerMapping.update({_id: 'infra'}, 
  	{ \$push: {ownerUsageResourceMappings: {type: 'localDisk', owner: 'infra_web'} } }
  )
_EOF
 
# Show all Mapping 
docker exec -i usage-mongo mongo << _EOF
  use usage
  db.ownerMapping.find().pretty()
_EOF

```

## Agent

```
# Config
cat > _agent-config.json << _EOF
{
  "centralUri" : "https://usage.example.com",
  "hostname" : "h1.example.com",
  "hostnameKey" : "F5AAB679C",

  "diskSpaceRootFs" : "/hostfs/",
  
  "jamesDatabases" : [
  	{
	  	"host" : "127.0.0.1",
	  	"port" : 3306,
	  	"dbName" : "james",
	  	"dbUser" : "james",
	  	"dbPassword" : "qwerty"
	  }
  ]
}
_EOF

# Start
docker run -ti --rm \
  --volume $PWD:/local \
  --volume /:/hostfs \
  --volume /usr/bin/docker:/usr/bin/docker \
  --volume /usr/lib/x86_64-linux-gnu/libltdl.so.7.3.1:/usr/lib/x86_64-linux-gnu/libltdl.so.7 \
  --volume /var/run/docker.sock:/var/run/docker.sock \
  --workdir /local \
  usage-metrics-agent:master-SNAPSHOT /local/_agent-config.json

```

## Central

```
# Config
cat > _central-config.json << _EOF
{
  "hostKeySalt" : "KFJeufjf773jf",

  "mongoUri" : "mongodb://172.17.0.2:27017/usage"
}
_EOF

# Start
docker run -ti --rm \
  --volume $PWD:/local \
  --workdir /local \
  usage-metrics-central:master-SNAPSHOT /local/_central-config.json

```

## Get the report

```
curl http://127.0.0.1:8080/report/showReport \
	--header "Content-Type: application/json" \
	-X POST --data '{ "authUser": "test", "authKey": "test" }'
```

## Swagger

You can see the API documentation here: http://localhost:8080/swagger-ui/index.html
