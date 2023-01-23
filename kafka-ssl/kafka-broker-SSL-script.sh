# Broker's host name:
ALIAS_VALUE=kafka
# Broker's description:
D_NAME="CN=kafka, OU=Organisation, O=Organisation, L=City, ST=City, C=PL"

TRUST_STORE_PASSWORD="34rtsTb290"
KEY_STORE_PASSWORD="34rtsTb290"



#  1. Generate the key and certificate for the client using the keytool utility:
keytool -keystore ./serverkeystore.jks -alias ${ALIAS_VALUE} -validity 365 -genkey -keyalg RSA -storepass ${TRUST_STORE_PASSWORD} -keypass ${KEY_STORE_PASSWORD} -dname "${D_NAME}"

#  2. Extract the certificate from the [key store] and Sign its using the CA:
keytool -keystore ./serverkeystore.jks -alias ${ALIAS_VALUE} -certreq -file ./cert-file -storepass ${KEY_STORE_PASSWORD}
openssl x509 -req -CA ./ca-cert.crt -CAkey ./ca-key.key -in ./cert-file -out ./cert-signed -days 365 -CAcreateserial

#  3. Import both the certificate of the CA and the signed certificate into the [key store]:
# [*] will ask => if you trust that cert:
keytool -keystore ./serverkeystore.jks -alias CARoot -import -file ./ca-cert.crt -storepass ${KEY_STORE_PASSWORD} -noprompt
keytool -keystore ./serverkeystore.jks -alias ${ALIAS_VALUE} -import -file ./cert-signed -storepass ${KEY_STORE_PASSWORD}

#  4. Generate the SSL trust store for the Kafka brokers:
# [*] will ask => if you trust that cert:
keytool -keystore ./servertruststore.jks -alias CARoot -import -file ./ca-cert.crt -storepass ${TRUST_STORE_PASSWORD} -noprompt