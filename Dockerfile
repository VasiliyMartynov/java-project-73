FROM gradle:jdk20

WORKDIR /app

COPY ./ .

RUN ./gradlew installDist

CMD build/install/app/bin/app