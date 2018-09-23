#! /usr/bin/env bash
pip install --user virtualenv
virtualenv -p python3 p3
source ./p3/bin/activate
pip install -r requirements.txt
mkdocs build
