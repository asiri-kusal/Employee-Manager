FROM openjdk:8-jre-slim
VOLUME /tmp
ARG JAR_FILE
ARG PORT
ENV _JAVA_OPTIONS="-Xms1024m -Xmx2500m -XX:MaxMetaspaceSize=256m"
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
ENV TZ=Asia/Colombo
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
EXPOSE ${PORT}