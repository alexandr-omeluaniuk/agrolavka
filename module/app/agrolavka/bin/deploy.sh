#!/bin/bash
JAVA_HOME=/usr/lib/jvm/jdk-17
KEY=/home/alex/secure/agrolavka/id_rsa_agrolavka
HOST=157.90.160.237

export JAVA_HOME=${JAVA_HOME}

# ssh -i ${KEY} agrolavka@${HOST} "rm -f agrolavka.war"
scp -i ${KEY} build/libs/agrolavka-1.0.war agrolavka@${HOST}:/home/agrolavka
ssh -i ${KEY} agrolavka@${HOST} "mv agrolavka-1.0.war agrolavka.war"
ssh -i ${KEY} agrolavka@${HOST} "chmod 500 agrolavka.war"
ssh -i ${KEY} agrolavka@${HOST} "sudo service agrolavka restart"

echo "Agrolavka has deployed..."
