#!/bin/bash
javac $(find * | grep .java) #-Xlint:unchecked
jar cfm MyJar.jar manifest.mf *.class view/ model/
java -jar MyJar.jar
