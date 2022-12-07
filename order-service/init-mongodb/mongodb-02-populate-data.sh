#!/bin/bash

echo "########### Loading data to Mongo DB ###########"
mongoimport --jsonArray --db mutual-aid-app_MOCK --collection order-service_order --file /tmp/data/mock-data.json