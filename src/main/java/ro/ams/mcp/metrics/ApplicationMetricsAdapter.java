package ro.ams.mcp.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Andrei Musca on 2/12/19
 */
@Component
public class ApplicationMetricsAdapter {

  private final MeterRegistry meterRegistry;

  @Autowired
  public ApplicationMetricsAdapter(MeterRegistry meterRegistry) {
    this.meterRegistry = meterRegistry;
  }

  public Map<String, Object> getKpi() {
    return meterRegistry.getMeters().stream()
        .filter(meter -> meter.getId().getName().startsWith("mcp.kpi"))
        .collect(Collectors.toMap(m->m.getId().getName(),m->m.measure().iterator().next().getValue()));
  }

  public Map<String, Object> getMetrics() {
    return meterRegistry.getMeters().stream()
        .filter(meter -> meter.getId().getName().startsWith("mcp.json"))
        .collect(Collectors.toMap(m->m.getId().getName(),m->m.measure().iterator().next().getValue()));
  }


}
