package com.coachhe.chapter06_WindowAndTime.时间;

import com.coachhe.chapter05_DataStreamAPI.Event;
import org.apache.flink.api.common.eventtime.*;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.time.Duration;

/**
 * @author CoachHe
 * @date 2022/12/4 04:01
 **/
public class WatermarkTest {

    public static void main(String[] args) throws Exception {



        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        env.getConfig().setAutoWatermarkInterval(100);

        env.fromElements(
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
//                .assignTimestampsAndWatermarks( WatermarkStrategy.<Event>forMonotonousTimestamps() .withTimestampAssigner(new SerializableTimestampAssigner<Event>() { @Override
//                            public long extractTimestamp(Event event, long l) {
//                                return event.timestamp;
//                            }
//                        })) ;
                // 乱序流的watermark生成
//                .assignTimestampsAndWatermarks(WatermarkStrategy.<Event>forBoundedOutOfOrderness(Duration.ofSeconds(2))
//                        .withTimestampAssigner(new SerializableTimestampAssigner<Event>() {
//                            @Override
//                            public long extractTimestamp(Event event, long l) {
//                                return event.timestamp;
//                            }
//                        })
//                );
                // 自定义的watermark生成
                .assignTimestampsAndWatermarks(new WatermarkStrategy<Event>() {
                    @Override
                    public WatermarkGenerator<Event> createWatermarkGenerator(WatermarkGeneratorSupplier.Context context) {
                        return new CustomBoundedOutOfOrdernessGenerator();
                    }
                }.withTimestampAssigner(new SerializableTimestampAssigner<Event>() {
                    @Override
                    public long extractTimestamp(Event event, long l) {
                        return event.timestamp;
                    }
                }));
    }
}

class CustomBoundedOutOfOrdernessGenerator implements WatermarkGenerator<Event> {

    private Long delayTime = 5000L; // 延迟时间
    private Long maxTx = -Long.MAX_VALUE + delayTime + 1L; // 观察到的最大时间

    @Override
    public void onEvent(Event event, long l, WatermarkOutput watermarkOutput) {
        // 每来一条数据就调用一次
        maxTx = Math.max(event.timestamp, maxTx); // 更新最大时间戳
    }

    @Override
    public void onPeriodicEmit(WatermarkOutput watermarkOutput) {
        // 发射水位线，默认200ms调用一次
        watermarkOutput.emitWatermark(new Watermark(maxTx - delayTime - 1L));
    }
}
