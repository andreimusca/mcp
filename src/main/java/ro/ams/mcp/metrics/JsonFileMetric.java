package ro.ams.mcp.metrics;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Synchronized;

/**
 * Created by Andrei Musca on 2/12/19
 */
@Data
public class JsonFileMetric {
  private final Map<String, CountryMetric> countryMetricMap = new ConcurrentHashMap<>();
  private double okKoRatio;

  @Synchronized
  public double getOkKoRatio() {
    return okKoRatio;
  }

  @Synchronized
  public void setOkKoRatio(double okKoRatio) {
    this.okKoRatio = okKoRatio;
  }


  @Data
  @EqualsAndHashCode(of = "country")
  public static class CountryMetric {
    private final String country;
    private long totalDuration;
    private double durationAverage;

    @Synchronized
    public long getTotalDuration() {
      return totalDuration;
    }

    @Synchronized
    public void setTotalDuration(long totalDuration) {
      this.totalDuration = totalDuration;
    }

    @Synchronized
    public double getDurationAverage() {
      return durationAverage;
    }

    @Synchronized
    public void setDurationAverage(double durationAverage) {
      this.durationAverage = durationAverage;
    }
  }


}

