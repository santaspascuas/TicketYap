package com.trabajofinal.ticketyap.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class BatchScheduler {

    private static final Logger log = LoggerFactory.getLogger(BatchScheduler.class);

    private final JobLauncher jobLauncher;
    private final Job importarEventosJob;
    private final Job eliminarEventosAntiguosJob;

    @Value("${app.scheduler.enabled:false}")
    private boolean schedulerEnabled;

    public BatchScheduler(JobLauncher jobLauncher, Job importarEventosJob, Job eliminarEventosAntiguosJob) {
        this.jobLauncher = jobLauncher;
        this.importarEventosJob = importarEventosJob;
        this.eliminarEventosAntiguosJob = eliminarEventosAntiguosJob;
    }

    @Scheduled(cron = "0 0 3 * * *")
    public void ejecutarImportacion() {
        if (!schedulerEnabled) return;
        try {
            JobParameters params = new JobParametersBuilder()
                .addLong("timestamp", System.currentTimeMillis())
                .toJobParameters();
            jobLauncher.run(importarEventosJob, params);
            log.info("Job importarEventosJob completado");
        } catch (Exception e) {
            log.error("Error ejecutando importarEventosJob: {}", e.getMessage(), e);
        }
    }

    @Scheduled(cron = "0 0 4 * * *")
    public void ejecutarLimpieza() {
        if (!schedulerEnabled) return;
        try {
            JobParameters params = new JobParametersBuilder()
                .addLong("timestamp", System.currentTimeMillis())
                .toJobParameters();
            jobLauncher.run(eliminarEventosAntiguosJob, params);
            log.info("Job eliminarEventosAntiguosJob completado");
        } catch (Exception e) {
            log.error("Error ejecutando eliminarEventosAntiguosJob: {}", e.getMessage(), e);
        }
    }
}
