#gcloud builds submit --tag eu.gcr.io/buc-personal-banking/timecurveobject-manager .
FROM openjdk:11-jre-slim
VOLUME /tmp
ARG JAR_FILE=build/libs/timecurve-manager-0.1.0.jar
ADD ${JAR_FILE} timecurve-manager.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/timecurve-manager.jar"]