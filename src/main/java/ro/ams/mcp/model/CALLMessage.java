package ro.ams.mcp.model;

import lombok.Data;

/**
 * Created by Andrei Musca on 2/12/19
 */
@Data
public class CALLMessage extends AbstractMessage {
  public enum StatusCode {OK,KO}

  public CALLMessage(){
    setMessage_type(MESSAGE_TYPE.CALL);
  }

  private Long duration;
  private StatusCode status_code;
  private String status_description;


  @Override
  public boolean hasMissingFields() {
    return super.hasMissingFields()||duration==null|| status_code==null||status_description==null;
  }
}
