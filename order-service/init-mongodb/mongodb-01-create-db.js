// Create user
dbAdmin = db.getSiblingDB("admin");
dbAdmin.createUser({
    user: "testUser",
    pwd: "welcome!",
    roles: [{ role: "userAdminAnyDatabase", db: "admin" }],
    mechanisms: ["SCRAM-SHA-1"],
});

// Authenticate user
dbAdmin.auth({
    user: "testUser",
    pwd: "welcome!",
    mechanisms: ["SCRAM-SHA-1"],
    digestPassword: true,
});

// Create DB and collection
db = new Mongo().getDB("mutual-aid-app_MOCK");
db.createCollection("order-service_order", { capped: false });