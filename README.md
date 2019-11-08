# Time Curve Service

## Description
Time curve service is a service to managing balance curves. It provides
* multi curve per object
* labeling of each curve
* seamless behaviour of the curve over time
* fast horizontal scalable putting of events (changes of he curve)
* fast querying
* multi dimensional dates per curve

## Architecture Diagram
![Alt text]()


## Service Diagram
* Event Manager Service (Timecurve Objec, Event & Approved Balance)
* GSN Service (singleton)
* Time Curve Balances and Turnovers (horizontal scaling)


## Service Apis
### Time Curve Event

### Time Curve Object

### Time Curve Balances and Turnovers


--------------------------
## GCP

### Kubernetes cluster (project-id: 281928467963)


#### create cluster
gcloud config set project buc-personal-banking
gcloud config set compute/zone europe-west3-c
gcloud container clusters create timecurve --machine-type=g1-small --disk-size=30GB --num-nodes=1
gcloud container clusters get-credentials timecurve
kubectl cluster-info

#### enable apis
gcloud services enable sqladmin.googleapis.com
gcloud services enable pubsub.googleapis.com

#### create secrets
kubectl create secret generic timecurve-cloudsql-credentials --from-file=cloudsql-credentials.json=cloudsql-timecurves-credentials.json
kubectl create secret generic timecurve-db-credentials --from-literal username=postgres --from-literal password=buc
kubectl create secret generic timecurve-pubsub-credentials --from-file=pubsub-credentials.json=pubsub-timecurve-credentials.json

#### create deployment
kubectl create -f "C:\dev\timecurve\manager\k8s\timecurve-manager.yaml"
kubectl create -f "C:\dev\timecurve\baltov\k8s\timecurve-baltov.yaml"

#### delete deployment
kubectl delete -f "C:\dev\timecurve\manager\k8s\timecurve-manager.yaml"
kubectl delete -f "C:\dev\timecurve\baltov\k8s\timecurve-baltov.yaml"

#### delete secrets
kubectl delete secret timecurve-cloudsql-credentials
kubectl delete secret timecurve-db-credentials
kubectl delete secret timecurve-pubsub-credentials

#### delete cluster
gcloud container clusters delete timecurve


### cloud pubsub

#### enable apis
gcloud services enable pubsub.googleapis.com

#### service account
gcloud iam service-accounts create timecurves-pubsub-app --display-name "timecurves-pubsub-app"
gcloud projects add-iam-policy-binding buc-personal-banking --member "serviceAccount:timecurves-pubsub-app@buc-personal-banking.iam.gserviceaccount.com" --role "roles/pubsub.admin"
gcloud iam service-accounts keys create pubsub-timecurve-credentials.json --iam-account timecurves-pubsub-app@buc-personal-banking.iam.gserviceaccount.com

-- need to be generated each time, service account not
kubectl create secret generic timecurve-pubsub-credentials  --from-file=pubsub-credentials.json=timecurve-pubsub-credentials.json


### cloud sql

#### create db
gcloud sql instances create timecurves --tier=db-f1-micro --database-version=POSTGRES_9_6 --region=europe-west3
gcloud sql users set-password postgres --instance timecurves --password buc
gcloud sql databases create timecurves --instance timecurves
--to test: gcloud sql connect timecurves --user=postgres --quiet

#### Postgres database
https://cloud.google.com/sql/docs/postgres/connect-kubernetes-engine?hl=de
gcloud sql users create [DBUSER] --instance=[INSTANCE_NAME] --password=[PASSWORD]
https://codelabs.developers.google.com/codelabs/cloud-postgresql-gke-memegen/#0
https://chartio.com/resources/tutorials/how-to-list-databases-and-tables-in-postgresql-using-psql/
list databases: \l
change database: \c timecurves
list tables: \dt

#### service account
gcloud iam service-accounts create timecurves-sql-app --display-name "timecurves-sql-app"
gcloud projects add-iam-policy-binding buc-personal-banking --member "serviceAccount:timecurves-sql-app@buc-personal-banking.iam.gserviceaccount.com" --role "roles/cloudsql.admin"
gcloud iam service-accounts keys create cloudsql-timecurves-credentials.json --iam-account timecurves-sql-app@buc-personal-banking.iam.gserviceaccount.com

-- need to be generated each time, service account not
kubectl create secret generic timecurve-cloudsql-credentials --from-file=cloudsql-credentials.json=cloudsql-timecurves-credentials.json

#### sql instance
-- stop sql instance
gcloud sql instances patch timecurves --activation-policy NEVER
-- start sql instance
gcloud sql instances patch timecurves --activation-policy ALWAYS
-- restart sql instance
gcloud sql instances restart timecurves


### cloud build
https://cloud.google.com/cloud-build/docs/quickstart-docker
gcloud builds submit --tag eu.gcr.io/buc-personal-banking/timecurve-manager .

### gcloud sdk
https://cloud.google.com/sdk/gcloud/reference/components/update
--update to latest version run in the sdk window: gcloud components update
--to init: gcloud init

### kubernetes dashboard

#### steps
kubectl apply -f https://raw.githubusercontent.com/kubernetes/dashboard/v2.0.0-beta4/aio/deploy/recommended.yaml
kubectl proxy
http://localhost:8001/api/v1/namespaces/kubernetes-dashboard/services/https:kubernetes-dashboard:/proxy/

#### links
https://kubernetes.io/docs/tasks/access-application-cluster/web-ui-dashboard/

--------------------------
## Docker Local (Integration) with bitnami Kafka
docker build -t timecurve-manager .
docker build -t timecurve-baltov .
docker pull mongo
docker build -t payment .

** create a network "tcmgr-kafka"
docker network create tcmgr-kafka --driver bridge

** start zookeeper
docker run -d --name zookeeper --network tcmgr-kafka -e ALLOW_ANONYMOUS_LOGIN=yes -p 2181:2181 bitnami/zookeeper:latest

** start kafka
docker run -d --name kafka --network tcmgr-kafka -e KAFKA_ZOOKEEPER_CONNECT=zookeeper:2181 -e ALLOW_PLAINTEXT_LISTENER=yes -p 9092:9092 bitnami/kafka:latest

** create topics "domain-event-booking", "command-booking"
docker run --rm --network tcmgr-kafka -e KAFKA_ZOOKEEPER_CONNECT=zookeeper:2181 bitnami/kafka:latest kafka-topics.sh --create --topic domain-event-booking --replication-factor 1 --partitions 1 --zookeeper zookeeper:2181

docker run --rm --network tcmgr-kafka -e KAFKA_ZOOKEEPER_CONNECT=zookeeper:2181 bitnami/kafka:latest kafka-topics.sh --create --topic command-booking --replication-factor 1 --partitions 1 --zookeeper zookeeper:2181

docker run --rm --network tcmgr-kafka -e KAFKA_ZOOKEEPER_CONNECT=zookeeper:2181 bitnami/kafka:latest kafka-topics.sh --create --topic payment-reply-booking --replication-factor 1 --partitions 1 --zookeeper zookeeper:2181

** list all topics
docker run --rm --network tcmgr-kafka -e KAFKA_ZOOKEEPER_CONNECT=zookeeper:2181 bitnami/kafka:latest kafka-topics.sh --list --zookeeper zookeeper:2181

** inspect one topic
docker run --name kafka-consumer --network tcmgr-kafka -e KAFKA_ZOOKEEPER_CONNECT=zookeeper:2181 bitnami/kafka:latest kafka-console-consumer.sh --topic payment-reply-booking --from-beginning --bootstrap-server kafka:9092


** create publisher
docker run --rm --name kafka-publisher --interactive --network tcmgr-kafka -e KAFKA_ZOOKEEPER_CONNECT=zookeeper:2181 bitnami/kafka:latest kafka-console-producer.sh --topic domain-booking-booking --broker-list kafka:9092

** create consumer
docker run --rm --name kafka-consumer --network tcmgr-kafka -e KAFKA_ZOOKEEPER_CONNECT=zookeeper:2181 bitnami/kafka:latest kafka-console-consumer.sh --topic domain-booking-booking --from-beginning --bootstrap-server kafka:9092

** create timecurve-manager
docker run -d --name timecurve-manager --network tcmgr-kafka -p 8081:8080 timecurve-manager:latest
docker run --name timecurve-manager --network tcmgr-kafka -p 8081:8080 timecurve-manager:latest

** create timecurve-baltov
docker run -d --name timecurve-baltov --network tcmgr-kafka -p 8082:8080 timecurve-baltov:latest
docker run --name timecurve-baltov --network tcmgr-kafka -p 8082:8080 timecurve-baltov:latest

** start mongo db
docker run -d --name mongo-payment --network tcmgr-kafka -p 27000:27017 mongo:latest

** create payment
docker run -d --name payment --network tcmgr-kafka --link=mongo-payment -p 8083:8080 payment:latest
docker run --name payment --network tcmgr-kafka --link=mongo-payment -p 8083:8080 payment:latest

** cleanup
FOR /f "tokens=*" %i IN ('docker ps -q') DO docker stop %i
FOR /f "tokens=*" %i IN ('docker container ls -a -q') DO docker rm %i

### mongo in docker
http://www.littlebigextra.com/how-to-connect-to-spring-boot-rest-service-to-mongo-db-in-docker/

** bash into the container
docker exec -i -t mongo-payment bash

** connect to local mongo
mongo

show dbs
use [db-name] 
show collections
db.[collection-name].find( {} )


--------------------------
## Helpers
https://github.com/spring-cloud/spring-cloud-gcp/tree/master/spring-cloud-gcp-samples/spring-cloud-gcp-data-jpa-sample
https://www.springboottutorial.com/spring-boot-and-h2-in-memory-database
https://cloud.google.com/sdk/gcloud/reference/sql/databases/create
https://spring.io/blog/2018/08/23/bootiful-gcp-relational-data-access-with-spring-cloud-gcp-2-8
https://reflectoring.io/unit-testing-spring-boot/
https://github.com/eventuate-tram/eventuate-tram-sagas
https://blog.ippon.tech/boost-the-performance-of-your-spring-data-jpa-application/
https://blog.restcase.com/5-basic-rest-api-design-guidelines/
https://restfulapi.net/resource-naming/

-- transaction handling
https://dzone.com/articles/transaction-synchronization-and-spring-application

-- jpa performance
https://thoughts-on-java.org/jpa-generate-primary-keys/


-- different datasources
https://www.credera.com/blog/technology-insights/java/gradle-profiles-for-multi-project-spring-boot-applications/

https://riptutorial.com/spring-boot/example/21856/dev-and-prod-environment-using-different-datasources
https://itnext.io/switching-build-configurations-in-spring-boot-e7a607c6af82
https://dzone.com/articles/setup-multiple-datasources-with-spring-boot-and-sp

-- graphql
https://medium.com/maxime-heckel/building-a-graphql-wrapper-for-the-docker-api-2109f2b9c202

-- bpmn
https://medium.com/@venkat.y/running-workflow-as-a-microservice-25781ecf8155


Log4j
    implementation('org.springframework.boot:spring-boot-starter-log4j2')

-- Main app class
```java
@SpringBootApplication
@EnableTransactionManagement(proxyTargetClass = true)
@Slf4j
public class AccountManagementApp {


  @PostConstruct
  public void init() {
    Properties props = System.getProperties();
    log.info("Java vendor: {}, Java VM: {}, Java version: {}, Java home: {}, "
                    + "OS name: {}, OS version: {}, OS architecture: {}",
            props.get("java.vendor"),
            props.get("java.vm.name"),
            props.get("java.vm.version"),
            props.get("java.home"),
            props.get("os.name"),
            props.get("os.version"),
            props.get("os.arch")
    );
  }

