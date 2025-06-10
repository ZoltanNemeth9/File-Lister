#!/bin/bash
# exit the program if something happens
set -e

# Create the required two postGre containers. I'm sorry but I forgot what was the function of the second one.
# I chose to replace them if they are already exist
podman run --replace -d --name pg-history -e POSTGRES_PASSWORD=historypwd -e POSTGRES_DB=historydb -p 8081:5432 docker.io/library/postgres
podman run --replace -d --name pg-dump -e POSTGRES_PASSWORD=dumppwd -e POSTGRES_DB=dumpdb -p 8082:5432 docker.io/library/postgres

# just in case I put a sleep here seems like it is not necesarry though.
echo "Waiting for PostgreSQL containers to initialize..."
sleep 10

# I create the schema everytime
podman cp init-db.sql pg-history:/init-db.sql
podman exec -it pg-history psql -U postgres -d historydb -f /init-db.sql

# I had to give rights so I could build it
chmod +x gradlew
# Build the application using podman and build the application container
make build
podman build -t file-lister .
#run it
make run


echo "✅ Setup complete."
echo "Swagger UI: http://localhost:8080/swagger-ui/index.html"