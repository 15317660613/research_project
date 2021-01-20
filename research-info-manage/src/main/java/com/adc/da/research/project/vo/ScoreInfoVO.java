package com.adc.da.research.project.vo;

import com.adc.da.research.project.entity.AnnexFileEO;
import com.adc.da.research.project.entity.JudgeInfoEO;
import com.adc.da.research.project.entity.ScoreInfoEO;
import lombok.Data;

import java.util.List;

@Data
public class ScoreInfoVO {
    private List<ScoreInfoEO> scoreInfoEOList;
    private List<AnnexFileEO> annexFileEOList;
    private JudgeInfoEO judgeInfoEO;
}
