FROM jenkins/jenkins:lts

USER root

# Télécharger et installer JDK 21
RUN apt-get update \
    &&  apt-get install -y wget \
    && wget https://download.oracle.com/java/21/latest/jdk-21_linux-x64_bin.tar.gz -O /tmp/jdk-21_linux-x64_bin.tar.gz \
    && mkdir -p /opt/java \
    && tar -xzf /tmp/jdk-21_linux-x64_bin.tar.gz -C /opt/java \
    && rm /tmp/jdk-21_linux-x64_bin.tar.gz \
    && ln -s /opt/java/jdk-21* /opt/java/jdk-21

# Définir JAVA_HOME
ENV JAVA_HOME=/opt/java/jdk-21
ENV PATH="$JAVA_HOME/bin:$PATH"

# Installer Docker CLI
RUN apt-get update && \
    apt-get install -y docker.io && \
    rm -rf /var/lib/apt/lists/*


# Installer plugins Jenkins
RUN jenkins-plugin-cli --plugins \
    pipeline-model-definition \
    pipeline-stage-view \
    workflow-aggregator \
    jacoco \
    pipeline-utility-steps \
    ws-cleanup

USER jenkins
