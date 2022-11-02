ALIAS_VALUE=localhost # for clients it doesn't mater. You cane use common name like 'localhost' or write different aliases, you will get the same result.
TRUST_STORE_PASSWORD=34rtsTb290
KEY_STORE_PASSWORD=34rtsTb290
CA_CERT_PATH=../../../kafka-ssl/secret/ca-cert
CA_KEY_PATH=../../../kafka-ssl/secret/ca-key
D_NAME="CN=broker1, OU=Organisation, O=Organisation, L=City, ST=City, C=PL"

#Create the client [trust store] file:
keytool -keystore ./clienttruststore.jks -alias CARoot -import -file ${CA_CERT_PATH} -storepass ${TRUST_STORE_PASSWORD}

#Create and SetUp the client [key store] file.
#  1. Generate the key and certificate for the client using the keytool utility:
keytool -keystore ./clientkeystore.jks -alias ${ALIAS_VALUE} -validity 365 -genkey -keyalg RSA -storepass ${TRUST_STORE_PASSWORD} -keypass ${KEY_STORE_PASSWORD} -dname "${D_NAME}"

#  2. Extract the certificate from the [key store] and Sign its using the CA:
keytool -keystore ./clientkeystore.jks -alias ${ALIAS_VALUE} -certreq -file ./cert-file
openssl x509 -req -CA ${CA_CERT_PATH} -CAkey ${CA_KEY_PATH} -in ./cert-file -out ./cert-signed -days 365 -CAcreateserial

#  3. Import both the certificate of the CA and the signed certificate into the [key store]:
keytool -keystore ./clientkeystore.jks -alias CARoot -import -file ${CA_CERT_PATH}
keytool -keystore ./clientkeystore.jks -alias ${ALIAS_VALUE} -import -file ./cert-signed