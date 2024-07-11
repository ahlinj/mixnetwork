docker-compose build
docker-compose up -d
$containers = @("entry-point-1", "user1-1", "user2-1", "user3-1", "user4-1", "user5-1", "user6-1","user7-1","user8-1","user9-1","user10-1")
foreach ($container in $containers) {
    if ($container -eq "entry-point-1") {
        Start-Process powershell -ArgumentList "docker exec -it mixnet-$container bash -c './ep_run_java.sh; exec bash'"
    } else {
        Start-Process powershell -ArgumentList "docker exec -it mixnet-$container bash -c './user_run_java.sh; exec bash'"
    }
}