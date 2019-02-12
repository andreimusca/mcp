package ro.ams.mcp.events;

import lombok.Data;

/**
 * Created by Andrei Musca on 2/12/19
 */
@Data
public class ProcessedJsonFileEvent implements GenericEvent{
  private final String source;
  private final long duration;
}
