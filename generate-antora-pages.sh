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

# Badges content is no longer needed as we're removing all badges

# Create Antora pages for each submodule
for submodule in "${SUBMODULES[@]}"; do
  echo "Processing $submodule..."

  # Extract the title from the README.adoc file
  title=$(head -n 1 "$submodule/README.adoc" | sed 's/^= //')

  # Create a temporary file to store the processed content
  temp_file=$(mktemp)

  # Process the README.adoc file line by line
  while IFS= read -r line; do
    # Skip the line if it contains the include directive for badges.adoc
    if [[ "$line" == *"include::../docs/badges.adoc[]"* ]]; then
      continue
    else
      echo "$line" >> "$temp_file"
    fi
  done < <(tail -n +2 "$submodule/README.adoc")

  # Create the Antora page with the actual content
  cat > "docs/modules/ROOT/pages/$submodule.adoc" << EOF
= $title

$(cat "$temp_file")
EOF

  # Remove the temporary file
  rm "$temp_file"

  echo "Created Antora page for $submodule"
done

echo "All Antora pages have been created."
