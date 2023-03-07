package reverb

import (
	"log"
	"net"
	"os"
	"testing"
)

// @Author: coachhe
// @Date: 2022/8/24 下午2:07

func TestReverb(t *testing.T) {
	conn, err := net.Dial("tcp", "localhost:8000")
	if err != nil {
		log.Printf("conect failed, %v", err.Error())
		return
	}
	defer func(conn net.Conn) {
		err := conn.Close()
		if err != nil {
			log.Fatal(err)
		}
	}(conn)
	go mustCopy(os.Stdout, conn)
	mustCopy(conn, os.Stdin)
}
