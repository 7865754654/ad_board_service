FROM amazoncorretto:17-alpine AS build
RUN apk add --no-cache gradle
WORKDIR /app
COPY . .
RUN gradle clean build -x test

FROM amazoncorretto:17-alpine
WORKDIR /app
COPY --from=build /app/app/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]