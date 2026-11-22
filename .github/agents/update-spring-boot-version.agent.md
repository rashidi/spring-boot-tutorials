---
name: update-spring-boot-version
description: Synchronizes the Spring Boot version in README.adoc and Antora UI templates across master and antora branches when Dependabot upgrades Spring Boot.
---

# Update Spring Boot Version in Documentation

## Context
When Dependabot upgrades the Spring Boot Gradle plugin in `build.gradle.kts`, documentation and UI files across `master` and `antora` branches must stay synchronized.

## Task
1. **Identify Version Changes:** Read the new `org.springframework.boot` plugin version from `build.gradle.kts` (falling back to `master` if necessary) to extract `<OLD_VERSION>` and `<NEW_VERSION>`. Exit if unchanged.
2. **Update Branch-Specific Files:**
   - **`README.adoc` (if present):** Update the badge image URL and Gradle plugin link to `<NEW_VERSION>`.
   - **`supplemental-ui/partials/footer-content.hbs` (if present, e.g., on `antora`):** Update the badge image `src` URL to `<NEW_VERSION>`.
3. **Verify:** Run `git diff` to confirm only version strings changed and report modified files.

## Rules
- Do not modify `build.gradle.kts` or create missing branch-specific files.
- Preserve all existing formatting and whitespace.
