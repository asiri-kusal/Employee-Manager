/*
 * Copyright Camunda Services GmbH and/or licensed to Camunda Services GmbH under
 * one or more contributor license agreements. See the NOTICE file distributed
 * with this work for additional information regarding copyright ownership.
 * Licensed under the Zeebe Community License 1.0. You may not use this file
 * except in compliance with the Zeebe Community License 1.0.
 */
package lk.dialog.ms.zeebe;

import java.time.Duration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.zeebe.client.ZeebeClient;
import io.zeebe.client.ZeebeClientBuilder;
import io.zeebe.client.api.response.ActivatedJob;
import io.zeebe.client.api.worker.JobClient;
import io.zeebe.client.api.worker.JobHandler;
import io.zeebe.client.api.worker.JobWorker;

public class JobWorkerCreator {

  private static final Logger LOGGER = LogManager.getLogger(JobWorkerCreator.class);
  private static final String BROKER = "localhost:26500";
  private static final String JOBTYPE = "collect-money";

  /**
   * Initiate Zeebe client
   */
  public void runZeebeClient() {

    final ZeebeClientBuilder builder =
        ZeebeClient.newClientBuilder().brokerContactPoint(BROKER).usePlaintext();

    try (ZeebeClient client = builder.build()) {
      LOGGER.info("Opening job worker.");
      final JobWorker workerRegistration =
          client
              .newWorker()
              .jobType(JOBTYPE)
              .handler(new ExampleJobHandler())
              .timeout(Duration.ofSeconds(10))
              .open();
      // call workerRegistration.close() to close it
      workerRegistration.close();

    }
  }

  private static class ExampleJobHandler implements JobHandler {
    @Override
    public void handle(final JobClient client, final ActivatedJob job) {
      try {
        // here: business logic that is executed with every job
      } catch (Exception e) {
        client.newThrowErrorCommand(job.getKey()).errorCode("405").errorMessage(e.getMessage()).send().join();
      }
      client.newCompleteCommand(job.getKey()).send().join();
    }
  }

}