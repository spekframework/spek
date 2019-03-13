#! /usr/bin/env bash
set -e
BASEDIR=$(dirname "$0")
VIRTUALENV_DIR="$BASEDIR/p3"

if [[ ! -d "$VIRTUALENV_DIR" ]]; then
    pip3 install virtualenv
    virtualenv -p python3 "$VIRTUALENV_DIR"
    source "$VIRTUALENV_DIR/bin/activate"
    pip3 install -r requirements.txt
else
    source "$VIRTUALENV_DIR/bin/activate"
fi
mkdocs serve