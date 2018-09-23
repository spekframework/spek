#! /usr/bin/env bash
BASEDIR=$(dirname "$0")
VIRTUALENV_DIR="$BASEDIR/p3"

if [[ ! -d "$VIRTUALENV_DIR" ]]; then
    pip install virtualenv
    virtualenv -p python3 "$VIRTUALENV_DIR"
    source "$VIRTUALENV_DIR/bin/activate"
    pip install -r requirements.txt
fi
mkdocs build