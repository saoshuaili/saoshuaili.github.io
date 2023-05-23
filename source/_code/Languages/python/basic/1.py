#!/usr/bin/python3

import pymysql


# 打开数据库连接
db = pymysql.connect(host='10.10.1.37',
                     user='reader',
                     password='rjg+u5vylbL-_v{Kvsnv6wi%',
                     )

# 使用 cursor() 方法创建一个游标对象 cursor
cursor = db.cursor()

# 使用 execute()  方法执行 SQL 查询
cursor.execute("SHOW databases")
# cursor.execute("select * from cloudLearn.hello")

# 使用 fetchone() 方法获取单条数据.
data = cursor.fetchall()

print(data)
# (('information_schema',), ('cloudLearn',), ('cronjob',), ('gmall',), ('hive',), ('maxwell',), ('metastore',), ('mysql',), ('performance_schema',), ('rc',), ('sys',), ('work_order',))

for dbname in data:
    #('work_order',)
    dbname_re = str(dbname).replace("(", "").replace("'", "").replace(")","").replace(",","")
    #print("current database is : %s" % dbname_re)
    cursor.execute("use %s" % dbname_re)
    cursor.execute("show tables")
    data2 = cursor.fetchall()
    for tmp in data2:
        #('casbin_rule',)
        print("|%s|%s|" % (dbname_re, str(tmp).replace("(", "").replace("'", "").replace(")","").replace(",","")))

# 关闭数据库连接
db.close()
