package hello.scheduling_tasks;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * http://docs.spring.io/spring/docs/current/spring-framework-reference/html/scheduling.html#scheduling-annotation-support-scheduled
 * http://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/scheduling/support/CronSequenceGenerator.html
 */
@EnableScheduling
class ScheduledTasks {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    // If a fixed rate execution is desired, simply change the property name specified within the annotation.
    // The following would be executed every 5 seconds measured between the successive start times of each invocation
    @Scheduled(fixedRate = 5000l)
    public void reportCurrentTime() {
        System.out.println("The time is now " + dateFormat.format(new Date()));
    }

//    // the following method would be invoked every 5 seconds with a fixed delay,
//    // meaning that the period will be measured from the completion time of each preceding invocation
//    @Scheduled(fixedDelay = 5000l)
//    void fixedDelay() {
//        println 'fixed delay'
//    }
//
//    // For fixed-delay and fixed-rate tasks, an initial delay may be specified indicating
//    // the number of milliseconds to wait before the first execution of the method.
//    @Scheduled(initialDelay = 1000l, fixedRate = 5000l)
//    void fixedRateInitialDelay() {
//        println 'fixedRateInitialDelay'
//    }
//
//    // If simple periodic scheduling is not expressive enough, then a cron expression may be provided.
//    // For example, the following will only execute on weekdays.
//    @Scheduled(cron = "*/5 * * * * MON-FRI")
//    void cron() {
//        println "cron"
//        // something that should execute on weekdays only
//    }
}
