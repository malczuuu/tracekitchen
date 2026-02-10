package io.github.malczuuu.tracekit.app.entrypoint;

import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

@RestController
@RequestMapping(path = "/entrypoint")
public class EntrypointController {

  private static final Logger log = LoggerFactory.getLogger(EntrypointController.class);

  private final SampleService sampleService;
  private final AsyncService asyncService;
  private final RestClient restClient;
  private final String baseUrl;

  public EntrypointController(
      SampleService sampleService,
      AsyncService asyncService,
      RestClient restClient,
      @Value("${tracekit.entrypoint.downstream-base-url}") String baseUrl) {
    this.sampleService = sampleService;
    this.asyncService = asyncService;
    this.restClient = restClient;
    this.baseUrl = baseUrl;
  }

  @GetMapping(produces = MediaType.TEXT_PLAIN_VALUE)
  public @Nullable String get() {
    sampleService.requires();
    sampleService.requiresNew();
    sampleService.classAware();
    asyncService.async();
    ResponseEntity<String> response =
        restClient.get().uri(baseUrl + "/downstream").retrieve().toEntity(String.class);
    log.info("Called downstream GET /downstream and returned body={}", response.getBody());
    return response.getBody();
  }

  @PostMapping(produces = MediaType.TEXT_PLAIN_VALUE)
  public @Nullable String post() {
    ResponseEntity<String> response =
        restClient.post().uri(baseUrl + "/downstream").retrieve().toEntity(String.class);
    log.info("Called downstream POST /downstream and returned body={}", response.getBody());
    return response.getBody();
  }
}
