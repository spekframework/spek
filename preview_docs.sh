#! /usr/bin/env bash
BASEDIR=$(dirname "$0")
if [ -d "$BASEDIR/p3" ]; then
    pip install virtualenv
    virtualenv -p python3 p3
    source ./p3/bin/activate
    pip install -r requirements.txt
fi
mkdocs serve
