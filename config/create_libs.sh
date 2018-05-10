#!/usr/bin/env bash
# PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
# Copyright (C) 2013-2018 Florian J. Breunig. All rights reserved.

set -o nounset
set -o errexit
set -o pipefail

readonly VERSION="1.7.0"

readonly PROGNAME=$(basename "$0")
readonly PROGDIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
readonly TARGET_DIR=${TARGET_DIR:-"$PROGDIR/../lib"}
readonly TMP_DIR=${TMP_DIR:-$(mktemp -d -t paypalimporter)}

# Check if all required dependencies exist
check_prerequisites() {
  # All executables used in this script must be available
  for executable in basename cp git mvn patch; do
    command -v "${executable}" > /dev/null 2>&1 || {
      echo >&2 "In order to use \"${PROGNAME}\", executable \"${executable}\" must be installed.";
      exit 1;
    }
  done
}

clone_repository() {
    local repository=$1
    local release_tag=$2
    local dir=$3
    git clone -b "$release_tag" --single-branch "$repository" --depth=1 "$dir"
}

apply_patch() {
    local patchfile=$1
    local dir=$2
    patch -p 1 -i "$patchfile" -d "$dir"
}

generate_jar() {
    local dir=$1
    mvn package -f "$dir/pom.xml"
}

copy_jar() {
    local source_dir=$1
    local target_file=$2
    cp "$source_dir/target/paypal-core-$VERSION.jar" "$target_file"
}

function cleanup {
    rm -rf "$TMP_DIR"
}

main() {
    check_prerequisites
    clone_repository "git@github.com:paypal/sdk-core-java.git" "v$VERSION" "$TMP_DIR"
    apply_patch "$PROGDIR/fix_proxies.patch" "$TMP_DIR"
    generate_jar "$TMP_DIR"
    copy_jar "$TMP_DIR" "$TARGET_DIR/paypal-core-paypalimporter-$VERSION.jar"
}

trap cleanup EXIT

main
