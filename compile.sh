#!/bin/bash
javac $(find * | grep .java) #-Xlint:unchecked
jar cfm MyJar.jar manifest.mf *.class gui/ business/
java -jar MyJar.jar
