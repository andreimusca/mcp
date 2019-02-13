package ro.ams.mcp.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.util.StringUtils;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ro.ams.mcp.events.MessageReadEvent;
import ro.ams.mcp.events.MissingFieldsEvent;
import ro.ams.mcp.events.RowErrorEvent;
import ro.ams.mcp.model.CALLMessage;
import ro.ams.mcp.model.MSGMessage;
import ro.ams.mcp.model.Message;

/**
 * Created by Andrei Musca on 2/12/19
 */
@Component
public class MessagesMetricCollector {
  private static final Logger logger = LoggerFactory.getLogger(MessagesMetricCollector.class);

  private final Map<String, JsonFileMetric> nameMetricMap = new ConcurrentHashMap<>();
  private final MeterRegistry meterRegistry;
  private final MSISDN msisdn;
  @Value("#{'${mcp.words}'.split(',')}")
  private List<String> words = Collections.emptyList();

  @Autowired
  public MessagesMetricCollector(MSISDN msisdn, MeterRegistry meterRegistry) {
    this.msisdn = msisdn;
    this.meterRegistry = meterRegistry;
  }

  @EventListener
  public void handleMissingFieldsEvent(MissingFieldsEvent event) {
    logger.debug("Received event={}", event);
    meterRegistry.counter("mcp.json." + event.getSource() + ".missing.fields").increment();

  }

  @EventListener
  public void handleRowErrorEvent(RowErrorEvent event) {
    logger.debug("Received event={}", event);
    meterRegistry.counter("mcp.json." + event.getSource() + ".total.error.rows").increment();

  }

  @EventListener
  public void handleMessageReadEvent(MessageReadEvent event) {
    logger.debug("Received event={}", event);
    Message message = event.getMessage();
    if (message instanceof CALLMessage) {
      handleCallMessage((CALLMessage) message, event.getSource());
    } else if (message instanceof MSGMessage) {
      handleMsgMessage((MSGMessage) message, event.getSource());
    }
  }


  private void handleCallMessage(CALLMessage callMessage, String source) {
    Counter okCounter = meterRegistry.counter("mcp.json." + source + ".total.calls.ok");
    Counter koCounter = meterRegistry.counter("mcp.json." + source + ".total.calls.ko");

    if (callMessage.getStatus_code().equals(CALLMessage.StatusCode.OK)) {
      okCounter.increment();
    }
    if (callMessage.getStatus_code().equals(CALLMessage.StatusCode.KO)) {
      koCounter.increment();
    }

    String origin = msisdn.extractCountry(callMessage.getOrigin());
    String destination = msisdn.extractCountry(callMessage.getDestination());
    Counter originCounter = meterRegistry.counter("mcp.json." + source + ".calls.origin.country." + origin);
    originCounter.increment();
    meterRegistry.counter("mcp.json." + source + ".calls.destination.country." + destination).increment();
    meterRegistry.counter("mcp.json." + source + ".calls.duration.country." + origin).increment(callMessage.getDuration());


    double okCount = okCounter.count();
    double koCount = koCounter.count();
    if (koCount == 0) {
      koCount = 1;
    }
    double okKoRatio = okCount / koCount;

    JsonFileMetric.CountryMetric countryMetric;
    JsonFileMetric jsonFileMetric = this.nameMetricMap.computeIfAbsent(source, s -> new JsonFileMetric());


    jsonFileMetric.setOkKoRatio(okKoRatio);
    meterRegistry.gauge("mcp.json." + source + ".ratio.calls.ok.ko", jsonFileMetric, JsonFileMetric::getOkKoRatio);
    countryMetric = jsonFileMetric.getCountryMetricMap().computeIfAbsent(origin,
        JsonFileMetric.CountryMetric::new);

    long totalDuration = countryMetric.getTotalDuration() + callMessage.getDuration();
    double average = totalDuration / originCounter.count();
    countryMetric.setDurationAverage(average);
    countryMetric.setTotalDuration(totalDuration);

    meterRegistry.gauge("mcp.json." + source + ".calls.average.duration.country." + origin,
        countryMetric, JsonFileMetric.CountryMetric::getDurationAverage);

  }


  private void handleMsgMessage(MSGMessage message, String source) {
    if (StringUtils.isEmpty(message.getMessage_content())) {
      meterRegistry.counter("mcp.json." + source + ".total.messages.blank.content").increment();
    } else {
      words.forEach(word -> {
        if (message.getMessage_content().contains(word)) {
          meterRegistry.counter("mcp.json." + source + ".ranking.word." + word).increment();
        }
      });
    }
  }
}
