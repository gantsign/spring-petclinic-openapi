#!/bin/bash
set -e

./gradlew clean build --no-daemon

(cd client/generated && npm install && npm run build)

(cd client && npm install && npm run build && CI=true npm run test:coverage)

if [[ "${CI:-}" == 'true' ]] && [[ $(git status --porcelain=v1 --untracked-files=no |
        grep --invert-match 'package-lock.json') ]]; then
    echo 'Source files not properly formatted, reformat and push again.' 1>&2
    git status --porcelain=v1 --untracked-files=no |
        grep --invert-match 'package-lock.json'
    exit 1
fi
