package ro.ams.mcp.service;

import ro.ams.mcp.events.GenericEvent;

/**
 * Created by Andrei Musca on 2/12/19
 */
public interface EventPublisher {

  void publish(GenericEvent genericEvent);
}
