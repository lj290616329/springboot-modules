package com.tsingtec.tsingweb.config.error;

import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.boot.web.server.ErrorPageRegistry;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class ErrorPageConfiguration implements ErrorPageRegistrar {

  @Override
  public void registerErrorPages(ErrorPageRegistry errorPageRegistry) {
    errorPageRegistry.addErrorPages(
        new ErrorPage(HttpStatus.NOT_FOUND, "/index/error/404"),
        new ErrorPage(HttpStatus.FORBIDDEN, "/index/error/403"),
        new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/index/error/500")
    );
  }

}
