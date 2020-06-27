set -x o
rm -rf ../kojojs-core/page/src/main/scala/kojo/
cp -va src/main/scala/kojo/ ../kojojs-core/page/src/main/scala/
cp -va src/main/scala/com/ ../kojojs-core/page/src/main/scala/
meld src/main/scala/ ../kojojs-core/page/src/main/scala/
