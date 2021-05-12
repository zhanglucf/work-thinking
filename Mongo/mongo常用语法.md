```shell
#登录mongo shell 脚本
	方式一：
		mongo -host 192.168.13.147 xietong110
		use xietong110
		db.auth("xietong","xietong$110$mongo")
	方式二：（貌似不好用）
		mongodb://xietong:xietong\$110\$mongo@192.168.0.17
		
#查询&排序
db.apply_message.find().sort({"applyDate":1})

#给表中已有列重命名
db.appResource.updateMany({}, {$rename : {"updateTime" : "lastModifyTime"}})

#条件查询方式一
db.appResource.find({"createTime": {}})

#更新时间
db.appResource.updateMany({"createTime": {}},{$set:{"createTime": ISODate("2020-09-03T04:19:13.644Z")}})

#查询所有
db.appResource.find()

#表重命名
db.mycollection.renameCollection('c_temp')

#条件查询
db.mycollection.find({$or: [{salary: {$lt:40}}, {salary: {$gt:200}}]})

#列举所有表
show collections

#给表中已有列重命名
db.appResource.updateMany({}, {$rename : {"classification" : "classificationId"}})

#查询指定字段 select name, skills from users
db.appResource.find({}, {"appType": 1})
db.appResource.find({"appType": "ncp"}, {"appType": 1})

#根据条件查询指定字段 select name, skills from users select name, age, skills from users where name = 'hurry' and age = 18
db.appResource.find({"appType": "ncp", "desc": "测试"})

# select name, age, skills from users where name = 'hurry' or age = 18
db.appResource.find({'$or': [{"appType": "ncp"}, {"desc": "测试"}]}, {})

# select * from users where age >= 20 and age <= 30;
db.users.find({'age': {'$gte': 20, '$lte': 30}});

# select * from users where age >= 20 and age <= 30
db.appResource.find({'version': {'$in': [0, 1]}});

# like "%hurry%"
db.appResource.find({'appType': /ncp/});

# like "hurry%"
db.appResource.find({'appType': /^ncp/});

# 数组查询 （mongoDB自己特有的） 
db.users.find({'skills': 'java'});

# 数组查询 $all skills中必须同时包含java 和 python
db.users.find({'skills': {'$all': ['java', 'python']}})

# 正则表达式 i(忽视大小写)
db.appResource.find({link: /.*baidu*/i})

# $elemMatch
db.appResource.find({"classifications": {"$elemMatch": {"_id": 1750020198238208}}})

# 嵌套对象属性查询
db.appResource.find({"profile.appIdentify": "APP020200409002"})

#  $not 取反
db.appResource.find({link: {"$not": /.*baidu*/i}})

# 分页排序查询，倒序（-1），正序（1）
db.appResource.find({link: {"$not": /.*baidu*/i}}).sort({"createTime": 1}).skip(0).limit(2);

# 整个表删除
db.collection.drop()

# 删除某些条件的数据，删除type类型是test的数据
db.collection.remove({"type" : "test"})

# 删除某些条件的数据，删除type类型是test的数据
db.collection.remove({"type" : "test"})

# 格式化后输出
db.appResource.find().pretty()

# app_backup替换成appResource
#原始appResource作为历史表保留
db.appResource.renameCollection("appResource_history")

#将预先通过接口备份的集合重新名称为appResource
db.app_backup.renameCollection("appResource")

#修改appResource中的classification字段为classificationId
db.appResource.updateMany({}, {$rename : {"classification" : "classificationId"}})

#更新classification表中类别名称为classificationName
db.classification.updateMany({}, {$rename : {"classification" : "classificationName"}})
```

