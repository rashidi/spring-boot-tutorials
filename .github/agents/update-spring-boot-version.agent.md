---
name: update-spring-boot-version
description: Synchronizes Spring Boot versions across master and antora branches with auto-merge enabled on PRs.
---

# Update Spring Boot Version Across Branches

## Task Overview
When Dependabot updates `org.springframework.boot` in `build.gradle.kts` on `master`, this agent extracts the new version, updates `README.adoc` on `master`, and propagates the changes to the `antora` branch (updating its distinct `README.adoc` and supplemental UI files) before opening a PR with auto-merge enabled.

### Key Steps
1. **Master Branch Updates:** Detect `<NEW_VERSION>` from `build.gradle.kts` and update badge and plugin links in `README.adoc` on the active PR branch.
2. **Antora Branch Sync:** Check out `origin/antora` into a feature branch (`chore/update-spring-boot-<NEW_VERSION>`), updating Spring Boot references in `supplemental-ui/partials/footer-content.hbs` and the branch-specific `README.adoc`.
3. **Auto-Merge PR:** Push the `antora` branch, create a pull request targeting `antora` if needed, and run:
   ```bash
   gh pr merge chore/update-spring-boot-<NEW_VERSION> --auto --squash
   ```
