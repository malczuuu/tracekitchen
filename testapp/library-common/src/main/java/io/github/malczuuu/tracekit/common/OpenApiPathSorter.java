package io.github.malczuuu.tracekit.common;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.customizers.OpenApiCustomizer;

/**
 * It was observed, that the order of paths in OpenAPI documentation is not deterministic. This
 * customizer sorts the paths alphabetically to ensure a consistent generated docs order.
 */
public class OpenApiPathSorter implements OpenApiCustomizer {

  private static final String DEFAULT_VERSION = "unspecified";

  private final String title;

  public OpenApiPathSorter(String title) {
    this.title = title;
  }

  @Override
  public void customise(OpenAPI openApi) {
    String version = getClass().getPackage().getImplementationVersion();
    openApi.setInfo(new Info().version(version != null ? version : DEFAULT_VERSION).title(title));

    Paths paths = new Paths();

    openApi.getPaths().keySet().stream()
        .sorted()
        .forEach(path -> paths.addPathItem(path, openApi.getPaths().get(path)));

    openApi.setPaths(paths);
  }
}
