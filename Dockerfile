FROM jenkins/jenkins:lts

USER root

# Install dependencies: wget, Maven, Docker CLI, certificates
RUN apt-get update && apt-get install -y \
    wget \
    maven \
    ca-certificates \
    docker.io \
    jq \
    && rm -rf /var/lib/apt/lists/*

# Install JDK 21 (Oracle)
RUN wget https://download.oracle.com/java/21/latest/jdk-21_linux-x64_bin.tar.gz -O /tmp/jdk-21_linux-x64_bin.tar.gz \
    && mkdir -p /opt/java \
    && tar -xzf /tmp/jdk-21_linux-x64_bin.tar.gz -C /opt/java \
    && rm /tmp/jdk-21_linux-x64_bin.tar.gz \
    && ln -s /opt/java/jdk-21* /opt/java/jdk-21

# Set Java environment variables
ENV JAVA_HOME=/opt/java/jdk-21
ENV PATH="$JAVA_HOME/bin:$PATH"

# Install Jenkins plugins (combine into one layer)
RUN jenkins-plugin-cli --plugins \
    pipeline-model-definition \
    pipeline-stage-view \
    workflow-aggregator \
    jacoco \
    pipeline-utility-steps \
    ws-cleanup \
    aws-steps \
    git

USER jenkins
