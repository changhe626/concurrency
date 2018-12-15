package com.onyx.concurrency.example.syncContainer;

import com.onyx.concurrency.annotaions.NotThreadSafe;
import java.util.Vector;

/**
 * 同步容器不一定是线程安全的
 *
 * Stack
 */
@NotThreadSafe
public class VectorDemo2 {

    private static Vector<Integer> vector = new Vector();

    public static void main(String[] args) {
        while (true) {
            for (int i = 0; i < 10; i++) {
                vector.add(i);
            }

            new Thread(() -> {
                for (int i = 0; i < vector.size(); i++) {
                    vector.remove(i);
                }
            }).start();

            new Thread(() -> {
                for (int i = 0; i < vector.size(); i++) {
                    vector.get(i);
                }
            }).start();
        }

    }


}
