#!/bin/bash
printf "\n\n\n\n\n\n\n\n"
javac -source 1.7 -target 1.7 $(find * | grep .java) #-Xlint:unchecked
jar cfm MyJar.jar manifest.mf *.class model/ view/ controller/ PNMLTools/
java -jar MyJar.jar
