ARG libsodium_image='docker.montagu.dide.ic.ac.uk:5000/openjdk-vault-libsodium:master'
FROM $libsodium_image

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
RUN apt-get install -y docker-ce=17.12.1~ce-0~debian

# Setup gradle
COPY gradlew /kodiak/
COPY gradle /kodiak/gradle/
WORKDIR /kodiak
RUN ./gradlew

# Pull in dependencies
COPY ./build.gradle /kodiak/
RUN ./gradlew

# Copy source
COPY ./src/ /kodiak/src/
COPY ./scripts/ /kodiak/scripts/

ARG git_id='UNKNOWN'
ARG git_branch='UNKNOWN'
ARG registry=vimc
ARG name=kodiak

ENV GIT_ID $git_id
ENV APP_DOCKER_TAG $registry/$name
ENV APP_DOCKER_COMMIT_TAG $registry/$name:$git_id
ENV APP_DOCKER_BRANCH_TAG $registry/$name:$git_branch

CMD ./gradlew test :distDocker -i -Dproduction=true -Pdocker_version=$GIT_ID -Pdocker_name=$APP_DOCKER_TAG \
     && docker tag $APP_DOCKER_COMMIT_TAG $APP_DOCKER_BRANCH_TAG \
     && docker push $APP_DOCKER_BRANCH_TAG
