mvn clean package -DskipTests
/home/hungpv/Documents/apache-tomcat-8.5.51/bin/shutdown.sh
rm /home/hungpv/Documents/apache-tomcat-8.5.51/webapps/test_javamelody/WEB-INF/lib/javamelody-core-1.81.0.jar
cp target/javamelody-core-1.81.0.jar /home/hungpv/Documents/apache-tomcat-8.5.51/webapps/test_javamelody/WEB-INF/lib/
rm /home/hungpv/mylogfile.log
/home/hungpv/Documents/apache-tomcat-8.5.51/bin/startup.sh
sleep 3
tail -f /home/hungpv/mylogfile.log