# specify the base imagine we want to use for this container
FROM ubuntu

# author or owner of this container
MAINTAINER Yann Mulonda (yannmjl@gmail.com)

# update the container and install curl
RUN apt-get -y update && apt-get -y install curl

# install java:latest verdion
RUN mkdir /opt/java && cd /opt/java && tar -xvzf /tmp/server-jre-8u144-linux-x64.tar.gz && ln -s jdk1.8.0_144/ latest

# create a directory where the app will be saved
RUN mkdir /app

# create environment variable for java
ENV JAVA_CMD /opt/java/latest/bin/java
ENV JAVA_HOME /opt/java/latest

# create directories and install leiningen on ubuntu
RUN mkdir -p ~/.lein/self-installs && cp /tmp/leiningen-2.7.1-standalone.jar ~/.lein/self-installs
# change directory access modifyers
RUN cp /tmp/lein.sh /usr/local/bin/lein && chmod 755 /usr/local/bin/lein

# So that we can run lein as root (we are in docker)
ENV LEIN_ROOT true

# cope the project file from its directory to the newly app directry ceated
COPY project.clj /app/project.clj

# set env path variable for java
ENV PATH ${PATH}:${JAVA_HOME}/bin
RUN cd /app && /usr/local/bin/lein deps

# Do the above first so that dependencies get installed properly.

COPY ./src /app/src

# change directory to app directory and start te server with lein cmd
CMD echo "starting the web server" && cd /app && /usr/local/bin/lein run -m restful.core

EXPOSE 5050

# If you're reading this and have any feedback on how this image could be
# improved, please open an issue or a pull request so we can discuss it!

