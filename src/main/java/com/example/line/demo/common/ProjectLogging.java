package com.example.line.demo.common;

import com.google.cloud.MonitoredResource;
import com.google.cloud.logging.LogEntry;
import com.google.cloud.logging.Logging;
import com.google.cloud.logging.LoggingOptions;
import com.google.cloud.logging.Payload.StringPayload;
import com.google.cloud.logging.Severity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;

/**
 * Logging.
 *
 * @author sian
 */
public class ProjectLogging {

  /** classname. */
  private String className = "";

  private static final boolean STACKDRIVER = false;

  /**
   * init.
   *
   * @param classname classname
   */
  public ProjectLogging(final Class<?> classname) {
    this.className = classname.getName();
  }

  /**
   * main.
   *
   * @param args args
   * @throws Exception Exception
   */
  public static void main(final String[] args) throws Exception {
    new ProjectLogging(ProjectLogging.class).debug("testdebug");
    new ProjectLogging(ProjectLogging.class).info("testinfo");
    new ProjectLogging(ProjectLogging.class).warn("testwarn");
    new ProjectLogging(ProjectLogging.class).error("testerror");
  }

  /**
   * Expects a new or existing Stackdriver log name as the first argument.
   *
   * @param loggingText Logging Text
   */
  public void debug(final String loggingText) {

    // Instantiates a client
    Logger logging = LoggerFactory.getLogger(this.className);
    logging.debug(loggingText);
    // Instantiates a client

    if (STACKDRIVER) {
      Logging loggingCloud = LoggingOptions.getDefaultInstance().getService();
      LogEntry entry =
          LogEntry.newBuilder(StringPayload.of(loggingText))
              .setSeverity(Severity.DEBUG)
              .setLogName(this.className)
              .setResource(MonitoredResource.newBuilder("global").build())
              .build();

      // Writes the log entry asynchronously
      loggingCloud.write(Collections.singleton(entry));
    }
  }

  /**
   * Expects a new or existing Stackdriver log name as the first argument.
   *
   * @param loggingText Logging Text
   */
  public void info(final String loggingText) {

    // Instantiates a client
    Logger logging = LoggerFactory.getLogger(this.className);
    logging.info(loggingText);
    // Instantiates a client

    if (STACKDRIVER) {
      Logging loggingCloud = LoggingOptions.getDefaultInstance().getService();
      LogEntry entry =
          LogEntry.newBuilder(StringPayload.of(loggingText))
              .setSeverity(Severity.INFO)
              .setLogName(this.className)
              .setResource(MonitoredResource.newBuilder("global").build())
              .build();

      // Writes the log entry asynchronously
      loggingCloud.write(Collections.singleton(entry));
    }
  }

  /**
   * Expects a new or existing Stackdriver log name as the first argument.
   *
   * @param loggingText Logging Text
   */
  public void warn(final String loggingText) {

    // Instantiates a client
    Logger logging = LoggerFactory.getLogger(this.className);
    logging.warn(loggingText);
    // Instantiates a client

    if (STACKDRIVER) {
      Logging loggingCloud = LoggingOptions.getDefaultInstance().getService();
      LogEntry entry =
          LogEntry.newBuilder(StringPayload.of(loggingText))
              .setSeverity(Severity.WARNING)
              .setLogName(this.className)
              .setResource(MonitoredResource.newBuilder("global").build())
              .build();

      // Writes the log entry asynchronously
      loggingCloud.write(Collections.singleton(entry));
    }
  }

  /**
   * Expects a new or existing Stackdriver log name as the first argument.
   *
   * @param loggingText Logging Text
   */
  public void error(final String loggingText) {

    // Instantiates a client
    Logger logging = LoggerFactory.getLogger(this.className);
    logging.error(loggingText);
    // Instantiates a client

    if (STACKDRIVER) {
      Logging loggingCloud = LoggingOptions.getDefaultInstance().getService();
      LogEntry entry =
          LogEntry.newBuilder(StringPayload.of(loggingText))
              .setSeverity(Severity.ERROR)
              .setLogName(this.className)
              .setResource(MonitoredResource.newBuilder("global").build())
              .build();

      // Writes the log entry asynchronously
      loggingCloud.write(Collections.singleton(entry));
    }
  }
}
