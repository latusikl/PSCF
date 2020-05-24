# PSCF #
---
Sample tools to run it:
- Docker Mosquitteo
 docker run -it -p 1883:1883 -p 9001:9001 -v mosquitto.conf:/$PWD/mosquitto/config/mosquitto.conf --name mqttBroker eclipse-mosquitto


brew install hivemq/mqtt-cli/mqtt-cli
