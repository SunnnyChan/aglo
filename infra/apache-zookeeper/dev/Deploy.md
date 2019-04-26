# Deploy

## [Download](http://mirror.bit.edu.cn/apache/zookeeper/stable/)
```sh
wget http://mirror.bit.edu.cn/apache/zookeeper/stable/zookeeper-X.X.X.tar.gz
```
## Config
```sh
cd zookeeper && mkdir data
cd mv conf/zoo_sample.cfg conf/zoo.cfg
```
* 修改dataDir属性
```md
# 数据的存放目录
dataDir=/home/hadoop/zookeeper/data
```
* 配置环境变量
```md
# Zookeeper Environment Variable
export ZOOKEEPER_HOME=/opt/zookeeper
export PATH=$PATH:$ZOOKEEPER_HOME/bin
```
* 启动停止
```sh
$ zkServer.sh start
$ zkServer.sh stop
```
