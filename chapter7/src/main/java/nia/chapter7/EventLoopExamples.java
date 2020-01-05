package nia.chapter7;

import java.util.Collections;
import java.util.List;

/**
 * 代码清单 7-1 在事件循环中执行任务
 *
 * @author <a href="mailto:norman.maurer@gmail.com">Norman Maurer</a>
 */
public class EventLoopExamples {
    /**
     * 代码清单 7-1 在事件循环中执行任务
     */
    public static void executeTaskInEventLoop() {
        boolean terminated = true;
        // ...
        while (!terminated) {
            // 阻塞，直到有事件已经就绪可被运行(blockUntilEventsReady:返回任务集合)
            List<Runnable> readyEvents = blockUntilEventsReady();
            for (Runnable ev : readyEvents) {
                // 循环遍历，并处理所有的事件
                ev.run();
            }
        }
    }

    /**
     * 返回任务集合
     *
     * @return 任务集合
     */
    private static final List<Runnable> blockUntilEventsReady() {
        return Collections.<Runnable>singletonList(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
