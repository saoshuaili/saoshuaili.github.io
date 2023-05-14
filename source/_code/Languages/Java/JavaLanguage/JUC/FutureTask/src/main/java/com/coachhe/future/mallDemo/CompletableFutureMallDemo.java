package com.coachhe.future.mallDemo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @PROJECT_NAME: JUC
 * @AUTHOR: CoachHe
 * @DATE: 2023/5/11 0:35
 * @DESCRIPTION: 1需求说明
 * 1.1同一款产品，同时搜索出同款产品在各大电商平台的售价；
 * 1.2同一款产品，同时搜索出本产品在同一个电商平台下，各个入驻卖家售价是多少
 * <p>
 * 2输出返回：
 * 出来结果希望是同款产品的在不同地方的价格清单列表， 返回一个List<String>
 * 《mysql》in jd price is 88.05
 * 《mysql》in dang dang price is 86.11
 * 《mysql》in tao bao price is 90.43
 * <p>
 * 3解决方案，比对同一个商品在各个平台上的价格，要求获得一个清单列表
 * 1   stepbystep   ， 按部就班， 查完京东查淘宝， 查完淘宝查天猫......
 * 2   all in       ，万箭齐发，一口气多线程异步任务同时查询。。。
 */
public class CompletableFutureMallDemo {

    static List<NetMall> list = Arrays.asList(
            new NetMall("jd"),
            new NetMall("dangdang"),
            new NetMall("taobao")
    );

    /**
     * step by step
     *
     * @param list
     * @param productName
     * @return
     */
    public static List<String> getPriceStepByStep(List<NetMall> list, String productName) {
        return list
                .stream()
                .map(netMall ->
                        String.format(productName + " in %s pr=ice is %.2f",
                                netMall.getNetMallName(),
                                netMall.calcPrice(productName)))
                .collect(Collectors.toList());
    }

    /**
     * 万箭齐发
     *
     * @param list
     * @param productName
     * @return
     */
    public static List<String> getPriceByCompletableFuture(List<NetMall> list, String productName) {
        return list.stream()
                .map(netMall -> {
                            System.out.printf("map1 print: %s\n", netMall.getNetMallName());
                            return CompletableFuture.supplyAsync(
                                    () -> String.format(productName + " in %s pr=ice is %.2f", netMall.getNetMallName(), netMall.calcPrice(productName))
                            );
                        }
                )
                .toList()
                .stream()
                .map(s -> {
                    System.out.printf("map2 print: %s\n", s.join());
                    return s.join();
                })
                .collect(Collectors.toList());

        // 第二种等价的写法
//        List<CompletableFuture<String>> list1 = list.stream()
//                .map(netMall -> {
//                            System.out.printf("map1 print: %s\n", netMall.getNetMallName());
//                            return CompletableFuture.supplyAsync(
//                                    () -> String.format(productName + " in %s pr=ice is %.2f", netMall.getNetMallName(), netMall.calcPrice(productName))
//                            );
//                        }
//                )
//                .toList();
//        return list1
//                .stream()
//                .map(netMall -> {
//                            System.out.printf("map1 print: %s\n", netMall.getNetMallName());
//                            return CompletableFuture.supplyAsync(
//                                    () -> String.format(productName + " in %s pr=ice is %.2f", netMall.getNetMallName(), netMall.calcPrice(productName))
//                            );
//                        }
//                )
//                .toList();
    }

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
//        List<String> list1 = getPriceStepByStep(list, "mysql");
        List<String> list1 = getPriceByCompletableFuture(list, "mysql");
        for (String element : list1) {
            System.out.println(element);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("---cost time: " + (endTime - startTime) + " 毫秒");

    }


}

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
class NetMall {
    private String netMallName;

    public double calcPrice(String productName) {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return ThreadLocalRandom.current().nextDouble() * 2 + productName.charAt(0);
    }
}
