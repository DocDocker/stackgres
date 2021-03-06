#!/bin/sh

set -e

DISTRIBUTEDLOGS_CONTROLLER_IMAGE_NAME="${DISTRIBUTEDLOGS_CONTROLLER_IMAGE_NAME:-"stackgres/distributedlogs-controller:development"}"
CONTAINER_BASE=$(buildah from "registry.access.redhat.com/ubi8-minimal:8.2")
TARGET_DISTRIBUTEDLOGS_CONTROLLER_IMAGE_NAME="${TARGET_DISTRIBUTEDLOGS_CONTROLLER_IMAGE_NAME:-docker-daemon:$DISTRIBUTEDLOGS_CONTROLLER_IMAGE_NAME}"

APP_OPTS="-Dquarkus.http.host=0.0.0.0 -Djava.util.logging.manager=org.jboss.logmanager.LogManager"

# Include binaries
buildah config --workingdir='/app/' "$CONTAINER_BASE"
buildah copy --chown nobody:nobody "$CONTAINER_BASE" 'distributedlogs-controller/target/stackgres-distributedlogs-controller-runner' '/app/stackgres-distributedlogs-controller'
buildah run "$CONTAINER_BASE" -- chmod 775 '/app'

## Run our server and expose the port
buildah config --cmd "./stackgres-distributedlogs-controller $APP_OPTS" "$CONTAINER_BASE"
buildah config --port 8080 "$CONTAINER_BASE"
buildah config --port 8443 "$CONTAINER_BASE"
buildah config --user nobody:nobody "$CONTAINER_BASE"
buildah config --env LANG="C.UTF-8" --env LC_ALL="C.UTF-8" "$CONTAINER_BASE"

## Commit this container to an image name
buildah commit "$CONTAINER_BASE" "$DISTRIBUTEDLOGS_CONTROLLER_IMAGE_NAME"
buildah push -f "${BUILDAH_PUSH_FORMAT:-docker}" "$DISTRIBUTEDLOGS_CONTROLLER_IMAGE_NAME" "$TARGET_DISTRIBUTEDLOGS_CONTROLLER_IMAGE_NAME"
buildah delete "$CONTAINER_BASE"
