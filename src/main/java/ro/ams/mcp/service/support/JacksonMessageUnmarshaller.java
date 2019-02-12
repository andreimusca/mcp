package ro.ams.mcp.service.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ro.ams.mcp.model.AbstractMessage;
import ro.ams.mcp.model.Message;
import ro.ams.mcp.service.MessageUnmarshaller;

/**
 * Created by Andrei Musca on 2/12/19
 */
@Component
public class JacksonMessageUnmarshaller implements MessageUnmarshaller {
  private static final Logger logger = LoggerFactory.getLogger(JacksonMessageUnmarshaller.class);

  //jackson is thread safe
  private final ObjectMapper objectMapper;

  public JacksonMessageUnmarshaller() {
    this.objectMapper = new ObjectMapper();
  }

  @Override
  public Optional<Message> unmarshall(String source) {
    Message message = null;
    try {
      message = objectMapper.readValue(source, AbstractMessage.class);
    } catch (IOException e) {
      logger.error("Error unmarshalling source={} error={}", source, e.getMessage());
    }

    return Optional.ofNullable(message);
  }
}
