package main

import (
	"io"
	"log"
	"net"
	"time"
)

//@author: coachhe
//@create: 2022/8/9 20:00

// 在这里，net.Conn满足io.Writer接口，因此可以直接通过io.WriteString()方法往里面写数据
func handleConn2(c net.Conn) {
	defer func(c net.Conn) {
		err := c.Close()
		if err != nil {
			log.Print(err)
		}
	}(c)
	for {
		_, err := io.WriteString(c, time.Now().Format("15:05:04\n"))
		if err != nil {
			return // 例如连接断开
		}

		// 加上之后可以读取来自连接的数据
		//var b = make([]byte, 50)
		//_, err = c.Read(b)
		//if err != nil {
		//	return
		//}
		//fmt.Println(string(b))
		time.Sleep(1 * time.Second)
	}
}
