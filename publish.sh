if [ "$TRAVIS_TAG" == "" ]; then
  echo -e 'Build Branch'
  ./gradlew build
elif [ "$TRAVIS_TAG" != "" ]; then
  echo -e 'Build Branch for tag: Tag ['$TRAVIS_TAG']'
  ./gradlew -PbintrayUser="${BINTRAY_USER}" -PbintrayKey="${BINTRAY_KEY}" build bintrayUpload
else
  echo -e 'WARN: Should not be here ./gradlew clean'
fi