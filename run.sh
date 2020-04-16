#!/bin/bash
#-----------------------------------------------------
# Script for running the application on MacOS / Linux
#-----------------------------------------------------

echo ''
echo 'Starting application... (you may stop it with Ctrl+C)'
echo ''
echo 'Note: a Java JDK (at least version 8) is required, as this will compile and run the app based on current sources.'
echo ''

./gradlew --console plain run