# newstore-config-collector
This is config collector app

HOW TO Build and RUN
./gradlew build installDist

TO RUN 
cd  ./build/install/nscc

./bin/nscc -tenant williams-sandbox -env p -u btashan@newstore.com -p ***********     -in ../../../configs.csv -of out -single out.json
