# KojoJS-dev
This is the development repository for the core of KojoJS.

The main goal for the repo - is to enables faster (than the full KojoJS webapp) turnaround for coding/testing/debugging/troubleshooting.

Prerequisites: 
* Install `sbt`, https://www.scala-sbt.org/1.x/docs/Installing-sbt-on-Linux.html
* Install `nodejs` and `npm`:
```
sudo apt install nodejs
sudo apt install npm
npm install source-map-support
```

# How to run
```
$ sbt
> fastOptJS
```
In another terminal, navigate to the root dir and:
```
./run.sh
```
The `run.sh` script will launch `google-chrome` with `run.html`
