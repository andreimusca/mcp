package ro.ams.mcp.model;

import lombok.Data;

/**
 * Created by Andrei Musca on 2/12/19
 */
@Data
public class MSGMessage extends AbstractMessage {
  private String message_content;
  private MessageStatus message_status;

  public MSGMessage() {
    setMessage_type(MESSAGE_TYPE.MSG);
  }

  @Override
  public boolean hasMissingFields() {
    return super.hasMissingFields() || message_content == null || message_status == null;
  }


  public enum MessageStatus {DELIVERED, SEEN}
}
