FROM registry.cn-hangzhou.aliyuncs.com/choerodon-tools/javabase:0.6.0
RUN echo "Asia/shanghai" > /etc/timezone;
COPY hap.jar /hap.jar
ENTRYPOINT exec java -Xms512M -Xmx1024M  -jar /hap.jar