#!/bin/bash
cd src/main/resources/static/admin
npm run build
cd ../../../../../
/home/alex/netbeans-12.0/netbeans/java/maven/bin/mvn clean install
ssh -i /media/veracrypt1/agrolavka/id_rsa_agrolavka agrolavka@157.90.160.237 "rm agrolavka.war"
scp -i /media/veracrypt1/agrolavka/id_rsa_agrolavka target/agrolavka.war agrolavka@157.90.160.237:/home/agrolavka
ssh -i /media/veracrypt1/agrolavka/id_rsa_agrolavka agrolavka@157.90.160.237 "chmod 500 agrolavka.war"
ssh -i /media/veracrypt1/agrolavka/id_rsa_agrolavka agrolavka@157.90.160.237 "sudo service agrolavka restart"
