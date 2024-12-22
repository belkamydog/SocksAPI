FROM gradle:jdk17-corretto

WORKDIR /SocksApi
COPY . .
RUN ./gradlew clean build -x test
CMD ./gradlew bootRun