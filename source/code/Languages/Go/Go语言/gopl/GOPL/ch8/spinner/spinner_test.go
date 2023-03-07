package main

import (
	"fmt"
	"testing"
	"time"
)

// @Author: coachhe
// @Date: 2022/8/24 下午2:09

func TestSpinner(t *testing.T) {
	//会一直执行,直到main函数返回后所有goroutine全部暴力终止
	go spinner(100 * time.Millisecond)
	const n = 45
	fibN := fib(n)
	fmt.Printf("\rFibonacci(%d) = %d\n", n, fibN)
}
