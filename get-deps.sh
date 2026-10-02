#!/bin/bash
sed -i 's/languageVersion = JavaLanguageVersion.of(25)//g' messaging-kafka/build.gradle.kts
./gradlew :messaging-kafka:test --info --no-configuration-cache > test.log
git checkout messaging-kafka/build.gradle.kts
