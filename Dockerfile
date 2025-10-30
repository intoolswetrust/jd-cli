FROM gcr.io/distroless/java17-debian12
MAINTAINER Josef (kwart) Cacek <josef.cacek@gmail.com>

COPY jd-cli/target/jd-cli.jar /jd-cli.jar
ENTRYPOINT ["/usr/bin/java", "-jar", "/jd-cli.jar"]
CMD ["--help"]
