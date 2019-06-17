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
* Event Service (horizontal scaling)
* Event Tagging Service (singleton)
* Time Curve Object (horizontal scaling)
* Time Curve Balances and Turnovers (horizontal scaling)

One common persistence for all services

## Service Apis
### Time Curve Event

### Time Curve Object

### Time Curve Balances and Turnovers

--------------------------
## Docker local
docker build -t timecurve-manager .
docker tag timecurve-manager chrisburki/timecurve-manager
docker push chrisburki/timecurve-manager:latest
docker run --name timecurve-manager -p 8090:8080 -d timecurve-manager

--------------------------
## GCP

### Kubernetes cluster (project-id: 281928467963)


#### create cluster
gcloud config set project buc-personal-banking
gcloud config set compute/zone europe-west3-c
gcloud container clusters create timecurve --machine-type=g1-small --disk-size=30GB --num-nodes=1
gcloud container clusters get-credentials timecurve

#### delete cluster
gcloud container clusters delete timecurve

#### enable apis
gcloud services enable sqladmin.googleapis.com


#### create secrets & deployment
kubectl create secret generic cloudsql-instance-credentials --from-file=credentials.json=cloudsql-timecurves-credentials.json
kubectl create secret generic cloudsql-db-credentials --from-literal username=postgres --from-literal password=buc

kubectl create -f "C:\dev\timecurve\manager\k8s\timecurve-manager.yaml"

#### delete deployment
kubectl delete -f "C:\dev\timecurve\manager\k8s\timecurve-manager.yaml"
kubectl delete secret cloudsql-db-credentials
kubectl delete secret cloudsql-instance-credentials

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
kubectl create secret generic cloudsql-instance-credentials --from-file=cloudsql-timecurves-credentials.json

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

--------------------------
## Helpers
https://github.com/spring-cloud/spring-cloud-gcp/tree/master/spring-cloud-gcp-samples/spring-cloud-gcp-data-jpa-sample
https://www.springboottutorial.com/spring-boot-and-h2-in-memory-database
https://cloud.google.com/sdk/gcloud/reference/sql/databases/create
https://spring.io/blog/2018/08/23/bootiful-gcp-relational-data-access-with-spring-cloud-gcp-2-8
https://reflectoring.io/unit-testing-spring-boot/
https://github.com/eventuate-tram/eventuate-tram-sagas

-- transaction handling
https://dzone.com/articles/transaction-synchronization-and-spring-application



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

