#!/bin/bash
printf "\n\n\n\n\n\n\n\n"
cd src
javac -source 1.7 -target 1.7 $(find * | grep .java) #-Xlint:unchecked
jar cfm ../MyJar.jar manifest.mf *.class controller/ model/ PNMLTools/ view/
java -jar ../MyJar.jar
