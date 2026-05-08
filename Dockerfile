# --- Stage 1: Build Stage ---
FROM maven:3.8.5-openjdk-17-slim AS build
WORKDIR /app

# Copy only the server folder where the Java code lives
COPY server/ /app/server/

# Move into the server directory to run the build
WORKDIR /app/server
RUN mvn clean package -DskipTests

# --- Stage 2: Run Stage ---
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# Copy the JAR from the build stage (adjust 'server/target' path)
COPY --from=build /app/server/target/*.jar app.jar

# Memory optimizations for Render Free Tier
ENV JAVA_OPTS="-Xmx300m -Xss512k -XX:MaxMetaspaceSize=128m -XX:+UseSerialGC"

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]