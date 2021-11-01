[![Codacy Badge](https://app.codacy.com/project/badge/Grade/72c79a99b2c34d92b3fc495f5a455735)](https://www.codacy.com/gh/CC21-EDW/edw-component/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=CC21-EDW/edw-component&amp;utm_campaign=Badge_Grade)
[![DepShield Badge](https://depshield.sonatype.org/badges/CC21-EDW/edw-component/depshield.svg)](https://depshield.github.io)
![Build Status](https://github.com/CC21-EDW/edw-component/workflows/CI/badge.svg)

# edw-component
Base component used for service connectors providing base functionality

## Tech-Stack
see https://github.com/CC21-EDW/documentation/blob/main/adr/003-technology-stack.md

## Releasing

Run e.g. on main: `mvn -B release:prepare`

Subsequently the GitHub action worksflow "create release" will pick up the published tag and release and deploy the artifacts in the Github package registry.
