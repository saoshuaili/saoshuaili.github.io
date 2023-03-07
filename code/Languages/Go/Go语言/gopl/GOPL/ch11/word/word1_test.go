package word

import "testing"

//@author: coachhe
//@create: 2022/8/9 19:25

// IsPalindrome 判断一个字符串是否是回文字符串
// (Our first attempt.)
func isPalindrome(s string) bool {
	for i := range s {
		if s[i] != s[len(s)-1-i] {
			return false
		}
	}
	return true
}

func TestPalindrome(t *testing.T) {
	if !isPalindrome("detartrated") {
		t.Error(`IsPalindrome("detartrated") = false`)
	}
	if !isPalindrome("kayak") {
		t.Error(`IsPalindrome("kayak") = false`)
	}
}

func TestNonPalindrome(t *testing.T) {
	if isPalindrome("palindrome") {
		t.Error(`IsPalindrome("palindrome") = true`)
	}
}
