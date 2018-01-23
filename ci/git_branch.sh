#!/bin/bash
set -e

# Returns current git branch or "null" if branch cannot be resolved.
function getCurrentGitBranch() {
    # Ask git first.
    local git_branch=`git rev-parse --abbrev-ref HEAD`

    # If branch is "HEAD" it means that some commit was checked out directly and we're in detached mode ie on CI.
    if [ "$git_branch" != "HEAD" ]; then
        echo "$git_branch"
    else
        if [ "$TRAVIS_PULL_REQUEST" == "false" ]; then
            echo "$TRAVIS_PULL_REQUEST_BRANCH"
        else
            git_branch="$TRAVIS_BRANCH"

            if [ ! -z "$git_branch" ]; then
                echo "$git_branch"
            else
                echo "null"
            fi
        fi
    fi
}