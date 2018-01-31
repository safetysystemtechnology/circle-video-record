echo -e "Running script..."
./gradlew build
./gradlew -PbintrayUser="${BINTRAY_USER}" -PbintrayKey="${BINTRAY_KEY}" build bintrayUpload