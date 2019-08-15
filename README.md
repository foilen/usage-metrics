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

## Agent

```
# Config
cat > _agent-config.json << _EOF
{
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
