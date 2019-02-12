package ro.ams.mcp.service;

import java.io.InputStream;

/**
 * Created by Andrei Musca on 2/12/19
 */
public interface MessageSource {

  InputStream read(String date);
}
