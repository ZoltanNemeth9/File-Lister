APP_NAME=file-lister
JAR_FILE=build/libs/*.jar

build:
	./gradlew build

run:
	podman run --rm --network=host file-lister