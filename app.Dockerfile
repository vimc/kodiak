ARG libsodium_image='docker.montagu.dide.ic.ac.uk:5000/openjdk-libsodium:master'
FROM $libsodium_image

ENV production=true

RUN apt-get update && apt-get -y install unzip

ENV VAULT_VERSION=0.8.3
ENV VAULT_ZIP=vault_${VAULT_VERSION}_linux_amd64.zip

RUN wget https://releases.hashicorp.com/vault/${VAULT_VERSION}/${VAULT_ZIP}
RUN unzip ${VAULT_ZIP}
RUN mv vault /usr/bin
RUN rm ${VAULT_ZIP}
