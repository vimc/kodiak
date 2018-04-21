FROM libsodium

# Install docker
RUN apt-get update
RUN apt-get install -y \
        apt-transport-https \
        ca-certificates \
        curl \
        software-properties-common
RUN curl -fsSL https://download.docker.com/linux/debian/gpg | apt-key add -
RUN add-apt-repository \
   "deb [arch=amd64] https://download.docker.com/linux/debian \
   $(lsb_release -cs) \
   stable"
RUN apt-get update
# This outputs available Docker versions - useful for choosing a new version
# if the pinned version is deprecated
RUN apt-cache madison docker-ce
RUN apt-get install -y docker-ce=17.12.0~ce-0~debian

# Setup gradle
COPY gradlew /kodiak/
COPY gradle /kodiak/gradle/
WORKDIR /kodiak
RUN ./gradlew

# Pull in dependencies
COPY ./build.gradle /kodiak/
COPY ./config/ /kodiak/config/
COPY ./scripts/ /kodiak/scripts/
RUN ./gradlew

# Copy source
COPY ./src/ /kodiak/src/

RUN /kodiak/scripts/create-test-config.sh

CMD ./gradlew test :distDocker
