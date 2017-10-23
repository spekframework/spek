#!/bin/bash
set -e

# You can run it from any directory.
PROJECT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )/.."

"$PROJECT_DIR"/gradlew --no-daemon --info clean build
