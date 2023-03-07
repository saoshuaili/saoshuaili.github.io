package netcat

import (
	"log"
	"net"
	"os"
	"testing"
)

// @Author: coachhe
// @Date: 2022/8/24 下午1:40

func TestNetCat1(t *testing.T) {
	conn, err := net.Dial("tcp", "localhost:8000")
	if err != nil {
		log.Fatal(err)
	}
	mustCopy(os.Stdout, conn)
	conn.Close()
}
