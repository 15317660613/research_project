package com.adc.da.research.vo;


import lombok.Data;

@Data
public class ResearchProjectSortVO implements Comparable<ResearchProjectSortVO>{
    private int useFunds;
    private int unUseFunds;
    private  int allFunds;
    private String orgName;

    @Override
    public int compareTo(ResearchProjectSortVO o) {
        return o.getAllFunds() - this.getAllFunds();
    }
}
