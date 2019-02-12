package ro.ams.mcp.service.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import ro.ams.mcp.events.GenericEvent;
import ro.ams.mcp.service.EventPublisher;

/**
 * Created by Andrei Musca on 2/12/19
 */
@Component
public class SpringEventPublisher implements EventPublisher {

  private ApplicationEventPublisher applicationEventPublisher;

  @Autowired
  public SpringEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
    this.applicationEventPublisher = applicationEventPublisher;
  }


  @Override
  public void publish(GenericEvent genericEvent) {
    applicationEventPublisher.publishEvent(genericEvent);
  }
}
