package ro.ams.mcp.events;

import lombok.Data;
import ro.ams.mcp.model.Message;

/**
 * Created by Andrei Musca on 2/12/19
 */
@Data
public class MessageReadEvent implements GenericEvent{
  private final String source;
  private final Message message;
}
