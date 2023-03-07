package com.coachhe.S1_再论向上转型.出现的问题;

/**
 * @author CoachHe
 * @date 2022/12/21 13:07
 * 继承与向上转型
 **/
public class Music {
    public static void tune(Instrument instrument) {
        instrument.play(Note.MIDDLE_C);
    }

    public static void main(String[] args) {
        Wind flute = new Wind();
        tune(flute);
    }
}
