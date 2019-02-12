package ro.ams.mcp.web;

import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import ro.ams.mcp.service.MessageProcessorService;

/**
 * Created by Andrei Musca on 2/12/19
 */
@RestController
@RequestMapping("/api")
public class MessageProcessorController {

  private final MessageProcessorService messageProcessorService;

  @Autowired
  public MessageProcessorController(MessageProcessorService messageProcessorService) {
    this.messageProcessorService = messageProcessorService;
  }


  @GetMapping("/{date}")
  public ResponseEntity<String> process(@PathVariable String date) {
    messageProcessorService.process(date);
    return ResponseEntity.ok()
        .cacheControl(CacheControl.maxAge(1, TimeUnit.DAYS))
        .build();
  }


  @ExceptionHandler(IllegalArgumentException.class)
  public final ResponseEntity<?> handleBadArguments(IllegalArgumentException ex, WebRequest request) {
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.EXPECTATION_FAILED);
  }


}
