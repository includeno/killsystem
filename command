#docker-compose up 启动
#docker-compose logs 查看日志
#docker-compose stop 停止
#docker-compose down 删除

环境/Environment
docker-compose -f docker-compose-env.yml build
docker-compose -f docker-compose-env.yml up -d
docker-compose -f docker-compose-env.yml logs -f
docker-compose -f docker-compose-env.yml logs mysql_miaosha
docker-compose -f docker-compose-env.yml logs kafka

docker-compose -f docker-compose-env.yml stop
docker-compose -f docker-compose-env.yml down

应用 不使用env文件直接写配置/Application
docker-compose -f docker-compose-app.yml  up -d
docker-compose -f docker-compose-app.yml logs

docker-compose -f docker-compose-app.yml stop
docker-compose -f docker-compose-app.yml down

应用 使用env文件/Application with env 
docker-compose --env-file app.env -f docker-compose-app-withenv.yml  up -d
docker-compose -f docker-compose-app-withenv.yml logs

docker-compose -f docker-compose-app-withenv.yml stop
docker-compose -f docker-compose-app-withenv.yml down

应用本地打包/Application Build
docker-compose --env-file app.env -f docker-compose-app-build.yml up -d
docker-compose -f docker-compose-app-build.yml logs

docker-compose -f docker-compose-app-build.yml stop
docker-compose -f docker-compose-app-build.yml down