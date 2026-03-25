IMAGE_NAME=task-api-logging

run:
	./mvnw spring-boot:run

test:
	./mvnw test

build:
	./mvnw package -DskipTests

docker-build:
	docker build -t $(IMAGE_NAME) .

docker-run:
	docker run -p 8080:8080 $(IMAGE_NAME)