#!/bin/bash
printf "\n\n\n\n\n\n\n\n"
javac $(find * | grep .java) #-Xlint:unchecked
jar cfm MyJar.jar manifest.mf *.class model/ view/ controller/ parser/
java -jar MyJar.jar
