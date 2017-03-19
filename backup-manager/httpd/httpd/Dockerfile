FROM phusion/baseimage
MAINTAINER Mike S. <silvanovich.michael@gmail.com>
# installing httpd + mod_jk
RUN apt-get update && apt-get --assume-yes install --no-install-recommends apache2 libapache2-mod-jk
# Preparing Apache2 + mod_jk
RUN rm /etc/apache2/apache2.conf
RUN rm /etc/apache2/mods-available/jk.conf
RUN rm /etc/libapache2-mod-jk/workers.properties
ADD workers.properties /etc/libapache2-mod-jk/workers.properties
ADD jk.conf /etc/apache2/mods-available/jk.conf
ADD apache2.conf /etc/apache2/apache2.conf
# Configuring startup
RUN mkdir -p /etc/my_init.d
ADD apache2.sh /etc/my_init.d/apache2.sh
RUN chmod 775 /etc/my_init.d/apache2.sh
