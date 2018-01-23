#!/bin/bash
set -e

function returnValueOrNull() {
    if [ ! -z "$1" ]; then
        echo "$1"
    else
        echo "$1"
    fi
}

# Returns current git branch or "null" if branch cannot be resolved.
# See https://docs.travis-ci.com/user/environment-variables/#Default-Environment-Variables
function getCurrentGitBranch() {
    # Ask git first.
    local git_branch=`git rev-parse --abbrev-ref HEAD`

    # If branch is "HEAD" it means that some commit was checked out directly and we're in detached mode ie on CI.
    if [ "$git_branch" != "HEAD" ]; then
        echo "$git_branch"
    else
        if [ "$TRAVIS_PULL_REQUEST" == "false" ]; then
            echo `returnValueOrNull "$TRAVIS_BRANCH"`
        else
            echo `returnValueOrNull "$TRAVIS_PULL_REQUEST_BRANCH"`
        fi
    fi
}