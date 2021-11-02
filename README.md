![Build Status](https://github.com/CC21-EDW/edw-component/workflows/CI/badge.svg)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/72c79a99b2c34d92b3fc495f5a455735)](https://www.codacy.com/gh/CC21-EDW/edw-component/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=CC21-EDW/edw-component&amp;utm_campaign=Badge_Grade)
[![DepShield Badge](https://depshield.sonatype.org/badges/CC21-EDW/edw-component/depshield.svg)](https://depshield.github.io)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=CC21-EDW_edw-component&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=CC21-EDW_edw-component)

# edw-component
Base component used for service connectors providing base functionality

## Tech-Stack
see https://github.com/CC21-EDW/documentation/blob/main/adr/003-technology-stack.md

### Kafka
- [Kafka Java Client](https://docs.confluent.io/clients-kafka-java/current/overview.html)
- [API JavaDoc](https://docs.confluent.io/platform/current/clients/javadocs/javadoc/index.html)

TODO: Add example how to use (Producer / Consumer)

## Releasing

Run e.g. on main: `mvn -B release:prepare`

Subsequently the GitHub action worksflow "create release" will pick up the published tag and release and deploy the artifacts in the Github package registry.
