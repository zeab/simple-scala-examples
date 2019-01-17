
#Log into docker
#Run sbt publishDocker

echo $DOCKER_PASSWORD | docker login -u $DOCKER_USER --password-stdin
sbt dp-sws
