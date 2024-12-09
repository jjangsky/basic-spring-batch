package com.example.project.job.ValidatedParam.Validator;

import ch.qos.logback.core.util.StringUtil;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.util.StringUtils;

public class FileParamValidator implements JobParametersValidator {
    @Override
    public void validate(JobParameters parameters) throws JobParametersInvalidException {
        // 파일 이름 유효성 검사
        String fileName = parameters.getString("fileName");

        // 파일 이름이 csv 타입인지 확인
        if (!StringUtils.endsWithIgnoreCase(fileName, "csv")){
            throw new JobParametersInvalidException("This is not csv file");

        }
    }
}
