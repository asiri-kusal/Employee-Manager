@echo off
set /p id=Enter BitBucket project Name:
set /p featureName=Enter Feature Name:
git init
git add --all
git commit -m "Initial Commit"
git remote add origin https://bitbucket.dialog.lk/scm/crmrw/%id%.git
git branch feature/%featureName%
git push -u origin feature/%featureName%