package ro.ams.mcp.model.validator;

import ro.ams.mcp.model.CALLMessage;
import ro.ams.mcp.model.MSGMessage;

/**
 * Created by Andrei Musca on 2/12/19
 */
public class MissingFieldValidator {

  public static boolean isValid(CALLMessage callMessage) {
    return true;
  }

  public static boolean isValid(MSGMessage callMessage) {
    return true;
  }


}
