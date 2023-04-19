echo "${DOCKER_PASSWORD}" | docker login -u "${DOCKER_ID}" --password-stdin

# Order of building is very important. For example 'kafka-ca-ssl' must be built before 'kafka-ssl-broker1' and other
# images witch use that image inside.

docker build -t "${DOCKER_ID}"/kafka-ca-ssl -f ./kafka-ssl/Dockerfile.CA.SSL ./kafka-ssl
docker push "${DOCKER_ID}"/kafka-ca-ssl

docker build -t "${DOCKER_ID}"/kafka-ssl-broker1 -f ./kafka-ssl/Dockerfile.Kafka.SSL ./kafka-ssl
docker push "${DOCKER_ID}"/kafka-ssl-broker1

docker build -t "${DOCKER_ID}"/auth-service-boot ./auth-parent/auth-service-rest
docker push "${DOCKER_ID}"/auth-service-boot

docker build -t "${DOCKER_ID}"/profile-service-boot ./profile-service-parent/profile-service-rest
docker push "${DOCKER_ID}"/profile-service-boot

docker build -t "${DOCKER_ID}"/order-service-boot ./order-service
docker push "${DOCKER_ID}"/order-service-boot

docker build -t "${DOCKER_ID}"/event-storage-service-boot ./event-storage-service
docker push "${DOCKER_ID}"/event-storage-service-boot

docker build -t "${DOCKER_ID}"/message-chat-service-boot ./message-chat-service
docker push "${DOCKER_ID}"/message-chat-service-boot

docker build -t "${DOCKER_ID}"/task-executor-service-boot ./task-executor-service
docker push "${DOCKER_ID}"/task-executor-service-boot