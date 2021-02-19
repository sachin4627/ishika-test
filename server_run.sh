mysql -u root -proot -e \ "ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY 'root'";
mysql -u root -proot -e \ "flush privileges";
mysql -u root -proot -e \ "create database if not exists sakila";
export JAVA_HOME=$(readlink -f /usr/bin/javac | sed "s:/bin/javac::")
mvn clean install
cd target
java -jar ishuagarwal2210-me_buildout_xmeme-0.0.1-SNAPSHOT.jar