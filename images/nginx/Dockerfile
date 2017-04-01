FROM phusion/baseimage
MAINTAINER Mike S. <silvanovich.michael@gmail.com>
# installing dependencies and required tools
RUN apt-get update && apt-get --assume-yes install wget build-essential zlib1g-dev libpcre3 libpcre3-dev libssl-dev software-properties-common git mercurial
RUN mkdir -p /usr/src 
# Checking out sticky module
RUN cd /usr/src && git clone https://bitbucket.org/nginx-goodies/nginx-sticky-module-ng.git sticky && cd sticky && git checkout 1.2.5
# Checking out nginx
RUN cd /usr/src && hg clone http://hg.nginx.org/nginx nginx && cd nginx && hg checkout release-1.8.0
ADD etc /usr/src/nginx_etc
RUN rm /usr/src/nginx/conf/nginx.conf && cp /usr/src/nginx_etc/nginx/nginx.conf /usr/src/nginx/conf/nginx.conf
RUN cd /usr/src/nginx && ./auto/configure --prefix=/usr/share/nginx --conf-path=/etc/nginx/nginx.conf --error-log-path=/var/log/nginx/error.log --lock-path=/var/lock/nginx.lock --pid-path=/run/nginx.pid --add-module=$(readlink -f ../sticky) --with-http_stub_status_module && make && make install
RUN ln -s /usr/share/nginx/sbin/nginx /usr/sbin/nginx
ADD etc /usr/src/nginx/etc
RUN cp /usr/src/nginx/etc/init.d/nginx /etc/init.d/nginx && chmod 775 /etc/init.d/nginx
# Configuring startup
RUN mkdir -p /etc/my_init.d
ADD nginx.sh /etc/my_init.d/nginx.sh
RUN chmod 775 /etc/my_init.d/nginx.sh



