FROM jenkins/jenkins:lts

USER root

# Installer dépendances système (wget, maven, unzip, docker, jq)
RUN apt-get update && apt-get install -y \
    wget \
    maven \
    unzip \
    ca-certificates \
    docker.io \
    jq \
    && rm -rf /var/lib/apt/lists/*

# Installer AWS CLI v2
RUN curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip" \
    && unzip awscliv2.zip \
    && ./aws/install \
    && rm -rf awscliv2.zip aws

# Installer JDK 21 (Oracle)
RUN wget https://download.oracle.com/java/21/latest/jdk-21_linux-x64_bin.tar.gz -O /tmp/jdk-21_linux-x64_bin.tar.gz \
    && mkdir -p /opt/java \
    && tar -xzf /tmp/jdk-21_linux-x64_bin.tar.gz -C /opt/java \
    && rm /tmp/jdk-21_linux-x64_bin.tar.gz \
    && ln -s /opt/java/jdk-21* /opt/java/jdk-21

# Variables d’environnement
ENV JAVA_HOME=/opt/java/jdk-21
ENV PATH="/usr/local/bin:$JAVA_HOME/bin:$PATH"

# Installer les plugins Jenkins nécessaires
RUN jenkins-plugin-cli --plugins \
    pipeline-model-definition \
    pipeline-stage-view \
    workflow-aggregator \
    jacoco \
    pipeline-utility-steps \
    ws-cleanup \
    amazon-ecs \
    git \
    code-coverage-api \
    pipeline-aws

USER jenkins
