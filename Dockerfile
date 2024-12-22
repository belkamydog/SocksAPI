FROM ubuntu:jammy

WORKDIR /SocksApi
COPY . .
RUN gradle -Dskip.tests build
CMD ./gradlew bootRun