FROM maven:3.9.9-amazoncorretto-21-al2023

WORKDIR /SocksApi
COPY . .
RUN mvn clean install -DskipTests
CMD mvn spring-boot:run