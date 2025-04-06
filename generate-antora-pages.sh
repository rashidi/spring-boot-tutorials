#!/bin/bash

# List of submodules from the navigation file
SUBMODULES=(
  "batch-rest-repository"
  "batch-skip-step"
  "cloud-jdbc-env-repo"
  "data-domain-events"
  "data-envers-audit"
  "data-jdbc-audit"
  "data-jpa-audit"
  "data-jpa-event"
  "data-jpa-filtered-query"
  "data-mongodb-audit"
  "data-mongodb-full-text-search"
  "data-mongodb-transactional"
  "data-repository-definition"
  "data-rest-validation"
  "graphql"
  "modulith"
  "jooq"
  "data-mongodb-tc-data-load"
  "test-execution-listeners"
  "test-rest-assured"
  "test-slice-tests-rest"
  "web-rest-client"
)

# Create Antora pages for each submodule
for submodule in "${SUBMODULES[@]}"; do
  # Extract the title from the README.adoc file
  title=$(head -n 1 "$submodule/README.adoc" | sed 's/^= //')

  # Create the Antora page
  cat > "docs/modules/ROOT/pages/$submodule.adoc" << EOF
= $title
:page-aliases: $submodule.adoc

include::../../../../$submodule/README.adoc[lines=2..-1]
EOF

  echo "Created Antora page for $submodule"
done

echo "All Antora pages have been created."
