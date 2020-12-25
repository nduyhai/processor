package com.example.processor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

interface Processor extends Ordered {
    void process();

    void setNext(Processor processor);

}

abstract class AbstractProcessor implements Processor {
    protected Processor next;

    @Override
    public void setNext(Processor processor) {
        this.next = processor;
    }

    @Override
    public void process() {
        this.doProcess();
        if (Objects.nonNull(next)) {
            this.next.process();
        }
    }

    protected abstract void doProcess();
}

@SpringBootApplication
public class ProcessorApplication {


    public static void main(String[] args) {
        SpringApplication.run(ProcessorApplication.class, args);
    }

    @Bean
    public CommandLineRunner runner(ProcessorChain processors) {
        return args -> {
            processors.process();
        };
    }


}

@Slf4j
@Service
class LoggingProcessor extends AbstractProcessor {
    @Override
    public void doProcess() {
        log.info("{} is processing", this.getClass().getSimpleName());
    }

    @Override
    public int getOrder() {
        return 1;
    }
}


@Slf4j
@Service
class CleanupProcessor extends AbstractProcessor {
    @Override
    public void doProcess() {
        log.info("{} is processing", this.getClass().getSimpleName());
    }

    @Override
    public int getOrder() {
        return 2;
    }
}

@Slf4j
@Service
class ProcessorChain {
    @Autowired
    private List<Processor> processors;

    @PostConstruct
    public void init() {
        processors.sort(Comparator.comparingInt(Ordered::getOrder));
        for (int i = 0; i < processors.size() - 1; i++) {
            processors.get(i).setNext(processors.get(i + 1));
        }
    }

    public void process() {
        processors.get(0).process();
    }

}
