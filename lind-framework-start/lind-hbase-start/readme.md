# docker运行
docker rm -f hbase && docker run -d -h kafka146 -p 2181:2181 -p 18080:8080 -p 18085:8085 -p 19090:9090 -p 9095:9095 -p 16000:16000 -p 16010:16010  -p 16020:16020 -p 16201:16201  --name hbase harisekhon/hbase:1.3
# 建立表插数据
```
cd /hbase/bin/
hbase shell
创建student表，行键是id，一个列族info
> create 'student','id','info'
输入list就能列出新增的student表,输入scan 'student’可以列出student表的内容
> list
在student表新增一条记录，id为1001，名称为Tom
> put 'student','1001','info:name','Tom'
再给这条记录增加年龄属性
> put 'student','1001','info:age','16'
执行查询命令试试，根据行键1001查询记录
> get 'student','1001'
依次输入disable 'student'和drop 'student'两个命令，可以将student表删除
> disable 'student'
> drop 'student'
```
