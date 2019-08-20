# About

To report usage metrics of different resources on a machine.

License: The MIT License (MIT)


# Projects

- usage-metrics-agent: The agent that runs on all the machines to grab the metrics and to send them to the central service.
- usage-metrics-central: The web service that receives the metrics and generate reports by user.

# Usage

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

```

## Agent

```
# Config
cat > _agent-config.json << _EOF
{
  "centralUri" : "https://usage.example.com",
  "hostname" : "h1.example.com",
  "hostnameKey" : "F5AAB679C",
  
  "diskSpaceRootFs" : "/hostfs/"
}
_EOF

# Start
docker run -ti --rm \
  --volume $PWD:/local \
  --volume /:/hostfs \
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
  --volume /:/hostfs \
  --workdir /local \
  usage-metrics-central:master-SNAPSHOT /local/_central-config.json

```
