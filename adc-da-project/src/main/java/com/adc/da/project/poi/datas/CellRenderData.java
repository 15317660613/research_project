/*
 * Copyright 2014-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.adc.da.project.poi.datas;

import com.adc.da.project.poi.datas.style.TableStyle;

/**
 * 单元格数据
 * 
 * @author DingQiang
 * @version
 */
public class CellRenderData {

    private TextRenderData renderData;

    /**
     * 单元格级别的样式：背景色、单元格文字对齐方式
     */
    private TableStyle cellStyle;

    public CellRenderData() {}

    public CellRenderData(TextRenderData renderData) {
        this.renderData = renderData;
    }

    public CellRenderData(TextRenderData renderData, TableStyle cellStyle) {
        this.renderData = renderData;
        this.cellStyle = cellStyle;
    }

    public TextRenderData getRenderData() {
        return renderData;
    }

    public void setRenderData(TextRenderData renderData) {
        this.renderData = renderData;
    }

    public TableStyle getCellStyle() {
        return cellStyle;
    }

    public void setCellStyle(TableStyle cellStyle) {
        this.cellStyle = cellStyle;
    }

}
