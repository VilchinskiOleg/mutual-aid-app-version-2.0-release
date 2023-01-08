FROM tianxue04/jdk17-ubuntu:latest as builder
WORKDIR '/app'
COPY target/*.jar application.jar
RUN java -Djarmode=layertools -jar application.jar extract

FROM tianxue04/jdk17-ubuntu:latest
WORKDIR '/app'
COPY --from=builder /app/dependencies/ ./
COPY --from=builder /app/snapshot-dependencies/ ./
COPY --from=builder /app/spring-boot-loader/ ./
COPY --from=builder /app/application/ ./
CMD ["java", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005", "org.springframework.boot.loader.JarLauncher"]