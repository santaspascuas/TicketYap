package com.trabajofinal.ticketyap.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/batch")
public class BatchController {

    private static final Logger log = LoggerFactory.getLogger(BatchController.class);

    private final JobLauncher jobLauncher;
    private final Job importarEventosJob;
    private final Job eliminarEventosAntiguosJob;

    public BatchController(JobLauncher jobLauncher, Job importarEventosJob, Job eliminarEventosAntiguosJob) {
        this.jobLauncher = jobLauncher;
        this.importarEventosJob = importarEventosJob;
        this.eliminarEventosAntiguosJob = eliminarEventosAntiguosJob;
    }

    @PostMapping("/importar-eventos")
    public ResponseEntity<String> importarEventos() {
        log.info("[BatchController] Disparo manual de importarEventosJob");
        try {
            JobParameters params = new JobParametersBuilder()
                .addLong("timestamp", System.currentTimeMillis())
                .toJobParameters();
            log.debug("[BatchController] Parámetros del job: {}", params);
            JobExecution execution = jobLauncher.run(importarEventosJob, params);
            log.info("[BatchController] importarEventosJob finalizado — estado={} inicio={} fin={}",
                     execution.getStatus(), execution.getStartTime(), execution.getEndTime());
            return ResponseEntity.ok("Job finalizado con estado: " + execution.getStatus());
        } catch (Exception e) {
            log.error("[BatchController] Error ejecutando importarEventosJob: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/limpiar-eventos")
    public ResponseEntity<String> limpiarEventos() {
        log.info("[BatchController] Disparo manual de eliminarEventosAntiguosJob");
        try {
            JobParameters params = new JobParametersBuilder()
                .addLong("timestamp", System.currentTimeMillis())
                .toJobParameters();
            JobExecution execution = jobLauncher.run(eliminarEventosAntiguosJob, params);
            log.info("[BatchController] eliminarEventosAntiguosJob finalizado — estado={} inicio={} fin={}",
                     execution.getStatus(), execution.getStartTime(), execution.getEndTime());
            return ResponseEntity.ok("Job finalizado con estado: " + execution.getStatus());
        } catch (Exception e) {
            log.error("[BatchController] Error ejecutando eliminarEventosAntiguosJob: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }
}
