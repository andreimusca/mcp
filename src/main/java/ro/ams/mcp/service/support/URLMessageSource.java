package ro.ams.mcp.service.support;

import java.io.InputStream;
import java.net.URL;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ro.ams.mcp.service.MessageSource;

/**
 * Created by Andrei Musca on 2/12/19
 */
@Component
public class URLMessageSource implements MessageSource {
  private final String baseUrl;

  public URLMessageSource(@Value("${mcp.base.url}") String baseUrl) {
    this.baseUrl = baseUrl;
  }

  @SneakyThrows
  @Override
  public InputStream read(String date) {
    String url=String.format(baseUrl, date);
    return new URL(url).openStream();
  }
}
