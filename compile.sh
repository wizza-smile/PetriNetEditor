#!/bin/bash
javac $(find * | grep .java) #-Xlint:unchecked
jar cfm MyJar.jar manifest.mf *.class model/ view/ controller/
java -jar MyJar.jar
