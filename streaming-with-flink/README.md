# Task Definition
> Use docker-compose to setup a local Apache Flink cluster and create an application that: ● streams ​this bank transaction data​ through the Flink cluster ● Aggregates the data using flink’s windowing functionality and then reports customers who have total transactions of more than IDR 1,000,000,000 in a month. Create a SQL script that performs that same functionality as the application. 
 
# SETUP INSTRUCTIONS
- Docker Version = 19.03.1
- Docker-Composer Version = 1.22.0
- Apache Maven 3.6.0
- openjdk version "11.0.7"
# Clone the repositoy
-  git clone https://gitlab.com/third-eye-dev-group/streaming-with-flink.git
- cd streaming-with-flink

# Build the java application to stream the bank transaction data through flink cluster
- cd streaming-with-flink
- mvn clean compile install package
**Run the jar file under target folder**
- java -jar target/spend-report-0.1.jar

# Generate a Job Manager and a Task Manager
- docker-compose up -d
**Check if the containers are up**
- docker ps
- docker logs spend-report_taskmanager_1
- docker logs spend-report_jobmanager_1
** Access the Flink dashboard**
- http://<you-host-ip-here>:8081

# To run the Flink Job
- JOBMANAGER_CONTAINER=$(docker ps --filter name=jobmanager --format={{.ID}})
- docker exec -t -i "$JOBMANAGER_CONTAINER" flink run /spend-report-0.1.jar
- docker exec -it spend-report_jobmanager_1  /bin/sh
- inside the report_jobmanager_1 container a mounted volume will be present where the output.csv will is present(in our case its /demo)

