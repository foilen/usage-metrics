FROM openjdk:11-jre-slim

RUN export TERM=dumb ; \
  apt-get update && apt-get install -y \
    haproxy=1.8.19-1+deb10u2 \
    supervisor=3.3.5-1 \
  && apt-get clean && rm -rf /var/lib/apt/lists/*

COPY app/ /app

WORKDIR /app
ENTRYPOINT ["/app/bin/usage-metrics-agent"]
