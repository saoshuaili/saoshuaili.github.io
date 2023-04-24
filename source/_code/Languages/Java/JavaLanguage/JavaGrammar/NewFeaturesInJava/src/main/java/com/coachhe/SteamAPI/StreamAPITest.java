package com.coachhe.SteamAPI;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * 1. Stream关注的是对数据的运算，与CPU打交道
 *      集合关注的是数据的存储，与内存打交道
 * 2. Stream的执行流程
 *      Stream自己不会存储元素
 *      Stream不会改变源对象。它们会返回一个持有结果的新Stream
 *      Stream操作是延迟执行的。这意味着他们会等到需要结果的时候才执行
 * 3. Stream执行流程
 *      1. Stream的实例化
 *      2. 一系列的中间操作
 *      3. 终止操作
 * 4. 说明
 *      1. 一个中间操作链，对数据源的数据进行处理
 *      2. 一旦执行终止操作，就执行中间操作链，并产生结果。之后，不会再被用
 */
public class StreamAPITest {

    // 创建Stream
    @Test
    public void test1(){

        // 1. 方式1：通过集合
        List<String> lists = new ArrayList<>();
        lists.add("e1");
        lists.add("e2");
        lists.add("e3");
        lists.add("e4");
        Stream<String> stream = lists.stream(); // 返回一个顺序流
        Stream<String> parStream = lists.parallelStream(); // 返回一个并行流

        // 2. 方式2：通过数组
        Stream<String> arrStream = Arrays.stream(new String[]{"e1", "e2", "e3"});

        // 3. 方法3：通过Stream的of()
        Stream<Integer> integerStream = Stream.of(1, 2, 3, 4, 5);

        // 4. 方法4：创建无限流
        Stream.iterate(0, t -> t + 2).limit(10).forEach(System.out::println);

        // 5. generate
        Stream.generate(Math::random).limit(10).forEach(System.out::println);


    }

    /**
     * 测试中间操作
     */
    @Test
    public void test2(){

        // 1. 筛选与切片
        Stream<Integer> arrStream = Arrays.stream(new Integer[]{1, 2, 3, 4, 5});
        // 找出所有大于3的值,用filter
        arrStream.filter(e -> e > 3).forEach(System.out::println);

        // 截断流 用limit
        // 注意，上面的终止操作会导致stream终止，就不能再使用了，因此要重新生成
        arrStream = Arrays.stream(new Integer[]{1, 2, 3, 4, 5});
        arrStream.limit(3).forEach(System.out::println);

        // 跳过元素，扔掉前n个元素，如果不满足n个，返回一个空的流
        arrStream = Arrays.stream(new Integer[]{1, 2, 3, 4, 5});
        arrStream.skip(30).forEach(System.out::println);

        // 去重 distinct
        arrStream = Arrays.stream(new Integer[]{1, 2, 3, 4, 5, 1, 3, 4});
        arrStream.distinct().forEach(System.out::println);
    }


}
