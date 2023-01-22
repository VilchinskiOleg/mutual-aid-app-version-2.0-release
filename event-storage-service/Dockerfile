# only use as a container for CA:
FROM alehvilchynski/kafka-ca-ssl as ssl_ca_container

FROM adoptopenjdk/openjdk11 as builder
WORKDIR '/app'
COPY target/*.jar application.jar
RUN java -Djarmode=layertools -jar application.jar extract

FROM adoptopenjdk/openjdk11
WORKDIR '/app'
COPY --from=builder /app/dependencies/ ./
COPY --from=builder /app/snapshot-dependencies/ ./
COPY --from=builder /app/spring-boot-loader/ ./
COPY --from=builder /app/application/ ./
COPY --from=ssl_ca_container /certs/ca-key.key .
COPY --from=ssl_ca_container /certs/ca-cert.crt .
COPY kafka-ssl/secret/*.sh script.sh
RUN ./script.sh
CMD ["java", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005", "org.springframework.boot.loader.JarLauncher"]