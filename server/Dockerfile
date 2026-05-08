# --- Stage 1: Build Stage ---
FROM maven:3.8.5-openjdk-17-slim AS build
WORKDIR /app

# Since the Dockerfile is in the 'server' folder, 
# we copy everything from the current directory (.) into the image
COPY . .

# Compile and package the JAR
RUN mvn clean package -DskipTests

# --- Stage 2: Run Stage ---
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# Copy the JAR from the build stage target folder
COPY --from=build /app/target/*.jar app.jar

# Memory optimizations for Render Free Tier (512MB RAM)
# -Xmx300m: Keeps the heap small to avoid OOM crashes
# -XX:+UseSerialGC: Saves CPU and memory overhead
ENV JAVA_OPTS="-Xmx300m -Xss512k -XX:MaxMetaspaceSize=128m -XX:+UseSerialGC"

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]