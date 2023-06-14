package com.coachhe.jmm;

import sun.misc.Unsafe;

/**
 * @PROJECT_NAME: JUC
 * @DESCRIPTION:
 * @AUTHOR: CoachHe
 * @DATE: 2023/5/28 18:01
 */
public class DoubleCheckLockSingleton {

    private static DoubleCheckLockSingleton instance = null;

    private DoubleCheckLockSingleton() {
        Unsafe unsafe = Unsafe.getUnsafe();
    }

    // DCL
    public static DoubleCheckLockSingleton getInstance() {
        if (instance == null) {
            synchronized (DoubleCheckLockSingleton.class) {
                if (instance == null) {
                    instance = new DoubleCheckLockSingleton();
                }
            }
        }
        return instance;
    }

    public static void main(String[] args) {
        DoubleCheckLockSingleton instance = DoubleCheckLockSingleton.getInstance();
    }

}
