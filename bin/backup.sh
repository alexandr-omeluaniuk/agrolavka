#!/bin/bash
KEY=/home/alex/work/secure/agrolavka/id_rsa_agrolavka
HOST=157.90.160.237

ssh -i ${KEY} agrolavka@${HOST} "mysqldump -u root -p${REMOTE_DB_KEY} agrolavka > agrolavka.sql"
scp -i ${KEY} agrolavka@${HOST}:/home/agrolavka/agrolavka.sql /home/alex/work/backup/agrolavka.sql
echo "dump uploaded"

ssh -i ${KEY} agrolavka@${HOST} "zip -r aimages.zip agrolavka.images"
scp -i ${KEY} agrolavka@${HOST}:/home/agrolavka/aimages.zip /home/alex/work/backup/aimages.zip
echo "images uploaded"

rm -rf /home/alex/work/data/agrolavka.images
cd /home/alex/work/data
cp /home/alex/work/backup/aimages.zip /home/alex/work/data/
unzip aimages.zip
echo "images restored"

mysql -u root -p${LOCAL_DB_KEY} agrolavka < /home/alex/work/backup/agrolavka.sql