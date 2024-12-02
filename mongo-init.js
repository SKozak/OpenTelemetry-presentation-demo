print('Start #################################################################');
print('creatig fraud-service and user');
db.createUser({
    user: "fraud-app",
    pwd: "fraud-app",
    roles: [
        {
            role: "readWrite",
            db: "fraud-service",
        },
    ],
});
print("creatig fraud-service user ended")

print('creatig analyzes database and user');
db = db.getSiblingDB('analyzes');
db.createUser({
    user: "analyzes-app",
    pwd: "analyzes-app",
    roles: [
        {
            role: "readWrite",
            db: "analyzes",
        },
    ],
});
print("creatig analyzes database and user ended")

print('End #################################################################');
