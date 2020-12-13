package tpr.antonius.weatherapp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
Garbage Collector configuration:
    -Xms256m
    -Xmx256m
    -Xlog:gc=debug:file=./logs/gc-%p-%t.log:tags,uptime,time,level:filecount=5,filesize=10m
    -XX:+HeapDumpOnOutOfMemoryError
    -XX:HeapDumpPath=./logs/dump
    -XX:+UseG1GC
    -XX:MaxGCPauseMillis=10

    -XX:+UseSerialGC
    -XX:+UseParallelGC
    -XX:+UseConcMarkSweepGC                         CMS deprecated from JDK 9
    -XX:+UnlockExperimentalVMOptions -XX:+UseZGC    ZGC starting from JDK 11
* */


@SpringBootApplication
public class WeatherApplication {
    private static Logger logger = LoggerFactory.getLogger(WeatherApplication.class);

    public static void main(String[] args) {
        logger.info("Starting app...");
        SpringApplication.run(WeatherApplication.class, args);
    }

}
