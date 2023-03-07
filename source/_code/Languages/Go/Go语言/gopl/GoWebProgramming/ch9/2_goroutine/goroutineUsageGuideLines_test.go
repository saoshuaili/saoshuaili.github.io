package __goroutine

import (
	"testing"
	"time"
)

// @Author: coachhe
// @Date: 2022/8/25 下午1:28

func TestPrint1(t *testing.T) {
	print1()
}

func TestGoPrint1(t *testing.T) {
	goPrint1()
	time.Sleep(1 * time.Second)
}

func TestGoPrint2(t *testing.T) {
	goPrint2()
	time.Sleep(1 * time.Second)
}

func BenchmarkPrint1(b *testing.B) {
	for i := 0; i < b.N; i++ {
		print1()
	}
}

func BenchmarkGoPrint1(b *testing.B) {
	for i := 0; i < b.N; i++ {
		goPrint1()
	}
}
