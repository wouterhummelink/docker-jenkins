FROM fedora:27
MAINTAINER Wouter Hummelink <wouter.hummelink@ordina.nl>

COPY jenkins.repo /etc/yum.repos.d/
RUN yum -y install jenkins java-1.8.0-openjdk git make && yum clean all

ARG http_port=8080
ARG agent_port=50000
ARG user=jenkins

USER ${user}
ENV JENKINS_HOME /var/lib/jenkins
ENV JENKINS_SLAVE_AGENT_PORT ${agent_port}

VOLUME /var/lib/jenkins

COPY init.groovy.d/ /init.groovy.d/
COPY jenkins.sh /jenkins.sh
ENTRYPOINT ["/jenkins.sh"]

