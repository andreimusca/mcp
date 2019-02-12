package ro.ams.mcp.service.support;

import org.junit.Test;
import ro.ams.mcp.model.CALLMessage;
import ro.ams.mcp.model.MSGMessage;
import ro.ams.mcp.model.Message;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created by Andrei Musca on 2/12/19
 */
public class JacksonMessageUnmarshallerTest {

  private JacksonMessageUnmarshaller sut = new JacksonMessageUnmarshaller();

  @Test
  public void unmarshallCALL() {
    String msg = "{\"message_type\": \"CALL\",\"timestamp\": 1517645700,\"origin\": 34969000001,\"destination\": "
        + "34969000101,\"duration\": 120,\"status_code\": \"OK\",\"status_description\": \"OK\"}\n";

    Message unmarshall = sut.unmarshall(msg).get();

    assertTrue(unmarshall instanceof CALLMessage);
  }

  @Test
  public void unmarshallMsg() {
    String msg = "{\"message_type\": \"MSG\",\"timestamp\": 1517559300,\"origin\": 34960000001,\"destination\": "
        + "34960000101,\"message_content\": \"1. HELLO\",\"message_status\": \"DELIVERED\"}";

    Message unmarshall = sut.unmarshall(msg).get();

    assertTrue(unmarshall instanceof MSGMessage);
  }

  @Test(expected = com.fasterxml.jackson.databind.exc.InvalidTypeIdException.class)
  public void unmarshallUnknownType() {
    String msg = "{\"message_type\": \"XXX\",\"timestamp\": 1517559300,\"origin\": 34960000001,\"destination\": "
        + "34960000101,\"message_content\": \"1. HELLO\",\"message_status\": \"DELIVERED\"}";

    Message unmarshall = sut.unmarshall(msg).get();

    fail();
  }



  @Test
  public void missingField() {
    String msg = "{\"message_type\": \"MSG\"}";

    Message unmarshall = sut.unmarshall(msg).get();

    assertTrue(unmarshall instanceof MSGMessage);
  }
}