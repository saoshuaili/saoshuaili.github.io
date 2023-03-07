package netcat

import (
	"io"
	"log"
)

//@author: coachhe
//@create: 2022/8/9 20:21

func mustCopy(dst io.Writer, src io.Reader) {
	if _, err := io.Copy(dst, src); err != nil {
		log.Fatal(err)
	}
}
