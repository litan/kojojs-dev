echo *** syncing with remote upstream
git remote -v
git fetch upstream
git checkout master
git merge upstream/master
