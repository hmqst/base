package com.example.base.schedule;

import com.example.base.utils.DataUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executor;

@Component
@EnableScheduling
@Slf4j
@Async // 使用Async线程池，而不是scheduling单线程
public class MySchedule {

    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * initialDelay 首次执行的延迟时间
     * fixedRate当前任务开始执行5s后开启下个任务
     */
    // @Scheduled(initialDelay = 1000, fixedRate = 2000)
    public void initialDelay() {
        log.info("定时任务:" + format.format(new Date()) + Thread.currentThread().getName());
    }

    /**
     * cron表达式（秒 分 时 天(月) 月 天(周) 年份(一般省略)）
     * *  表示所有可能的值
     * /  表示数值的增量，简单来说，比如分写上0/5表示从0分开始，每隔5分钟
     * ?  仅用在天(月)和天(周)，表示不指定值，当其中一个有值时，另外一个需要设为?
     * <p>
     * 秒：0-59 , - * /
     * 26：表示第26秒
     * 5,16,27：表示第5秒、第16秒和第27秒
     * 1-5：表示从1秒到5秒，就是1 2 3 4 5 秒
     * *：表示每一秒
     * 2/15：表示从第2秒开始，每隔15秒，就是2 17 32 47 秒
     * 分：0-59 , - * /
     * 根据上述的秒，同理可得
     * 26：表示第26分钟
     * 5,16,27：表示第5分钟、第16分钟和第27分钟
     * 1-5：表示从1秒到5分，就是第1 2 3 4 5 分钟
     * *：表示每一分钟
     * 2/15：表示从第2分钟开始，每隔15分钟，就是2 17 32 47 分钟
     * 时：0-23 , - * /
     * 10：表示早上10点
     * 22：表示晚上10点
     * 2,8,18,23：表示凌晨2点，早上8点，傍晚6点和晚上11点
     * 9-17：表示朝九晚五，就是9 10 11 12 13 14 15 16 17 点
     * *：表示每小时
     * 10/2：表示从早上10点开始，每隔2小时
     * 天（月）：1-31 , - * / ? L W C
     * 31：表示31号（注意有的月份是没有31号的）
     * 5,10,15：表示5号、10号和15号
     * 8-17：表示8号到17号
     * *：表示所有可能的值（当指定为星号时，天（周）需指定为?）
     * 5/9：表示从5号开始，每隔9天
     * ？：当天（周）指定任意值的时候，天（月）需要设置为?
     * L：表示一个月中的最后一天
     * 6L：表示倒数第6天
     * 15W：W指工作日，如果15号不是周六日，则表示15号当天；如果15号是周六，则表示14号；如果15号是周日，则表示16号；如果31W是周日，则31W表示29号（周五），不会跨月
     * LW：指最后一个工作日
     * 15C：表示第15天（不是很明白这个，使用15和15C貌似效果相同？）
     * 月：1-12 , - * /
     * 10：表示10月份
     * 5,10,11：表示5月份、10月份和11月份
     * 4-8：表示4月份到8月份
     * *：表示所有可能的值
     * 5/2：表示从5月份开始，每隔2个月，（就是5 7 9 11 次年5 次年7 次年9......）
     * 1-12：也可以用JAN-DEC表示
     * 天（周）：MON-SAT , - * / ? L C #
     * MON：表示周一
     * MON,TUE,FRI：表示周一，周二和周五
     * MON-FRI：表示从周一到周五
     * MON-WED,SAT：表示从周一到周三，和周六（就是周一、周二、周三和周六）
     * mon,tue,wed,thu,fri,sat,sun：表示周一到周日（大小写均可，取星期英文前三个字母，注意星期四是thu，而不是thur）
     * 7L：表示最后一个周六（效果等同于SATL）（L表示last最后的意思，7表示周六，6表示周五，类推，1表示周日）
     * 6C：表示周五以及之后包含周五的日期（感觉效果FRI一样，具体还没深究）
     * 7#2：表示第二个周六（#前面的数字表示星期，1表示周日，2表示周一，以此类推；#后面的数字表示第几个）
     * 年：留空 2020-2099 , - * /
     * 2050：表示2050年
     * 2020,2030,2040：表示2020年、2030年和2040年
     * 2020-2030：表示2020年到2030年
     * *：表示所有可能的值
     * 2020/10：表示从2020年开始，每隔10年，（就是2020,2030,2040......）
     * <p>
     * "0 0 12 * * ?" 每天中午12点触发
     * "0 15 10 ? * *" 每天上午10:15触发
     * "0 15 10 * * ?" 每天上午10:15触发
     * "0 15 10 * * ? *" 每天上午10:15触发
     * "0 15 10 * * ? 2005" 2005年的每天上午10:15触发
     * "0 * 14 * * ?" 在每天下午2点到下午2:59期间的每1分钟触发
     * "0 0/5 14 * * ?" 在每天下午2点到下午2:55期间的每5分钟触发
     * "0 0/5 14,18 * * ?" 在每天下午2点到2:55期间和下午6点到6:55期间的每5分钟触发
     * "0 0-5 14 * * ?" 在每天下午2点到下午2:05期间的每1分钟触发
     * "0 10,44 14 ? 3 WED" 每年三月的星期三的下午2:10和2:44触发
     * "0 15 10 ? * MON-FRI" 周一至周五的上午10:15触发
     * "0 15 10 15 * ?" 每月15日上午10:15触发
     * "0 15 10 L * ?" 每月最后一日的上午10:15触发
     * "0 15 10 ? * 6L" 每月的最后一个星期五上午10:15触发
     * "0 15 10 ? * 6L 2002-2005" 2002年至2005年的每月的最后一个星期五上午10:15触发
     * "0 15 10 ? * 6#3" 每月的第三个星期五上午10:15触发
     * "0 * * * * ?" 每分钟只醒一次
     */

    @Scheduled(cron = "0 * * * * ?")
    public void cron() {
        log.info("cron表达式每分钟定时任务:" + format.format(new Date()) + Thread.currentThread().getName());
    }
}