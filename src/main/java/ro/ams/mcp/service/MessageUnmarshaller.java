package ro.ams.mcp.service;

import java.util.Optional;
import ro.ams.mcp.model.Message;

/**
 * Created by Andrei Musca on 2/12/19
 */
public interface MessageUnmarshaller {

  Optional<Message> unmarshall(String source);
}
