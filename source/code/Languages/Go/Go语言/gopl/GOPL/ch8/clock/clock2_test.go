package main

import (
	"log"
	"net"
	"testing"
)

//@author: coachhe
//@create: 2022/8/24 13:35

func TestClock2(t *testing.T) {
	listener, err := net.Listen("tcp", "localhost:8000") // 监听本地的8080端口
	if err != nil {
		log.Fatal(err)
	}
	for {
		conn, err := listener.Accept()
		if err != nil {
			log.Println(err) // 例如连接终止
			continue
		}
		go handleConn2(conn) // 一次处理一个连接
	}
}
