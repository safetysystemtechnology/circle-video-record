echo -e "Running script..."
./gradlew -PbintrayUser="${BINTRAY_USER}" -PbintrayKey="${BINTRAY_KEY}" build bintrayUpload