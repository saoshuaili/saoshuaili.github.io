package com.coachhe.SteamAPI;

import org.junit.Test;

import java.util.*;
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
     * 测试中间操作1
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

    /**
     * 测试中间操作2 映射 */
    @Test
    public void test3(){

        Stream<Integer> arrStream = Arrays.stream(new Integer[]{1, 2, 3, 4, 5});

        // map操作
        arrStream.map(e -> e + 1).forEach(System.out::println);

        // flatMap，接受一个函数作为参数，将流中的每个值都换成另一个流，然后把所有流连接成一个流
        // 类似下面的操作
        ArrayList<Integer> arr1 = new ArrayList<>();
        arr1.add(1);
        arr1.add(2);
        arr1.add(3);
        ArrayList<Integer> arr2 = new ArrayList<>();
        arr2.add(4);
        arr2.add(5);
        arr2.add(6);
        arr1.addAll(arr2);
        System.out.println(arr1);
        // 进一步了解
        List<String> list = Arrays.asList("aa", "bb", "cc", "dd");
        // 可以看到，list.stream()首先获取到了一个Stream<String>，然后通过map，里面的每个String元素又变成了Stream<Character>的类型
        // 因此，最终变成了Stream<Stream<Character>>这样的类型，Stream里面还有个Stream
        // 如果我们想获取每个Character，那就很麻烦了
        list.stream().map(StreamAPITest::fromStringToStream).forEach(System.out::println);
        // 这里就需要使用flatMap
        list.stream().flatMap(StreamAPITest::fromStringToStream).forEach(System.out::println);

    }

    public static Stream<Character> fromStringToStream(String str) {
        List<Character> list = new ArrayList<>();
        for (char c : str.toCharArray()) {
            list.add(c);
        }
        return list.stream();
    }

    // 中间操作3 - 排序操作
    @Test
    public void test4(){
        // sorted() 自然排序
        List<Integer> list = Arrays.asList(12, 43, 64, 2, 5);
        list.stream().sorted().forEach(System.out::println);

        // sorted(Comparator com) 定制排序,例如这里打印了从大到小比较的结果
        list.stream().sorted((e1, e2) -> e2 - e1).forEach(System.out::println);
    }

    // 终止操作1 - 匹配与查找
    @Test
    public void test5(){

        List<Integer> list = Arrays.asList(12, 43, 64, 2, 5);

        // 是否全部匹配 allMatch
        boolean b = list.stream().allMatch(e -> e > 6);
        System.out.println(b);

        // 是否存在某一个元素  anyMatch
        boolean b1 = list.stream().anyMatch(e -> e > 6);
        System.out.println(b1);

        // 是否所有元素都不满足 noneMatch
        boolean b2 = list.stream().noneMatch(e -> e > 6);
        System.out.println(b2);

        // 查找第一个元素 findFirst
        Optional<Integer> first = list.stream().findFirst();
        System.out.println(first);

        // 返回当前流任何符合的元素 findAny
        Optional<Integer> any = list.stream().findAny();
        System.out.println(any);

        // 求个数 count
        long count = list.stream().filter(e -> e > 6).count();
        System.out.println(count);

        // 求最大值 max
        Optional<Integer> max = list.stream().max(Comparator.comparingInt(e -> e));
        System.out.println(max);

        // 求最小值 min
        Optional<Integer> min = list.stream().max((e1, e2) -> e2 - e1);
        System.out.println(min);

        // 内部迭代 forEach
        list.stream().filter(e -> e > 6).forEach(System.out::println);

    }

    // 终止操作2 - 规约
    @Test
    public void test6(){

        List<Integer> list = Arrays.asList(12, 43, 64, 2, 5);

        // reduce(T identity, BinaryOperator) -> 可以将流中元素反复结合起来，得到一个值，返回T
        // 这里是有初始值T的情况
        Integer reduce = list.stream().reduce(8, Integer::sum);
        // 解释，在这里，第一个参数identity是一个起始值，我随便写了个8，代表第一个元素是8
        // 然后接下来的操作是Integer::sum，会一直读取stream中的元素，和第一个元素一起执行sum操作
        // 在这里就是第一个元素是8，然后和stream中的第一个元素12执行了sum操作，得到20
        // 然后20再和第二个元素43做sum操作，得到63，以此类推，得到最终的值
        System.out.println(reduce);

        // reduce(BinaryOperator) -> 可以将流中元素反复结合起来，得到一个值。
        // 这里是没有初始值的情况
        Optional<Integer> reduce1 = list.stream().reduce(Integer::sum);
        System.out.println(reduce1);

    }

}
