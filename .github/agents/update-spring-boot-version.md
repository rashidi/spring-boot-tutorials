# Update Spring Boot Version in Documentation

## Context

When Dependabot opens a pull request to upgrade the Spring Boot Gradle plugin in `build.gradle.kts`, the Spring Boot version referenced in `README.adoc` and `supplemental-ui/partials/footer-content.hbs` must also be updated to stay in sync.

## Task

1. Identify the new Spring Boot version from `build.gradle.kts`. The version is declared on the line that applies the Spring Boot plugin:

   ```
   id("org.springframework.boot") version "<VERSION>" apply false
   ```

2. Update `README.adoc` — there are two places that reference the Spring Boot version:

   - The badge image URL on the line containing `Spring_Boot-` — replace the old version number in the URL with the new one:
     ```
     image:https://img.shields.io/badge/Spring_Boot-<NEW_VERSION>-blue?style=flat-square&logo=springboot[Spring Boot version]
     ```
   - The prose text in the Commitment section that reads `Spring Boot <OLD_VERSION>` — both the plain text reference and the Gradle plugin link:
     ```
     with https://plugins.gradle.org/plugin/org.springframework.boot/<NEW_VERSION>[Spring Boot <NEW_VERSION>].
     ```

3. Update `supplemental-ui/partials/footer-content.hbs` — replace the old version number in the badge image `src` URL on the line containing `Spring_Boot-`:
   ```
   <img alt="Spring Boot version" src="https://img.shields.io/badge/Spring_Boot-<NEW_VERSION>-blue?style=flat-square&logo=springboot" />
   ```

## Rules

- Only change the Spring Boot version strings. Do not modify anything else in these files.
- The new version must exactly match the version declared in `build.gradle.kts`.
- Do not update `build.gradle.kts` — Dependabot has already updated it.
