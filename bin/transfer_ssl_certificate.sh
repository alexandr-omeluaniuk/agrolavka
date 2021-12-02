#!/bin/bash
KEY=/home/alex/secure/agrolavka/id_rsa_agrolavka
scp -i ${KEY} /home/alex/secure/agrolavka/agrolavka.by.zip agrolavka@157.90.160.237:/home/agrolavka/ssl
