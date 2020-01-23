FROM openjdk:17-alpine
RUN echo "Asia/Shanghai" > /etc/timezone
ADD ./build/libs/server.jar /root
WORKDIR /root
CMD ["java", "-jar", "server.jar"]
