FROM shipilev/openjdk-shenandoah:8

COPY ./target/server.jar /app/
COPY ./deploy/docker/start.sh /app/
COPY ./deploy/docker/config.json.template /app/
COPY ./ui/ /app/ui/

RUN apt-get update && apt-get install -y gettext-base curl
RUN chmod +x /app/start.sh

# Install nvm with node and npm
ENV NVM_DIR /usr/local/nvm
ENV NODE_VERSION 11.11

RUN mkdir $NVM_DIR &&  curl https://raw.githubusercontent.com/nvm-sh/nvm/v0.35.3/install.sh | bash \
    && . $NVM_DIR/nvm.sh \
    && nvm install $NODE_VERSION \
    && nvm alias default $NODE_VERSION \
    && nvm use default

ENV NODE_PATH $NVM_DIR/versions/node/v$NODE_VERSION/lib/node_modules
ENV PATH      $NVM_DIR/versions/node/v$NODE_VERSION/bin:$PATH

RUN . $NVM_DIR/nvm.sh \
    && nvm which current \
    && cd /app/ui \
    && npm i \
    && npm i -g gulp \
    && gulp

WORKDIR /app
EXPOSE 80

CMD [ "/app/start.sh" ]