#!/bin/bash
KEY=/home/alex/secure/agrolavka/id_rsa_agrolavka
MAVEN_HOME=/home/alex/work/soft/netbeans/java/maven/bin/mvn
cd src/main/resources/static/admin
npm run build
cd ../../../../../
${MAVEN_HOME} clean install
ssh -i ${KEY} agrolavka@157.90.160.237 "rm agrolavka.war"
scp -i ${KEY} target/agrolavka.war agrolavka@157.90.160.237:/home/agrolavka
ssh -i ${KEY} agrolavka@157.90.160.237 "chmod 500 agrolavka.war"
ssh -i ${KEY} agrolavka@157.90.160.237 "sudo service agrolavka restart"
