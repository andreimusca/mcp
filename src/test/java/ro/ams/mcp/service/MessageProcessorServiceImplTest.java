package ro.ams.mcp.service;

import java.io.InputStream;
import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import ro.ams.mcp.events.RowErrorEvent;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

/**
 * Created by Andrei Musca on 2/12/19
 */
@RunWith(MockitoJUnitRunner.class)
public class MessageProcessorServiceImplTest {

  @Mock
  private EventPublisher eventPublisher;
  @Mock
  private MessageSource messageSource;
  @Mock
  private MessageUnmarshaller messageUnmarshaller;

  @InjectMocks
  private MessageProcessorServiceImpl sut;

  @Test
  public void whenUnmarshallFailsRowErrorEventIsGenerated() {
    InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("MCP_20180131.json");
    when(messageSource.read("someDate")).thenReturn(is);
    when(messageUnmarshaller.unmarshall(any())).thenReturn(Optional.empty());

    sut.process("someDate");

    verify(eventPublisher,times(48)).publish(any(RowErrorEvent.class));
  }
}