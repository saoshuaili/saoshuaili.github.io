---
title: 2 lambda表达式
tags: []
categories:
  - 编程语言
  - Java
  - Java语言
  - Java语法
  - CoreJava
  - 第6章 接口和lambda表达式
date: 2022-12-15 00:31:30
---


# 1. 为什么使用 `lambda` 表达式

## `lambda` 表达式定义

`lambda` 表达式是一个**可传递的代码块**，可以在以后执行一次或多次。

## 使用 `lambda` 表达式的场景

例如我们在 [[1 接口]]中 ==接口与回调== 提过的 `swing` 中的 `Timer` 类，回忆一下：

```java
/**  
 * @author CoachHe  
 * @date 2022/12/8 01:28  
 **/
 public class TimeTest {  
  
    public static void main(String[] args) throws InterruptedException {  
        System.out.println("Begin");  
        TimePrinter listener = new TimePrinter();  
        Timer t = new Timer(1000, listener);  
        t.start();  
  
        // keep program running until the user select "OK"  
        JOptionPane.showMessageDialog(null, "Quit program?");  
        System.exit(0);  
    }  
}  
  
class TimePrinter implements ActionListener {  
    @Override  
    public void actionPerformed(ActionEvent e) {  
        System.out.println("At the tone, the time is "  
                + Instant.ofEpochMilli(e.getWhen()));  
        Toolkit.getDefaultToolkit().beep();  
    }  
}
```

可以看到，`Timer t = new Timer(1000, listener);` 中调用了 `TimePrinter` 类的实例，然后将该实例提交给了 `Timer` 对象。因为 `Timer` 对象的输入参数就是一个 `ActionListener` 接口类型的对象，所以我们不得不专门创建一个类来实现该接口，这其实是比较不方便的。

在很多语言中，可以直接处理代码块。为了支持函数式变成，设计者们设计了 `lambda` 表达式，让我们可以使用简便的**代码块方式**进行传入。

# 2. `lambda` 表达式的语法

`lambda` 表达式就是一个代码块，以及必须传入代码的变量规范。

例如我们要传入代码来检查一个字符串是否比另一个字符串短。这里要计算：

```java
(String first, String second) -> first.length() - second.length()
```

因此 `lambda` 表达式的语法如下：

==参数，箭头 (->) 以及一个表达式==。

1. 参数

注意，这里的参数一定要用括号包起来，即使 `lambda` 表达式没有参数，仍然要提供空括号，就像无参数方法一样：

```java
() -> System.out.print(1)
```

2. 箭头

这没啥好说的

3. 表达式

如果代码要完成的计算可以放在一个表达式里，那么直接写，就像上面参数中给的例子一样。如果代码要完成的计算无法在一个表达式里写完，那么把这些代码放在 `{}` 中。并包含显式的 `return` 语句。


我们不难知道，因为 `ActionListener` 中只有 `actionPerformed` 这一个方法，所以其实 `Timer` 中需要的仅仅是 `actionPerformed` 中包含的执行代码。因此 `lambda` 表达式的作用其实就是只将执行代码传入，然后会自动生成对应的对象实例，从而避免大量的重复工作。

# 3. 函数式接口

对于只有一个抽象方法的接口，需要这种接口的对象时，就可以提供一个 `lambda` 表达式。这种接口称为函数式接口。

例如，我们来看 `Arrays.sort` 方法。它的第二个参数需要一个 `Comparator` 实例，`Comparator` 就是只有一个方法的接口，所以可以提供一个 `lambda` 表达式：

我们先来看不使用 `lambda` 表达式的方式：

```java
/**  
 * @author CoachHe  
 * @date 2022/12/9 00:14  
 **/
 public class FunctionalInterfaceTest {  
    public static void main(String[] args) {  
        String[] arr = new String[]{"abc", "efc", "fslaf", "abcded"};  
        Arrays.sort(arr, new Comparator<String>() {  
            @Override  
            public int compare(String o1, String o2) {  
                return o1.length() - o2.length();  
            }  
        });  
    }  
}
```

先看看效果：

<img src=" https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/20221209002855.png" width = "90%" />

可以看到，原本乱序的数组根据字符串的长度排序了。

但是，在这里使用中我们 `new` 了一个 `Comparator` 对象，在对象里实现 `compare` 方法，然后 `Arrays.sort()` 方法会根据 `compare` 方法来比较大小，从而将 `arr` 数组按照字母的长度从左到右排序。

这里是可以使用 `lambda` 表达式进行替换的，原因是 `Comparator` 接口是只有一个抽象方法的接口，我们可以看到：

```java
@FunctionalInterface  
public interface Comparator<T> {  
    int compare(T o1, T o2);  
    boolean equals(Object obj);  
    default Comparator<T> reversed() {  
        return Collections.reverseOrder(this);  
    }  
    default Comparator<T> thenComparing(Comparator<? super T> other) {  
        Objects.requireNonNull(other);  
        return (Comparator<T> & Serializable) (c1, c2) -> {  
            int res = compare(c1, c2);  
            return (res != 0) ? res : other.compare(c1, c2);  
        };  
    }  
    default <U> Comparator<T> thenComparing(  
            Function<? super T, ? extends U> keyExtractor,  
            Comparator<? super U> keyComparator)  
    {  
        return thenComparing(comparing(keyExtractor, keyComparator));  
    }  
    default <U extends Comparable<? super U>> Comparator<T> thenComparing(  
            Function<? super T, ? extends U> keyExtractor)  
    {  
        return thenComparing(comparing(keyExtractor));  
    }  
    default Comparator<T> thenComparingInt(ToIntFunction<? super T> keyExtractor) {  
        return thenComparing(comparingInt(keyExtractor));  
    }  
    default Comparator<T> thenComparingLong(ToLongFunction<? super T> keyExtractor) {  
        return thenComparing(comparingLong(keyExtractor));  
    }  
    default Comparator<T> thenComparingDouble(ToDoubleFunction<? super T> keyExtractor) {  
        return thenComparing(comparingDouble(keyExtractor));  
    }  
    public static <T extends Comparable<? super T>> Comparator<T> reverseOrder() {  
        return Collections.reverseOrder();  
    }  
    @SuppressWarnings("unchecked")  
    public static <T extends Comparable<? super T>> Comparator<T> naturalOrder() {  
        return (Comparator<T>) Comparators.NaturalOrderComparator.INSTANCE;  
    }  
    public static <T> Comparator<T> nullsFirst(Comparator<? super T> comparator) {  
        return new Comparators.NullComparator<>(true, comparator);  
    }  
    public static <T> Comparator<T> nullsLast(Comparator<? super T> comparator) {  
        return new Comparators.NullComparator<>(false, comparator);  
    }  
    public static <T, U> Comparator<T> comparing(  
            Function<? super T, ? extends U> keyExtractor,  
            Comparator<? super U> keyComparator)  
    {  
        Objects.requireNonNull(keyExtractor);  
        Objects.requireNonNull(keyComparator);  
        return (Comparator<T> & Serializable)  
            (c1, c2) -> keyComparator.compare(keyExtractor.apply(c1),  
                                              keyExtractor.apply(c2));  
    }  
    public static <T, U extends Comparable<? super U>> Comparator<T> comparing(  
            Function<? super T, ? extends U> keyExtractor)  
    {  
        Objects.requireNonNull(keyExtractor);  
        return (Comparator<T> & Serializable)  
            (c1, c2) -> keyExtractor.apply(c1).compareTo(keyExtractor.apply(c2));  
    }  
    public static <T> Comparator<T> comparingInt(ToIntFunction<? super T> keyExtractor) {  
        Objects.requireNonNull(keyExtractor);  
        return (Comparator<T> & Serializable)  
            (c1, c2) -> Integer.compare(keyExtractor.applyAsInt(c1), keyExtractor.applyAsInt(c2));  
    }  
    public static <T> Comparator<T> comparingLong(ToLongFunction<? super T> keyExtractor) {  
        Objects.requireNonNull(keyExtractor);  
        return (Comparator<T> & Serializable)  
            (c1, c2) -> Long.compare(keyExtractor.applyAsLong(c1), keyExtractor.applyAsLong(c2));  
    }  
    public static<T> Comparator<T> comparingDouble(ToDoubleFunction<? super T> keyExtractor) {  
        Objects.requireNonNull(keyExtractor);  
        return (Comparator<T> & Serializable)  
            (c1, c2) -> Double.compare(keyExtractor.applyAsDouble(c1), keyExtractor.applyAsDouble(c2));  
    }  
}
```

对照一下方法图：

<img src=" https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/20221209002550.png" width = "50%" />

我们可以看到，只有第一个方法 `compare` 是抽象方法，其余方法都是默认方法或者是静态方法，因此都有其内部实现，只有 `compare` 这个抽象方法是一定需要重写的，所以符合 `lambda` 表达式的条件，上面的代码我们可以直接使用 `lambda` 表达式重写：

```java
/**  
 * @author CoachHe  
 * @date 2022/12/9 00:14  
 **/
 public class FunctionalInterfaceTest {  
    public static void main(String[] args) {  
        String[] arr = new String[]{"abc", "abcdedefcd", "fslaf", "cdfe"};  
        Arrays.sort(arr, (String o1, String o2) -> o1.length() - o2.length());  
        System.out.println(Arrays.toString(arr));  
    }  
}
```

看看最终的结果：

<img src=" https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/20221209003147.png" width = "90%" />

可以看到，用这么简单的代码块替换的语句可以达到完全相同的效果。

在底层，`Arrays. sort` 方法会接收实现了 `Comparator<String>` 的某个类的对象。在这个对象上调用 `compare` 方法会执行这个 `lambda` 表达式的体。

实际上，在 `Java` 中，对 `lambda` 表达式所能做的也只是转换为函数式接口。在其他支持函数字面量的程序设计语言中，可以声明函数类型 (如 `(String, String) -> int`), 声明这些类型的变量，还可以使用变量保存函数表达式。不过，`Java` 设计者还是决定保持我们熟悉的接口概念，没有为 `Java` 语言增加函数类型。

`Java API` 在 `java.util.function` 包中定义了很多非常通用的函数式接口，例如 `java.util.function` 包中有一个尤其有用的接口 `Predicate`：

`ArrayList` 类中有一个 `removeIf` 方法就是接受这个函数式接口作为参数，作为判断条件：

```java
// ArrayList类的两个方法：


    /**
     * @throws NullPointerException {@inheritDoc}
     */
    @Override
    public boolean removeIf(Predicate<? super E> filter) {
        return removeIf(filter, 0, size);
    }

    /**
     * Removes all elements satisfying the given predicate, from index
     * i (inclusive) to index end (exclusive).
     */
    boolean removeIf(Predicate<? super E> filter, int i, final int end) {
        Objects.requireNonNull(filter);
        int expectedModCount = modCount;
        final Object[] es = elementData;
        // Optimize for initial run of survivors
        for (; i < end && !filter.test(elementAt(es, i)); i++)
            ;
        // Tolerate predicates that reentrantly access the collection for
        // read (but writers still get CME), so traverse once to find
        // elements to delete, a second pass to physically expunge.
        if (i < end) {
            final int beg = i;
            final long[] deathRow = nBits(end - beg);
            deathRow[0] = 1L;   // set bit 0
            for (i = beg + 1; i < end; i++)
                if (filter.test(elementAt(es, i)))
                    setBit(deathRow, i - beg);
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
            modCount++;
            int w = beg;
            for (i = beg; i < end; i++)
                if (isClear(deathRow, i - beg))
                    es[w++] = es[i];
            shiftTailOverGap(es, w, end);
            return true;
        } else {
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
            return false;
        }
    }
```

另一个有用的函数式接口是 `Supplier<T>`:

```java
public interface Supplier<T> { 
	T get()； 
}
```

供应者 (supplier) 没有参数，调用时会生成一个 `T` 类型的值。供应者用于实现**懒计算**。

在这里给出一个用例：

```java
String fileDay = Objects.requireNonNull(fiDay, new String("20501111"));
```

可以看到，`new String("20501111")` 作为一个对象被传入到了 `requireNonNull` 方法中，如果前一个 `fiDay` 为 `null`，那就会取到后面这个对象。

但是这里前面这个 `null` 是我们预估的，实际情况下可能 `null` 的情况非常少见，那这种情况如果每次都要 `new` 一个 String 对象是比较浪费的。因此我们可以传入一个函数式接口，只有当前面一个对象是 `null` 时这个函数式接口才会生成一个对象。如下：

```java
String fiDay = "20501110";  
  
String fileDay = Objects.requireNonNull(fiDay, new String("20501111"));  
System.out.println(fileDay);  
String fileDay2 = Objects.requireNonNull(fiDay, () -> new String("20501112"));  
System.out.println(fileDay2);
```

<img src=" https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/20221209010353.png" width = "90%" />

通过打印我们可以知道，都是输出了前面那个值，所以效果是相同的。但是使用函数式接口传入无疑是更好的方式。

在这里我们还可以给出一个例子：还是在 `Flink` 中的水位线策略 `WatermarkStrategy` 接口中，有一个静态方法：

```java
static <T> WatermarkStrategy<T> forMonotonousTimestamps() {  
    return (ctx) -> {  
        return new AscendingTimestampsWatermarks();  
    };  
}
```

可以看到，很奇葩的是这里返回了一个 `lambda` 表达式而不是具体的实现了 `WatermarkStrategy` 的类，这是怎么回事？

原因很简单，`WatermarkStrategy` 也是一个函数式接口，只有一个抽象方法 `createWatermarkGenerator`，因此这个 lambda 表达式也就是对应了该抽象方法的具体实现，也就是说，可以等价于：

```java
static <T> WatermarkStrategy<T> forMonotonousTimestamps2() {  
    return new WatermarkStrategy<T>() {  
        @Override  
        public WatermarkGenerator<T> createWatermarkGenerator(WatermarkGeneratorSupplier.Context context) {  
            return new AscendingTimestampsWatermarks<>();  
        }  
    };  
}
```

可以看到，`createWatermarkGenerator` 是一个以 `WatermarkGeneratorSupplier.Context` 类型作为参数，返回值为实现了 `WatermarkGenerator` 接口的类实例的接口。因此上面两种写法是可以完全对等的。

相信通过这个例子就可以很明确了解函数式接口的定义了！


# 4. 方法引用

如果可以直接将方法传递到某个构造器中就方便很多了。

有时，`lambda` 表达式涉及一个方法。例如，假设你希望只要出现一个定时器事件就打印这个事件对象。当然，为此也可以调用：

```java
var timer = new Timer(1000, event -> System.out.printin(event));
```

但是，如果直接把 `printin` 方法传递到 `Timer` 构造器就更好了。具体做法如下：

```java
var timer = new Timer(1000, System.out::printin);
```

表达式 `System.out::printin` 是一个方法引用 (method reference), 它指示编译器生成一个函数式接口的实例，覆盖这个接口的抽象方法来调用给定的方法。在这个例子中，会生成一个 `ActionListener`, 它的 `actionPerformed` (`ActionEvent e`) 方法要调用 `System.out.println(e)`。

注意：
类似于 lambda 表达式，方法引用也不是一个对象。不过，为一个类型为函数式接口的变量赋值时会生成一个对象。

再例如，假设你想对字符串进行排序，而不考虑字母的大小写。可以传递以下方法表达式：

```java
Arrays. sort (strings, String::icompareToIgnoreCase)
```

结果如下：

<img src=" https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/20221209013212.png" width = "100%" />

从这些例子可以看出，要用 `::` 运算符分隔方法名与对象或类名。主要有 3 种情况：

1. object::instanceMethod
2. Class::instanceMethod
3. Class::staticMethod

在第 1 种情况下，方法引用等价于向方法传递参数的 `lambda` 表达式。对于 `System.out::println`, 对象是 `System.out`, 所以方法表达式等价于 `x->System.out.println(x)`。

对于第 2 种情况，第 1 个参数会成为方法的隐式参数。例如，`String::compareToIgnoreCase` 等同于 `(x,y)->x.compareToIgnoreCase(y)`。 

在第 3 种情况下，所有参数都传递到静态方法：`Math::pow` 等价于 `(x,y)->Math.pow(x,y)`。

注意，只有当 `lambda` 表达式的体只调用一个方法而不做其他操作时，才能把 `lambda` 表达式重写为方法引用。

# 6. 变量作用域（闭包）

有些时候可能希望在 `lambda` 表达式中访问外围方法或类中的变量。例如：

```java
public class TestVariableUsageField {  
    static String text2 = "Hello2";  
    public static void main(String[] args) throws InterruptedException {  
        repeatMessage("Hello", 1000);  
        Thread.sleep(500000);  
    }  
    // 在lambda表达式中访问外围方法或类中的变量  
    public static void repeatMessage(String text, int delay) {  
        ActionListener listener = event -> {  
            System.out.println(text);  
            System.out.println(text2);  
            Toolkit.getDefaultToolkit().beep();  
        };  
        new Timer(delay, listener).start();  
    }  
}
```

运行结果如下：

<img src=" https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/20221211012238.png" width = "90%" />

可以看到，在 `lambda` 表达式中访问到了外围方法中的字段 `text` 和类字段 `text2`，这两个字段都不是在 `lambda` 表达式中定义的。

再想想看，这里好像有问题，尽管不那么明显。`lambda` 表达式的代码可能在 `repeat-Message` 调用返回很久以后才运行，而那时这个参数变量已经不存在了。如何保留 `text` 变量呢？

在 `Java` 中，要确保所捕获的值是明确定义的，这里有一个重要的限制。在 `lambda` 表达式中，只能引用值不会改变的变量。

```java
// 不合法的做法，引用值被改变了  
public static void countDown(int start, int delay) {  
    ActionListener listener = event -> {  
        start--;  
        System.out.println(start);  
    };  
    new Timer(delay, listener).start();  
}
```

可以看到，编译器直接报错了。这个限制是有原因的。如果在 `lambda` 表达式中更改变量，并发执行多个动作时就会不安全。

<img src=" https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/20221211023739.png" width = "90%" />

另外如果在 lambda 表达式中引用一个变量，而这个变量可能在外部改变，这也是不合法的。例如：

```java
// 不合法的做法2，lambda表达式中引用的对象在外部可能会改变。  
public static void repeat(String text, int count) {  
    for (int i = 0; i < count; i++) {  
        ActionListener listener = event -> {  
            System.out.println(i + ": " + text);  
            };  
        new Timer(1000, listener).start();  
    }  
}
```

这里也会报错：

<img src=" https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/20221211024026.png" width = "90%" />

可以看到，和上面的报错是相同的。

`lambda` 表达式中捕获的变量必须实际上是事实最终变量 (effectively final) 。事实最终变量是指，这个变量初始化之后就不会再为它赋新值。

