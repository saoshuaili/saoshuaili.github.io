package com.coachhe.S1_再论向上转型.出现的问题;

/**
 * @author CoachHe
 * @date 2022/12/21 13:06
 * Wind对象是一种Instrument
 * 因为他们有相同的接口
 **/
public class Wind extends Instrument{
    // 重新定义接口方法
    @Override
    public void play(Note note) {
        System.out.println("Wind.play " + note);
    }

}
