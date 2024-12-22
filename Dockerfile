FROM gradle:jdk8-corretto-al2023

WORKDIR /SocksApi
COPY . .
RUN gradle -Dskip.tests build
CMD ./gradlew bootRun