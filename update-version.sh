#!/bin/bash

NEXT_DEVELOPMENT_VERSION=$1

if [ -z "$NEXT_DEVELOPMENT_VERSION" ]; then
  echo 'Usage: ./update-version.sh NEXT_DEVELOPMENT_VERSION'
  exit 1
fi

echo "Updating POMs to $NEXT_DEVELOPMENT_VERSION"
mvn --batch-mode -DnewVersion=$NEXT_DEVELOPMENT_VERSION -DgenerateBackupPoms=false versions:set

echo "Done updating version. No changes made to repository, please do git add/commit/push manually."