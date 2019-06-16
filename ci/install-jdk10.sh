#!/usr/bin/env bash

curl -sL https://github.com/shyiko/jabba/raw/master/install.sh | bash && . ~/.jabba/jabba.sh
jabba install zulu@1.10.0-2
jabba use zulu@1.10.0-2
echo "##vso[task.setvariable variable=JAVA_HOME]$(jabba which --home $(jabba current))"
