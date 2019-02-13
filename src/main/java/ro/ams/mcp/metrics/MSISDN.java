package ro.ams.mcp.metrics;

import org.springframework.stereotype.Component;

/**
 * Created by Andrei Musca on 2/12/19
 */
@Component
public class MSISDN {

  //TODO use some kind of lib
  public String extractCountry(long source) {
    String s = String.valueOf(source);
    return s.substring(0, Math.min(3, s.length()));
  }
}
