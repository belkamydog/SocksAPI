FROM gradle:jdk21-corretto-al2023

WORKDIR /SocksApi
COPY . .
RUN ./gradlew clean build -x test
CMD ./gradlew bootRun