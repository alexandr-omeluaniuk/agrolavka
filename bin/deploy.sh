#!/bin/bash
/home/alex/netbeans-12.0/netbeans/java/maven/bin/mvn clean install
scp -i /media/veracrypt1/agrolavka/id_rsa_agrolavka target/agrolavka.war agrolavka@157.90.160.237:/home/agrolavka
