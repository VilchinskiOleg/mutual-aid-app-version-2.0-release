ALIAS_VALUE=broker1 # it should be broker's host name.
TRUST_STORE_PASSWORD=34rtsTb290
KEY_STORE_PASSWORD=34rtsTb290
D_NAME="CN=broker1, OU=Organisation, O=Organisation, L=City, ST=City, C=PL"

#Generate and SetUp the SSL [key store] :
#  1. Generate the key and certificate for the client using the keytool utility:
keytool -keystore ./serverkeystore.jks -alias ${ALIAS_VALUE} -validity 365 -genkey -keyalg RSA -storepass ${TRUST_STORE_PASSWORD} -keypass ${KEY_STORE_PASSWORD} -dname "${D_NAME}"

#  2. Extract the certificate from the [key store] and Sign its using the CA:
keytool -keystore ./serverkeystore.jks -alias ${ALIAS_VALUE} -certreq -file ./cert-file
openssl x509 -req -CA ../ca-cert -CAkey ../ca-key -in ./cert-file -out ./cert-signed -days 365 -CAcreateserial -extfile ../openssl.cnf -extensions req_ext

#  3. Import both the certificate of the CA and the signed certificate into the [key store]:
keytool -keystore ./serverkeystore.jks -alias CARoot -import -file ../ca-cert
keytool -keystore ./serverkeystore.jks -alias ${ALIAS_VALUE} -import -file ./cert-signed

#Generate the SSL [trust store] for the Kafka brokers:
keytool -keystore ./servertruststore.jks -alias CARoot -import -file ../ca-cert -storepass ${TRUST_STORE_PASSWORD}