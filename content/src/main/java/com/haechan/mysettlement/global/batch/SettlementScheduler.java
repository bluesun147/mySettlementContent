package com.haechan.mysettlement.global.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class SettlementScheduler {
    private final JobLauncher jobLauncher;
    private final SettlementJobConfig settlementJob;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    // 매달 1일 새벽 2시 정산
    @Scheduled(cron = "0 0 2 1 * *")
    public void runJob() {

        // job parameter 설정
        Map<String, JobParameter> confMap = new HashMap<>();
//        confMap.put("time", new JobParameter(System.currentTimeMillis()));
        JobParameters jobParameters = new JobParameters();

        try {
            jobLauncher.run(settlementJob.settlement_batchBuild(jobRepository, transactionManager), jobParameters);
        } catch (JobInstanceAlreadyCompleteException | JobParametersInvalidException |
                 JobRestartException | JobExecutionAlreadyRunningException e) {
            System.out.println(e.getMessage());
        }
    }
}
