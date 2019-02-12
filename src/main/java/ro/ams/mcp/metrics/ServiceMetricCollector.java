package ro.ams.mcp.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ro.ams.mcp.events.MessageReadEvent;
import ro.ams.mcp.events.ProcessedJsonFileEvent;
import ro.ams.mcp.events.RowReadEvent;
import ro.ams.mcp.model.CALLMessage;
import ro.ams.mcp.model.MSGMessage;
import ro.ams.mcp.model.Message;

/**
 * Created by Andrei Musca on 2/12/19
 */
@Component
public class ServiceMetricCollector {
  private static final Logger logger = LoggerFactory.getLogger(ServiceMetricCollector.class);
  private final MeterRegistry meterRegistry;
  private final MSISDN msisdn;

  private final Counter jsonFilesCounter;
  private final Counter rowsCounter;
  private final Counter callsCounter;
  private final Counter msgCounter;
  private final Counter differentOriginCountryCodes;
  private final Counter differentDestinationCountryCodes;
  private final Set<String> originCountryCodes = new HashSet<>();
  private final Set<String> destinationCountryCodes = new HashSet<>();

  @Autowired
  public ServiceMetricCollector(MSISDN msisdn, MeterRegistry meterRegistry) {
    this.msisdn = msisdn;
    this.meterRegistry = meterRegistry;
    jsonFilesCounter = meterRegistry.counter("mcp.kpi.totalJSONFiles");
    rowsCounter = meterRegistry.counter("mcp.kpi.totalRows");
    callsCounter = meterRegistry.counter("mcp.kpi.totalCalls");
    msgCounter = meterRegistry.counter("mcp.kpi.totalMsgs");
    differentOriginCountryCodes = meterRegistry.counter("mcp.kpi.differentOriginCountryCodes");
    differentDestinationCountryCodes = meterRegistry.counter("mcp.kpi.differentDestinationCountryCodes");
  }

  @EventListener
  public void handleProcessedJsonFile(ProcessedJsonFileEvent event) {
    logger.debug("Received event={}", event);
    meterRegistry.gauge("mcp.kpi.json.duration." + event.getSource(), event.getDuration());
    jsonFilesCounter.increment();
  }

  @EventListener
  public void handleRowRead(RowReadEvent event) {
    logger.debug("Received event={}", event);
    rowsCounter.increment();
  }

  @EventListener
  public void handleMessage(MessageReadEvent event) {
    logger.debug("Received event={}", event);
    Message message = event.getMessage();
    if (message instanceof CALLMessage) {
      callsCounter.increment();
    } else if (message instanceof MSGMessage) {
      msgCounter.increment();
    }

    String originCountry = msisdn.extractCountry(message.getOrigin());
    String destinationCountry = msisdn.extractCountry(message.getDestination());

    synchronized (originCountryCodes) {
      if (!originCountryCodes.contains(originCountry)) {
        originCountryCodes.add(originCountry);
        differentOriginCountryCodes.increment();
      }
    }

    synchronized (destinationCountryCodes) {
      if (!destinationCountryCodes.contains(destinationCountry)) {
        destinationCountryCodes.add(destinationCountry);
        differentDestinationCountryCodes.increment();
      }
    }

  }


}
