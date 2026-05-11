package com.trabajofinal.ticketyap.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.PlatformTransactionManager;

import com.trabajofinal.ticketyap.batch.dto.EventoConImagen;
import com.trabajofinal.ticketyap.batch.dto.TicketmasterResponse;
import com.trabajofinal.ticketyap.batch.processor.EventoItemProcessor;
import com.trabajofinal.ticketyap.batch.reader.TicketmasterItemReader;
import com.trabajofinal.ticketyap.batch.tasklet.EliminarEventosAntiguosTasklet;
import com.trabajofinal.ticketyap.batch.writer.EventoItemWriter;
import com.trabajofinal.ticketyap.service.MinioService;

@Configuration
@EnableScheduling
public class BatchConfig {

    @Value("${ticketmaster.api.key}")
    private String apiKey;

    @Value("${ticketmaster.tipo-evento-id:1}")
    private int tipoEventoId;

    @Bean
    @org.springframework.batch.core.configuration.annotation.StepScope
    public TicketmasterItemReader ticketmasterItemReader() {
        return new TicketmasterItemReader(apiKey);
    }

    @Bean
    @org.springframework.batch.core.configuration.annotation.StepScope
    public EventoItemProcessor eventoItemProcessor(JdbcTemplate jdbcTemplate) {
        return new EventoItemProcessor(jdbcTemplate, tipoEventoId);
    }

    @Bean
    @org.springframework.batch.core.configuration.annotation.StepScope
    public EventoItemWriter eventoItemWriter(JdbcTemplate jdbcTemplate, MinioService minioService) {
        return new EventoItemWriter(jdbcTemplate, minioService);
    }

    @Bean
    public Step importarEventosStep(JobRepository jobRepository,
                                    PlatformTransactionManager txManager,
                                    TicketmasterItemReader reader,
                                    EventoItemProcessor processor,
                                    EventoItemWriter writer) {
        return new StepBuilder("importarEventosStep", jobRepository)
            .<TicketmasterResponse.Evento, EventoConImagen>chunk(20, txManager)
            .reader(reader)
            .processor(processor)
            .writer(writer)
            .build();
    }

    @Bean
    public Job importarEventosJob(JobRepository jobRepository, Step importarEventosStep) {
        return new JobBuilder("importarEventosJob", jobRepository)
            .start(importarEventosStep)
            .build();
    }

    @Bean
    @org.springframework.batch.core.configuration.annotation.StepScope
    public EliminarEventosAntiguosTasklet eliminarEventosAntiguosTasklet(JdbcTemplate jdbcTemplate) {
        return new EliminarEventosAntiguosTasklet(jdbcTemplate);
    }

    @Bean
    public Step eliminarEventosAntiguosStep(JobRepository jobRepository,
                                            PlatformTransactionManager txManager,
                                            EliminarEventosAntiguosTasklet tasklet) {
        return new StepBuilder("eliminarEventosAntiguosStep", jobRepository)
            .tasklet(tasklet, txManager)
            .build();
    }

    @Bean
    public Job eliminarEventosAntiguosJob(JobRepository jobRepository, Step eliminarEventosAntiguosStep) {
        return new JobBuilder("eliminarEventosAntiguosJob", jobRepository)
            .start(eliminarEventosAntiguosStep)
            .build();
    }
}
