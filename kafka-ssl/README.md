1. Run  "generate-SSL-CA-for-all-kafka-brokers-and-clients-script.sh"  to create common CA
2. Run  "generate-SSL-for-kafka-broker-script.sh"  to create SSL for broker
3. Run docker command  "docker-compose up"  from current dir to run kafka-docker container
4. Run  "generate-SSL-for-kafka-client-script.sh"  to create SSL for every client
5. For correct hosts mapping (clients -> docker kafka broker) you need to register kafka-docker-container hostname in /etc/hosts 
6. Run clients.