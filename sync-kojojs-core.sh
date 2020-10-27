set -x o
rm -rf ~/work/kojojs-core/page/src/main/scala/kojo/
cp -va src/main/scala/kojo/ ~/work/kojojs-core/page/src/main/scala/
cp -va src/main/scala/com/ ~/work/kojojs-core/page/src/main/scala/
cp -va src/main/scala/pixiscalajs/ ~/work/kojojs-core/page/src/main/scala/
cp -va src/main/scala/howlerscalajs/ ~/work/kojojs-core/page/src/main/scala/
meld src/main/scala/ ~/work/kojojs-core/page/src/main/scala/
