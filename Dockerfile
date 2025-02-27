#### Build
FROM maven:3.8.8-eclipse-temurin-21-alpine AS build

LABEL org.opencontainers.image.authors="eliangela@eliangela.dev.br"

WORKDIR /workspace/app

COPY .mvn .mvn
COPY pom.xml .
COPY src src

RUN mvn -DskipTests install

WORKDIR /workspace/app/
RUN mkdir -p target/dependency

WORKDIR /workspace/app/target/dependency
RUN jar -xf ../*.jar

#### Execution
FROM maven:3.8.8-eclipse-temurin-21-alpine

VOLUME /tmp
ARG DEPENDENCY=/workspace/app/target/dependency

RUN apk update && apk add --no-cache \
    ttf-dejavu \
    && rm -rf /var/lib/apk/lists/*

ENV TZ="America/Sao_Paulo"

ENV JAVA_HOME /usr/lib/jvm/java-21-openjdk-amd64/
RUN export JAVA_HOME

RUN export SPRING_PROFILES_ACTIVE
RUN export SPRING_DATASOURCE_URL
RUN export SPRING_DATASOURCE_USERNAME
RUN export SPRING_DATASOURCE_PASSWORD
RUN export MESSAGES_WHATSAPP_URL
RUN export MESSAGES_WHATSAPP_TOKEN
RUN export INVOICE_BASE_URL

RUN addgroup -S -g 10001 appusr && adduser -S -g 10001 appusr -G appusr
USER appusr

COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app

ENTRYPOINT ["java","-cp","app:app/lib/*","br.dev.eliangela.invoice_reminder.InvoiceReminderApplication"]
