#!/bin/bash
JAVA_HOME=/usr/lib/jvm/jdk-17
JASYPT_JAR=../module/app/agrolavka/build/exploded/agrolavka-1.0-plain.war/WEB-INF/lib/jasypt-1.9.3.jar

java -cp $JASYPT_JAR org.jasypt.intf.cli.JasyptPBEStringEncryptionCLI input=$1 password=${JASYPT_ENCRYPTOR_PASSWORD} \
    verbose=true stringOutputType=base64 algorithm=PBEWITHHMACSHA512ANDAES_256 ivGeneratorClassName=org.jasypt.iv.RandomIvGenerator
