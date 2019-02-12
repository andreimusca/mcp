package ro.ams.mcp.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;

/**
 * Created by Andrei Musca on 2/12/19
 */
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    property = "message_type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = CALLMessage.class, name = "CALL"),
    @JsonSubTypes.Type(value = MSGMessage.class, name = "MSG")
})
@Data
public class AbstractMessage implements Message{
  private MESSAGE_TYPE message_type;
  private Long timestamp;
  private Long origin;
  private Long destination;

  @Override
  public boolean hasMissingFields() {
    return timestamp==null || origin==null || destination==null;
  }
}
