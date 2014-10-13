#!/bin/bash
javac $(find * | grep .java)
jar cfm MyJar.jar manifest.mf *.class gui/
java -jar MyJar.jar
