package ro.ams.mcp.web;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.ams.mcp.metrics.ApplicationMetricsAdapter;

/**
 * Created by Andrei Musca on 2/12/19
 */
@RestController
public class ApplicationMetricsController {

  private final ApplicationMetricsAdapter applicationMetrics;

  @Autowired
  public ApplicationMetricsController(ApplicationMetricsAdapter applicationMetrics) {
    this.applicationMetrics = applicationMetrics;
  }


  @GetMapping("/kpi")
  public Map<String, Object> kpi() {
    return applicationMetrics.getKpi();
  }

  @GetMapping("/metrics")
  public Map<String, Object> metrics() {
    return applicationMetrics.getMetrics();
  }

}
