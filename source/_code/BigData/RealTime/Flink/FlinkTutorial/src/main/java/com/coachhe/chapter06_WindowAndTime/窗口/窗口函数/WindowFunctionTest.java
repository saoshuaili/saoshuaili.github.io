package com.coachhe.chapter06_WindowAndTime.窗口.窗口函数;

import com.coachhe.chapter05_DataStreamAPI.Event;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;

/**
 * @author CoachHe
 * @date 2022/12/13 01:19
 **/
public class WindowFunctionTest {
    public static void main(String[] args) {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        env.getConfig().setAutoWatermarkInterval(100);

        SingleOutputStreamOperator<Event> stream = env.fromElements(
                        new Event("Mary", "./home", 1000L),
                        new Event("Bob", "./cart", 2000L),
                        new Event("Alice", "./prod?id=100", 3000L),
                        new Event("Alice", "./prod?id=200", 3500L),
                        new Event("Bob", "./prod?id=2", 2500L),
                        new Event("Alice", "./prod?id=300", 3600L),
                        new Event("Bob", "./home", 3000L),
                        new Event("Bob", "./prod?id=1", 2300L),
                        new Event("Bob", "./prod?id=3", 3300L))
                // 有序流的watermark生成，一般做测试用
                .assignTimestampsAndWatermarks(WatermarkStrategy
                        .<Event>forMonotonousTimestamps()
                        .withTimestampAssigner((SerializableTimestampAssigner<Event>) (event, l) -> event.timestamp));


        stream
                .keyBy(data -> data.user)
                .window(TumblingEventTimeWindows.of(Time.hours(1)))
                .reduce((ReduceFunction<Event>) (event, t1) -> new Event());

    }
}
