package ro.ams.mcp.service;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Optional;
import lombok.Cleanup;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.ams.mcp.aop.Timed;
import ro.ams.mcp.events.MessageReadEvent;
import ro.ams.mcp.events.MissingFieldsEvent;
import ro.ams.mcp.events.RowErrorEvent;
import ro.ams.mcp.events.RowReadEvent;
import ro.ams.mcp.model.Message;

/**
 * Created by Andrei Musca on 2/12/19
 */
@Service
public class MessageProcessorServiceImpl implements MessageProcessorService {
  private static final Logger logger = LoggerFactory.getLogger(MessageProcessorServiceImpl.class);

  private MessageSource messageSource;
  private MessageUnmarshaller messageUnmarshaller;
  private EventPublisher eventPublisher;

  @Autowired
  public MessageProcessorServiceImpl(MessageSource messageSource,
      MessageUnmarshaller messageUnmarshaller, EventPublisher eventPublisher) {
    this.messageSource = messageSource;
    this.messageUnmarshaller = messageUnmarshaller;
    this.eventPublisher = eventPublisher;
  }

  @Timed
  @SneakyThrows
  @Override
  public void process(String date) {
    //TODO maybe validate input format YYYYMMDD

      InputStream inputStream = messageSource.read(date);
      @Cleanup BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

      String line;
      while ((line = bufferedReader.readLine()) != null) {
        eventPublisher.publish(new RowReadEvent(date,line));
        Optional<Message> optionalMessage = messageUnmarshaller.unmarshall(line);
        if (optionalMessage.isPresent()) {
          Message message = optionalMessage.get();
          if(message.hasMissingFields()){
            eventPublisher.publish(new MissingFieldsEvent(date,line));
          }else{
            eventPublisher.publish(new MessageReadEvent(date, message));
          }
        } else {
          eventPublisher.publish(new RowErrorEvent(date, line));
        }
      }
    }

}
