package ro.ams.mcp.model;

/**
 * Created by Andrei Musca on 2/12/19
 */

public interface Message {
  enum MESSAGE_TYPE{CALL,MSG}

   MESSAGE_TYPE getMessage_type();
   Long getTimestamp();
   Long getOrigin();
   Long getDestination();

   boolean hasMissingFields();
}
