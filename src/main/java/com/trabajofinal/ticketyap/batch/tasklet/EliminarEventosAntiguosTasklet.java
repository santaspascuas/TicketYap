package com.trabajofinal.ticketyap.batch.tasklet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.jdbc.core.JdbcTemplate;

public class EliminarEventosAntiguosTasklet implements Tasklet {

    private static final Logger log = LoggerFactory.getLogger(EliminarEventosAntiguosTasklet.class);

    private final JdbcTemplate jdbc;

    public EliminarEventosAntiguosTasklet(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
        int eliminados = jdbc.update("DELETE FROM eventos WHERE fecha_evento < CURRENT_DATE");
        log.info("{} eventos pasados eliminados", eliminados);
        return RepeatStatus.FINISHED;
    }
}
