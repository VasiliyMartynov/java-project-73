setup:
	gradle wrapper --gradle-version 8.3

clean:
	./gradlew clean

build:
	./gradlew clean build

start:
	make -C frontend start
	./gradlew bootRun --args='--spring.profiles.active=dev'

start_back:
	./gradlew bootRun --args='--spring.profiles.active=dev'

start-prod:
	./gradlew bootRun --args='--spring.profiles.active=prod'

install:
	./gradlew installDist

start-dist:
	./build/install/app/bin/app

lint:
	./gradlew checkstyleMain checkstyleTest

test:
	./gradlew test

report:
	./gradlew jacocoTestReport

check-updates:
	./gradlew dependencyUpdates

generate-migrations:
	gradle diffChangeLog

db-migrate:
	./gradlew update


.PHONY: build