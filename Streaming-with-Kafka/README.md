# Task Definition
> > Run a simple java application which will act as a Kafka producer and sends ‘transaction’ events.The application will send at least 1000 transactions across 20 customers across a single topic. A second application in python that acts as a Kafka consumer to subscribe to the topic and upon receipt of each message logs a tuple of (account_number, sum(amount)), i.e. the total value of the transactions made by the customer while the application has been running. A protobuf to serialize the messages.

# SETUP INSTRUCTIONS
- Docker Version = 19.03.1
- Docker-Composer Version = 1.22.0
- python 3.6
- pip3
# Clone the repository
> - git clone https://gitlab.com/third-eye-dev-group/bank-project.git 
> - cd bank-project/
 - In the docker-compose.yml , change the **KAFKA_ADVERTISED_HOST_NAME: <docker_host_ip>** under service kafka-1 and kafka-2 to the host machine of the docker where you are running docker.
## Producer-Consumer Image build

> Build image of the java producer  application
- **cd Producer**
- **docker build -t producer/java .**
> Build the image of python consumer application
- **cd bank-project/consumer**
- **docker build -t kafka/consumer .**
- **python3 prouducer.py ##to generate the producer app**
- **python3 consumer.py  ##to generate the consumer app** 

# Generate Zookeeper, multi-broker Kafka and Java Producer application which are all a part of the docker-compose. 

> **For Linux Users:**
- docker-compose up -d

## Check if containers are up
- docker ps 
- docker logs kafka1
- docker logs kafka2
- docker logs zookeeper1
- docker logs zookeeper2
- docker logs producer --> In this log, you will see the serialised streams of the transactions to te brokers.

## Monitor and receive the transaction in the consumer application

> In a seperate shell, paste the below. You will observe the series of transactions are being logged.
- **docker run --rm kafka/consumer**

## Additional: If you would like to run the python producer application
**Please Note: Before performing this operation, comment out the producer section in the docker-compose.yml**
- cd producer
- docker build -t kafka/producer .
- docker run --rm kafka/producer


 
 
