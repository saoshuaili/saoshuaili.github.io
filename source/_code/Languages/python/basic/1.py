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

# 使用 fetchone() 方法获取单条数据.
data = cursor.fetchall()

print(data)

for dbname in data:
    dbname_re = str(dbname).replace("(", "").replace("'", "").replace(")","").replace(",","")
    cursor.execute("use %s" % dbname_re)
    cursor.execute("show tables")
    data2 = cursor.fetchall()
    for tmp in data2:
        print("|%s|%s|" % (dbname_re, str(tmp).replace("(", "").replace("'", "").replace(")","").replace(",","")))

# 关闭数据库连接
db.close()
