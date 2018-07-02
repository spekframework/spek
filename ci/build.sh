#!/bin/bash
set -e

# You can run it from any directory.
PROJECT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )/.."

source "$PROJECT_DIR/ci/git_branch.sh"

"$PROJECT_DIR"/gradlew --no-daemon --info --stacktrace clean snapshot build

BINTRAY_USER="$BINTRAY_USER"
BINTRAY_API_KEY="$BINTRAY_API_KEY"

GIT_BRANCH=`getCurrentGitBranch`

function checkIfEnvVarIsNotEmpty() {
    local env_var_name="$1"

    if [ -z "$env_var_name" ]; then
        echo "Error: environment variable $env_var_name is empty."
        exit 1
    fi
}

#GIT_BRANCH_FOR_SNAPSHOTS="2.x"

#if [ "$GIT_BRANCH" == "$GIT_BRANCH_FOR_SNAPSHOTS" ]; then
#    checkIfEnvVarIsNotEmpty "$BINTRAY_USER"
#    checkIfEnvVarIsNotEmpty "$BINTRAY_API_KEY"
#
#    echo "Publishing snapshot..."
#    "$PROJECT_DIR"/gradlew snapshot artifactoryPublish --info --stacktrace
#else
#    echo "Snapshot will not be published for this build, because branch '$GIT_BRANCH' doesn't match '$GIT_BRANCH_FOR_SNAPSHOTS'."
#fi
