FROM openjdk:8-jre

WORKDIR /root

RUN curl -L https://github.com/Will3333/RoomMap-Client/releases/download/v0.2.0-BETA/roommap-client-backend-0.2.0-beta.tar --output roommap-client.tar && \
    tar -xvf roommap-client.tar && rm -f roommap-client.tar && \
    mv roommap-client-backend-0.2.0-beta roommap-client && \
    curl -L https://github.com/Will3333/RoomMap-Client/releases/download/v0.2.0-BETA/resources-0.2.0-beta.tar --output resources.tar && \
    tar -xvf resources.tar && rm -f resources.tar && \
    mv resources roommap-client/resources

WORKDIR /root/roommap-client

EXPOSE 80

CMD bin/roommap-client-backend -f /data/roommap-client.yml