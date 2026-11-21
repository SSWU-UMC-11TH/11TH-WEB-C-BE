#1. SpringBoot 빌드
FROM mcr.microsoft.com/openjdk/jdk:17-ubuntu AS build

WORKDIR /app

COPY . .

RUN chmod +x ./gradlew
RUN ./gradlew clean bootJar --no-daemon -x test

RUN find build/libs -maxdepth 1 -name "*.jar" \
    ! -name "*-plain.jar" \
    -exec cp {} /app.jar \;

#2. 실행단계 \
FROM mcr.microsoft.com/openjdk/jdk:17-ubuntu

WORKDIR /app

COPY --from=build /app.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]