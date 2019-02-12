package ro.ams.mcp.aop;

import lombok.SneakyThrows;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import ro.ams.mcp.events.ProcessedJsonFileEvent;
import ro.ams.mcp.service.EventPublisher;
import ro.ams.mcp.service.MessageProcessorService;

/**
 * Created by Andrei Musca on 2/10/19
 */
@Aspect
@Component
public class ProcessTimingAspect {


  private EventPublisher eventPublisher;

  @Autowired
  public ProcessTimingAspect(EventPublisher eventPublisher) {
    this.eventPublisher = eventPublisher;
  }

  @Pointcut("@annotation(Timed)")
  public void timedMethods() {
  }


  @Around(value = "timedMethods()")
  @SneakyThrows
  public void logMethod(ProceedingJoinPoint jp) {
    StopWatch stopWatch = new StopWatch();
    stopWatch.start();
    try {
      jp.proceed(jp.getArgs());
      stopWatch.stop();
      long duration = stopWatch.getTotalTimeMillis();
      if (jp.getTarget() instanceof MessageProcessorService) {
        ProcessedJsonFileEvent event = new ProcessedJsonFileEvent((String) jp.getArgs()[0], duration);
        eventPublisher.publish(event);
      }
    } finally {
      if (stopWatch.isRunning()) {
        stopWatch.stop();
      }
    }

  }
}
