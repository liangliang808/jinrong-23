package com.softeem;

import com.softeem.config.CounterConfig;
import com.softeem.util.DbUtil;
import com.thirdpart.uuid.SofteemUuid;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication

public class CounterApplication {

    @Autowired
    private DbUtil dbUtil;

    @Autowired
    private CounterConfig counterConfig;

    @PostConstruct
    private void init(){
        SofteemUuid.getInstance().init(counterConfig.getDataCenterId(),
                counterConfig.getWorkerId());
    }

    public static void main(String[] args) {
        SpringApplication.run(CounterApplication.class,args);
    }
}
