package main

import (
	"io"
	"log"
	"net"
	"time"
)

//@author: coachhe
//@create: 2022/8/9 16:32

// 在这里，net.Conn满足io.Writer接口，因此可以直接通过io.WriteString()方法往里面写数据
func handleConn1(c net.Conn) {
	defer func(c net.Conn) {
		err := c.Close()
		if err != nil {
			log.Printf("网络连接关闭失败，%v", err.Error())
		}
	}(c)
	for {
		_, err := io.WriteString(c, time.Now().Format("15:05:04\n"))
		if err != nil {
			return // 例如连接断开
		}
		time.Sleep(1 * time.Second)
	}
}
