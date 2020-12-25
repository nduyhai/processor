package com.example.processor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.util.List;

interface Processor {
    void process();
}

@SpringBootApplication
public class ProcessorApplication {


    public static void main(String[] args) {
        SpringApplication.run(ProcessorApplication.class, args);
    }

    @Bean
    public CommandLineRunner runner(List<Processor> processors) {
        return args -> {
            // Keep ordering
            for (Processor processor : processors) {
                processor.process();
            }
        };
    }


}

@Order(1)
@Slf4j
@Service
class LoggingProcessor implements Processor {
    @Override
    public void process() {
        log.info("{} is processing", this.getClass().getSimpleName());
    }
}


@Order(2)
@Slf4j
@Service
class CleanupProcessor implements Processor {
    @Override
    public void process() {
        log.info("{} is processing", this.getClass().getSimpleName());
    }
}
