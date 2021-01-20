package com.adc.da.budget.entity;

import com.adc.da.util.utils.StringUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.List;

@Document(indexName = "financial_prd", type = "reports")
public class ReportEO {
    public ReportEO(){}
    public ReportEO(String id, List<List<String>> tableArray){
        this.id = id;
        this.tableArray = tableArray;
    }
    @Id
    private String id;
    private List<List<String>> tableArray;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<List<String>> getTableArray() {
        return tableArray;
    }
    public void setTableArray(List<List<String>> tableArray) {
        this.tableArray = tableArray;
    }
    public String toArrayString(){
        for(int i=0;i< tableArray.size(); i++){
            for(int j=0; j< tableArray.get(0).size(); j++){
                tableArray.get(i).set(j, StringUtils.wrap(tableArray.get(i).get(j), "\""));
            }
        }
        return StringUtils.join(tableArray);
    }
}
