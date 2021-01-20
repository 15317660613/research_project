package com.adc.da.finicialProcess.split;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class SplitBySmallDeptTask implements Callable< List<ResultFile> > {
    private static final Logger logger =  LoggerFactory.getLogger(SplitBySmallDeptTask.class);
    public SplitBySmallDeptTask(File resFile, String basePath) {
        this.resFile = resFile;
        this.basePath = basePath;
    }

    private File resFile;
    private String basePath;

    @Override
    public List<ResultFile> call() throws Exception {
        List<ResultFile> resultFileList = new ArrayList<>();
        try {
             resultFileList = ExcelSplitUtil.doSplitBySmallDeptInMultiThreadNotLoopTask(resFile,basePath);
        }catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return resultFileList ;
        }

        return resultFileList;
    }
}
