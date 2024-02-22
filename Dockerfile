FROM container-registry.oracle.com/graalvm/native-image:21 AS builder
RUN microdnf install findutils
WORKDIR /rinha
COPY . /rinha
RUN ./gradlew clean build &&\
    ./gradlew nativeCompile

FROM container-registry.oracle.com/os/oraclelinux:9-slim
COPY --from=builder /rinha/build/native/nativeCompile/app app
EXPOSE 8080 8080
ENTRYPOINT ["/app", "-Xms64m", "-Xmx138m"]