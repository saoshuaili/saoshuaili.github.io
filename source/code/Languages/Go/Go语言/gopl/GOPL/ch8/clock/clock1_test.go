package main

//@author: coachhe
//@create: 2022/8/24 13:36

import (
	"log"
	"net"
	"testing"
)

func TestClock1(t *testing.T) {

	listener, err := net.Listen("tcp", "localhost:8000") // 监听本地的8080端口
	if err != nil {
		log.Fatal(err)
	}
	for {
		log.Print("hi")
		conn, err := listener.Accept()
		if err != nil {
			log.Print(err) // 例如连接终止
			continue
		}
		handleConn1(conn) // 一次处理一个连接
	}
}
