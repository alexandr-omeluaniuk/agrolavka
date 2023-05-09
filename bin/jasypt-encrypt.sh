#!/bin/bash
JAVA_HOME=/usr/lib/jvm/jdk-17
KEY=/home/alex/secure/agrolavka/test/jasypt.pem

java -cp jasypt-1.9.3.jar org.jasypt.intf.cli.JasyptPBEStringEncryptionCLI input=$1 password=${JASYPT_ENCRYPTOR_PASSWORD} \
    verbose=true stringOutputType=base64 algorithm=PBEWITHHMACSHA512ANDAES_256 ivGeneratorClassName=org.jasypt.iv.RandomIvGenerator
