FROM open-liberty

USER 1001
ARG VERSION=1.0
ARG REVISION=SNAPSHOT

LABEL \
	org.opencontainers.image.authors="Hello Cloud Demo" \
	org.opencontainers.image.vendor="IBM" \
	org.opencontainers.image.url="local" \
	org.opencontainers.image.version="$VERSION" \
	org.opencontainers.image.revision="$REVISION" \
	vendor="Open Liberty" \
	name="database" \
	version="$VERSION-$REVISION" \
	summary="The database microservice for Hello Cloud Demo" \
	description="This image contains the Hello Cloud Demo database microservice running with the Open Liberty runtime."

COPY --chown=1001:0 src/main/liberty/config/ /config/
COPY --chown=1001:0 target/*.war /config/apps/

RUN configure.sh
