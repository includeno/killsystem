docker-compose stop
docker-compose down

环境/Environment
docker-compose -f docker-compose-env.yml build
docker-compose -f docker-compose-env.yml up -d
docker-compose -f docker-compose-env.yml logs -f
docker-compose -f docker-compose-env.yml logs mysql_miaosha

docker-compose -f docker-compose-env.yml stop
docker-compose -f docker-compose-env.yml down

应用/Application
docker-compose -f docker-compose-app.yml up -d
docker-compose -f docker-compose-app.yml logs

docker-compose -f docker-compose-app.yml stop
docker-compose -f docker-compose-app.yml down

应用本地打包/Application Build
docker-compose -f docker-compose-app-build.yml up -d
docker-compose -f docker-compose-app-build.yml logs

docker-compose -f docker-compose-app-build.yml stop
docker-compose -f docker-compose-app-build.yml down