#!/usr/bin/env bash

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"
FILE=${1:-.envrc}
cd "${DIR}"/../ || exit

echo "Setting environment from ${FILE}!"

source "${DIR}"/.envrc.example

# shellcheck disable=SC1090
source "${DIR}"/"${FILE}" 2> /dev/null

./gradlew bootRun