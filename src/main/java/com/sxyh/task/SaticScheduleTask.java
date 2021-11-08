package com.sxyh.task;

import net.javacrumbs.shedlock.core.SchedulerLock;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;

@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
public class SaticScheduleTask {

    @Scheduled(cron = "0/5 * * * * ?")
    //每5秒执行一次，至少锁3秒，最多锁4秒
    @SchedulerLock(name = "taskJob1", lockAtLeastFor = 2*1000,lockAtMostFor = 4*1000)
    /**
     *     name：主键，用来识别某个定时任务
     *     lockAtMostFor(锁持有的最大时间（ms）)：
     *              当前任务获取的锁的最大持有时间为5s，5s之后就算没有执行完，其他定时任务也可以获取锁去执行任务
     *
     *     lockAtLeastFor(锁持有的最小时间（ms）)：
     *                  就算当前定时任务已经执行完成，其他任务也无法获得锁，必须等2s之后才能获取
     *     lockAtMostForString：lockAtMostFor的字符串，例如“PT14M”表示为14分钟，单位可以是S,M,H
     *     lockAtLeastForString：lockAtLeastFor的字符串，例如“PT14M”表示为14分钟,单位可以是S,M,H
     */
    private void configureTasks() {
        System.err.println("执行静态定时任务时间: " + LocalDateTime.now());
    }
}
