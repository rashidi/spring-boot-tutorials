---
name: update-spring-boot-version
description: Synchronizes Spring Boot version badges and headers on the antora branch when Dependabot upgrades Spring Boot on master.
---

# Update Spring Boot Version on Antora

## Task Overview
When Dependabot updates `org.springframework.boot:spring-boot-gradle-plugin` in `build-logic/build.gradle.kts` on `master`, this agent extracts the new version and propagates it to the Antora UI partials on the `antora` branch before opening a PR with auto-merge enabled.

### Key Steps
1. **Detect New Version:** Extract `<NEW_VERSION>` from `org.springframework.boot:spring-boot-gradle-plugin:<NEW_VERSION>` in `build-logic/build.gradle.kts` on the active Dependabot PR branch. Do not modify `README.adoc` on `master` or `antora` (both are version-agnostic).
2. **Antora Branch Sync:** Check out `origin/antora` into a feature branch (`chore/update-spring-boot-<NEW_VERSION>`) and update the Spring Boot version in:
   - `supplemental-ui/partials/article.hbs` (both the home hero eyebrow and the article header eyebrow: `Java 25 · Spring Boot <NEW_VERSION>`)
   - `supplemental-ui/partials/footer-content.hbs` (the shields.io badge URL: `Spring_Boot-<NEW_VERSION>`)
3. **Auto-Merge PR:** Push the branch, create a pull request targeting `antora` if needed, and run:
   ```bash
   gh pr merge chore/update-spring-boot-<NEW_VERSION> --auto --squash
   ```
