#!/bin/bash
sudo apt update
export DEBIAN_FRONTEND=noninteractive
MYSQL_ROOT_PASSWORD='root'
echo debconf mysql-server/root_password password $MYSQL_ROOT_PASSWORD | sudo debconf-set-selections
echo debconf mysql-server/root_password_again password $MYSQL_ROOT_PASSWORD | sudo debconf-set-selections
sudo apt-get -qq install -y mysql-server > /dev/null
sudo apt-get install -y maven
