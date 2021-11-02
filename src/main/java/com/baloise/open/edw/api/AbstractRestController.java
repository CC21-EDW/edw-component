package com.baloise.open.edw.api;

import com.baloise.open.edw.domain.dto.Problem;
import com.baloise.open.edw.domain.services.CorrelationId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;
import java.lang.invoke.MethodHandles;

public class AbstractRestController {
  protected static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  protected Response tryWithExceptionHandling(CheckedSupplier<Response> action) {
    try {
      CorrelationId.set("" + System.currentTimeMillis());
      return action.apply();
    } catch (Exception e) {
      LOGGER.error(e.getMessage(), e);
      return Response.serverError().entity(getAsProblem(e)).build();
    }
  }

  private Problem getAsProblem(Exception e) {
    return Problem.builder()
        .status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())
        .cid(CorrelationId.get())
        .title(e.getClass().getSimpleName())
        .detail(e.getMessage())
        .build();
  }

  protected Response renderProblemResponse(Response.Status status, String title, String message) {
    final Problem problem = Problem.builder()
        .status(status.getStatusCode())
        .detail(message)
        .cid(CorrelationId.get())
        .title(title)
        .build();
    return Response.status(status).entity(problem).build();
  }

  @FunctionalInterface
  protected interface CheckedSupplier<R> {
    R apply() throws Exception;
  }
}
