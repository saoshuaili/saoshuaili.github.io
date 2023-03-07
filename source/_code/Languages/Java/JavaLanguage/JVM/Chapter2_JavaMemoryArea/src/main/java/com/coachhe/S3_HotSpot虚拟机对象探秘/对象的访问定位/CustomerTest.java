package com.coachhe.S3_HotSpot虚拟机对象探秘.对象的访问定位;

/**
 * @author CoachHe
 * @date 2023/1/6 00:44
 * 利用这个程序来对应JVM第二章笔记的图示，明确对象的访问定位在堆内存中的结构
 **/
public class CustomerTest {
    public static void main(String[] args) {
        Customer cust = new Customer();
    }
}

class Customer {
    int id = 1001;
    String name;
    Account acct;
    {
        name = "匿名客户";
    }
}

class Account {

}
