FROM shipilev/openjdk-shenandoah:8

COPY ./target/server.jar /app/
COPY ./deploy/docker/start.sh /app/
COPY ./deploy/docker/config.json.template /app/
COPY ./ui/build/ /app/ui/

RUN apt-get update && apt-get install -y gettext-base
RUN chmod +x /app/start.sh

WORKDIR /app
EXPOSE 80

CMD [ "/app/start.sh" ]