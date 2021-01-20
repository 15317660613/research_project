package com.adc.da.research.project.poi;


import com.adc.da.research.project.poi.table.ResearchContentData;
import com.adc.da.word.datas.RowRenderData;
import com.adc.da.word.policy.DynamicTableRenderPolicy;
import com.adc.da.word.policy.MiniTableRenderPolicy;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import java.util.List;

public class ScheduleTradeTablePolicy extends DynamicTableRenderPolicy {

    private int scheduleTradeStartLine ;

    public ScheduleTradeTablePolicy(int scheduleTradeStartLine){
        this.scheduleTradeStartLine = scheduleTradeStartLine ;
    }

    @Override
    public void render(XWPFTable table, Object data) {
        if (null == data) return;
        ResearchContentData detailData = (ResearchContentData) data;

        List<RowRenderData> scheduleTradeRowRenderDataList = detailData.getScheduleTradeRowRenderDataList();

        if (null != scheduleTradeRowRenderDataList) {
            table.removeRow(scheduleTradeStartLine);

            int cellNum = scheduleTradeRowRenderDataList.get(0).size();

            for (int i = scheduleTradeRowRenderDataList.size()-1 ; i >= 0  ; i --) {
                XWPFTableRow insertNewTableRow = table.insertNewTableRow(scheduleTradeStartLine);
                for (int j = 0; j < cellNum; j++) {
                    insertNewTableRow.addNewTableCell();
                    System.out.println("插入新行!");
                }
                MiniTableRenderPolicy.Helper.renderRow(table, scheduleTradeStartLine  , scheduleTradeRowRenderDataList.get(i));
            }
        }

    }



}
