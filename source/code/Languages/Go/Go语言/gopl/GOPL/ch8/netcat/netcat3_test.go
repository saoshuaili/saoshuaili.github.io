package netcat

import (
	"io"
	"log"
	"net"
	"os"
	"testing"
)

// @Author: coachhe
// @Date: 2022/8/24 下午1:42

// 解释：
// 通过主goroutine将输入复制到conn中(意义不大)
// goroutine将conn内的数据写出到标准输出中
// 如果外部写入数据中断(用户关闭输入流)了，那么goroutine运行结束，打印done，然后会传入一个空结构体到done这个通道中
// 外部感知，整个程序结束
// 注意：这里有一个很奇怪的地方，就是用户关闭输入流这里不知道怎么关闭，如果只是外部数据中断，主goroutine是不会结束的，所以我感觉有更好的理解方式，
// 就是将conn.Close()放到goroutine里面，这样只要连接中断，goroutine自动运行结束，外部马上接受到done这个通道的信息，主goroutine结束
func TestNetCat3(t *testing.T) {
	conn, err := net.Dial("tcp", "localhost:8000")
	if err != nil {
		log.Fatalln(err)
	}
	done := make(chan struct{})
	go func() {
		log.Println("goroutine开始运行")
		io.Copy(os.Stdout, conn) // 注意：忽略错误
		// 新加一行，在连接中断时关闭conn
		conn.Close()
		log.Println("done")
		done <- struct{}{} // 传入一个空结构体，通知主goroutine
	}()
	// 这行感觉意义不大，将标准输入写入连接中，将其注释
	// mustCopy(conn, os.Stdin)
	log.Println("结束主goroutine")
	// 本来在这里结束，为了不直接中断程序将其放入goroutine中
	// conn.Close()
	<-done // 等待后台goroutine完成
}
