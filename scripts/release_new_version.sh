#!/usr/bin/env bash

set -o nounset
set -o errexit
set -o pipefail

CHANGELOG_FILE=${CHANGELOG_FILE:-"CHANGELOG.md"}
realpath --canonicalize-existing "${CHANGELOG_FILE}"
readonly CHANGELOG_FILE

COMMIT_TEMPLATE=${COMMIT_TEMPLATE:-"config/release_new_version.gitmessage"}
realpath --canonicalize-existing "${COMMIT_TEMPLATE}"
readonly COMMIT_TEMPLATE

DEPLOYMENT_INSTRUCTIONS_FILE=${DEPLOYMENT_INSTRUCTIONS_FILE:-"config/deployment_instructions.md"}
realpath --canonicalize-existing "${DEPLOYMENT_INSTRUCTIONS_FILE}"
readonly DEPLOYMENT_INSTRUCTIONS_FILE

DICT_TEMPLATE=${DICT_TEMPLATE:-"config/meta_info.default"}
realpath --canonicalize-existing "${DICT_TEMPLATE}"
readonly DICT_TEMPLATE

DICT_FILE=${DICT_FILE:-"core/src/main/resources/com/moneydance/modules/features/paypalimporter/meta_info.dict"}
realpath --canonicalize-existing "${DICT_FILE}"
readonly DICT_FILE

GIT_MERGE_AUTOEDIT=${GIT_MERGE_AUTOEDIT:-no}
readonly GIT_MERGE_AUTOEDIT

PROGNAME=$(basename "$0")
readonly PROGNAME

SCRIPT_DIR=$(cd "$(dirname "${BASH_SOURCE[0]}")" &>/dev/null && pwd -P)
readonly SCRIPT_DIR

function msg() {
  echo >&2 -e "${1-}"
}

function wait_for_confirmation() {
  local msg="Press Enter to continue"
  msg "${CYAN}${msg}${NOFORMAT}"
  read -r REPLY
  msg
}

function setup_colors() {
  if [[ -t 2 ]] && [[ -z "${NO_COLOR-}" ]] && [[ "${TERM-}" != "dumb" ]]; then
    readonly NOFORMAT='\033[0m' PURPLE='\033[0;35m' CYAN='\033[0;36m'
  else
    readonly NOFORMAT='' PURPLE='' CYAN=''
  fi
}

check_prerequisites() {
  # All executables used in this script must be available
  for executable in bat date envsubst git grep popd printf pushd sed; do
    command -v "${executable}" > /dev/null 2>&1 || {
      msg "In order to use \"${PROGNAME}\", executable ${CYAN}\"${executable}\"${NOFORMAT} must be installed.";
      exit 1;
    }
  done
}

check_clean_stage() {
  git switch develop

  if [[ -n "$(git status --porcelain)" ]]; then
    git status
    msg "Git working directory \"$(PWD)\" is not clean. Aborting."
    exit 1
  fi

  git pull origin develop
  git push --dry-run origin develop main
}

check_working_build_process() {
  ./gradlew clean check test assemble
}

bump_build_number() {
  OLD_VERSION=$(grep module_build "${DICT_FILE}" | grep --extended-regexp --no-filename --only-matching "[0-9]+")
  readonly OLD_VERSION

  NEW_VERSION=$((OLD_VERSION+1))
  readonly NEW_VERSION
  export NEW_VERSION

  msg
  msg "Creating new release with build number ${PURPLE}${NEW_VERSION}${NOFORMAT}."
  wait_for_confirmation
}

patch_changelog_and_dictionary() {
  local timestamp
  timestamp="$(date +%F)"

  sed --expression="s/## \[Unreleased\]/## [v${NEW_VERSION}] - ${timestamp}/" --in-place "${CHANGELOG_FILE}"
  printf "[v%s]: https://github.com/my-flow/paypalimporter/compare/v%s...v%s\n" \
    "${NEW_VERSION}" "${OLD_VERSION}" "${NEW_VERSION}" \
    >> "${CHANGELOG_FILE}"

  AUTHOR="$(git config --get user.name)"
  readonly AUTHOR

  export VENDOR="${AUTHOR}"
  export MODULE_BUILD="${NEW_VERSION}"
  envsubst < "${DICT_TEMPLATE}" > "${DICT_FILE}"
}

commit_changes() {
  git commit --all --file="${COMMIT_TEMPLATE}"
  git show
}

integrate_into_main_branch() {
  git switch main
  git pull origin main

  git merge --no-commit --no-ff develop
  check_working_build_process
  git merge --abort

  export GIT_MERGE_AUTOEDIT
  git merge develop
  git tag "v${NEW_VERSION}" --message="tag v${NEW_VERSION}"

  git log --abbrev-commit --graph "v${OLD_VERSION}..v${NEW_VERSION}"
}

push_changes() {
  git push --tags origin develop main
}

display_deploy_instructions() {
  export CHANGELOG_FILE
  export AUTHOR

  msg
  envsubst < "${DEPLOYMENT_INSTRUCTIONS_FILE}" | \
    bat --decorations=never --language=markdown
}

function cleanup {
  popd
}

main() {
  pushd "${SCRIPT_DIR}/.." # popd as part of the final cleanup

  # initialize
  setup_colors

  # validate
  check_prerequisites
  check_clean_stage
  check_working_build_process
  bump_build_number

  # write changes
  patch_changelog_and_dictionary
  commit_changes

  # integrate changes
  integrate_into_main_branch

  # publish changes
  push_changes
  display_deploy_instructions
}

trap cleanup EXIT

main
