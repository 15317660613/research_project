package com.adc.da.project.policy;

import com.adc.da.project.detail.PlanTradeDetailData;
import com.adc.da.project.poi.datas.RowRenderData;
import com.adc.da.project.poi.policy.DynamicTableRenderPolicy;
import com.adc.da.project.poi.policy.MiniTableRenderPolicy;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import java.util.List;

public class PlanTradeTablePolicy extends DynamicTableRenderPolicy {

    private int planTradeStartLine ;

    public PlanTradeTablePolicy(int scheduleTradeStartLine){
        this.planTradeStartLine = scheduleTradeStartLine ;
    }

    @Override
    public void render(XWPFTable table, Object data) {
        if (null == data) return;
        PlanTradeDetailData detailData = (PlanTradeDetailData) data;

        List<RowRenderData> rowRenderDataList = detailData.getPlanTradeRowRenderDataList();
        if (null != rowRenderDataList) {
            table.removeRow(planTradeStartLine);
            int cellNum = rowRenderDataList.get(0).size() ;
            // 循环插入行
            for (int i = rowRenderDataList.size() - 1 ; i >= 0 ; i --) {
                XWPFTableRow insertNewTableRow = table.insertNewTableRow(planTradeStartLine);
                for (int j = 0; j < cellNum ; j++) insertNewTableRow.createCell();
//                // 合并单元格
//                TableTools.mergeCellsHorizonal(table, scheduleTradeStartLine, 0, 3);
                MiniTableRenderPolicy.Helper.renderRow(table, planTradeStartLine, rowRenderDataList.get(i));
            }
        }

    }



}
