package __goroutine

import (
	"fmt"
	"time"
)

// @Author: coachhe
// @Date: 2022/8/25 下午1:20

func printNumbers1() {
	for i := 0; i < 10; i++ {
		//fmt.Printf("%d ", i)
	}
}

func printLetter1() {
	for i := 'A'; i < 'A'+10; i++ {
		//fmt.Printf("%c ", i)
	}
}

func print1() {
	printNumbers1()
	printLetter1()
}

func goPrint1() {
	go printNumbers1()
	go printLetter1()
}

func printNumbers2() {
	for i := 0; i < 10; i++ {
		time.Sleep(1 * time.Millisecond)
		fmt.Printf("%d ", i)
	}
}

func printLetter2() {
	for i := 'A'; i < 'A'+10; i++ {
		time.Sleep(1 * time.Millisecond)
		fmt.Printf("%c ", i)
	}
}

func goPrint2() {
	go printNumbers2()
	go printLetter2()
}
