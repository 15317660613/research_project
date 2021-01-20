package com.adc.da.project.policy;

import com.adc.da.project.detail.ScheduleResearchDetailData;
import com.adc.da.project.poi.datas.RowRenderData;
import com.adc.da.project.poi.policy.DynamicTableRenderPolicy;
import com.adc.da.project.poi.policy.MiniTableRenderPolicy;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import java.util.List;

public class ScheduleResearchTablePolicy extends DynamicTableRenderPolicy {

    private int scheduleResearchStartLine ;

    public ScheduleResearchTablePolicy(int scheduleTradeStartLine){
        this.scheduleResearchStartLine = scheduleTradeStartLine ;
    }

    @Override
    public void render(XWPFTable table, Object data) {
        if (null == data) return;
        ScheduleResearchDetailData detailData = (ScheduleResearchDetailData) data;

        List<RowRenderData> rowRenderDataList = detailData.getScheduleResearchRowRenderDataList();
        if (null != rowRenderDataList) {
            table.removeRow(scheduleResearchStartLine);
            // 循环插入行
            int cellNum = rowRenderDataList.get(0).size() ;
            for (int i = rowRenderDataList.size() - 1 ; i >= 0 ; i --) {
                XWPFTableRow insertNewTableRow = table.insertNewTableRow(scheduleResearchStartLine);
                for (int j = 0; j < cellNum ; j++) insertNewTableRow.createCell();

                MiniTableRenderPolicy.Helper.renderRow(table, scheduleResearchStartLine, rowRenderDataList.get(i));
            }
        }

    }



}
