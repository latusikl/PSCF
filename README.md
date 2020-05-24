# PSCF #
---
Sample tools to run it:
- Docker Mosquitto
```
 docker run -it -p 1883:1883 -p 9001:9001 -v mosquitto.conf:/$PWD/mosquitto/config/mosquitto.conf --name mqttBroker eclipse-mosquitto
```
- Mqtt CLI
 [Download](https://www.hivemq.com/blog/mqtt-cli/)
 
 - Configuration in Spring:
  - No password or login to broker
  - Default port 1883
  
  - Topics:
   - *pscf-in* on this topic *pscf-demo* listen for messages from mocked EK. Messages are sended each second from *pscf-demo-data-sender* after running app.
   - *pscf-out* on this topic *pscf-demo* sends JSON object based on InputBrokerDto one per minute. You can read this data from MosquittoCLI:
   ```
   mqtt sub -t pscf-out
   ```
