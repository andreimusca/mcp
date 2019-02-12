package ro.ams.mcp.metrics;

import org.springframework.stereotype.Component;

/**
 * Created by Andrei Musca on 2/12/19
 */
@Component
public class MSISDN{

  //TODO use some kind of lib
  public String extractCountry(long source) {
    String s = String.valueOf(source);
    int max=3;
    if(s.length()<3){
      max=2;
    }
    return s.substring(0,max);
  }
}
