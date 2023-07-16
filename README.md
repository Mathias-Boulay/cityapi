# Cityapi

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
./gradlew quarkusDev
```

## Questions

### 1

See `compose.yml` file.

### 2

The database schema and the data is populated using [flyway](https://flywaydb.org/).
The migrations are located in `src/main/resources/db/migration` folder.

### 3

Populate the .env file according to the .env.example file.

Execute `docker compose up -d` and test the application in your browser:

- GET http://localhost:8080/city
- POST http://localhost:8080/city

Sadly, the service is not configurable using the required environment variables. We did not find a way
to change the environment variable names.

### 4

The tests are located in `src/test/java/com/airhacks/cityapi/CityResourceE2E.java` file. However, for some
god forsaken reason, the tests are not executed when running `./gradlew test` command. We did not find a way
to make the tests run.

### 5

See `Dockerfile-quarkus`.

It is a multi stage build. The first stage builds the application using gradle. The second stage copies the
application to a new image and runs it.

It uses graalvm to build a native image.

### 6, 7, 8, 9

See `.github/workflows/ci.yml` file.

### 10

See `.github/workflows/release.yml` file.

### 11

We chose to use k3d instead of minikube because it is easier to use and it is faster.

See `k3d.sh` file.

## 12

The chart is located in `cityapi` folder.

It creates a service, a deployment and an ingress. The ingress is configured to use the `cityapi.local` domain.

It also creates a postgresql database.

## 13

`k3d cluster create cityapi --api-port 6550 -p "1024:8080@loadbalancer" --agents 2`

## 14

`helm install cityapi cityapi`

Note: the app starts but outputs no logs. We did not find a way to make it output logs.

