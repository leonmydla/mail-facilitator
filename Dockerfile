FROM eclipse-temurin/17-jre-jammy

RUN mkdir /opt/mms && \
  cd /opt/mms

COPY target/mms-0.0.1-SNAPSHOT.jar /opt/mms/mms.jar

CMD java -jar /opt/mms/mms.jar
