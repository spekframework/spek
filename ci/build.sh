#!/bin/bash
set -e

# You can run it from any directory.
PROJECT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )/.."

"$PROJECT_DIR"/gradlew --no-daemon --info --stacktrace clean build

BINTRAY_USER="$BINTRAY_USER"
BINTRAY_API_KEY="$BINTRAY_API_KEY"

GIT_BRANCH=`git rev-parse --abbrev-ref HEAD`

function checkIfEnvVarIsNotEmpty() {
    local env_var_name="$1"

    if [ ! z "env_var_name" ]; then
        echo "Error: environment variable $env_var_name is empty."
        exit 1
    fi
}

# TODO don't merge, change branch name to '2.x' before merge.
if [ "$GIT_BRANCH" == "az/snapshot-pub-creds" ]; then
    checkIfEnvVarIsNotEmpty "BINTRAY_USER"
    checkIfEnvVarIsNotEmpty "BINTRAY_API_KEY"

    echo "Publishing snapshot..."
    "$PROJECT_DIR"/gradlew artifactoryPublish --info --stacktrace
else
    echo "Snapshot will not be published for this build."
fi
