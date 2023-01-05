# Make serts by CA:
FROM ssl as ssl_helper
#make certs from CA with name serverkeystore.jks avd servertruststore.jks

# Transfer serts to current image and update kafka config:
FROM wurstmeister/kafka
#ARG CURRENT_DIR=${PWD}

ARG KAFKA_SSL_KEYSTORE_LOCATION='/etc/kafka/secrets/certs/serverkeystore.jks'
ARG KAFKA_SSL_TRUSTSTORE_LOCATION='/etc/kafka/secrets/certs/servertruststore.jks'
COPY --from=ssl_helper serverkeystore.jks ${KAFKA_SSL_KEYSTORE_LOCATION}
COPY --from=ssl_helper serverkeystore.jks ${KAFKA_SSL_KEYSTORE_LOCATION}
#RUN cd /opt/kafka/config
#RUN echo 'new_prop=${KAFKA_SSL_KEYSTORE_LOCATION}\nprop_second=${KAFKA_SSL_TRUSTSTORE_LOCATION}' > additional.properties
#RUN cat additional.properties >> server.properties
#
#RUN cd ${CURRENT_DIR}
