package io.github.malczuuu.tracekitchen.downstream;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/downstream")
public class DownstreamController {

  @GetMapping(produces = MediaType.TEXT_PLAIN_VALUE)
  public String get() {
    return "This is a response to GET /downstream";
  }

  @PostMapping(produces = MediaType.TEXT_PLAIN_VALUE)
  public String post() {
    return "This is a response to POST /downstream";
  }
}
