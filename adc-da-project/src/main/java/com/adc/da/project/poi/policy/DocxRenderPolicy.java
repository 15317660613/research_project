/*
 * Copyright 2014-2019 the original author or authors.
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
package com.adc.da.project.poi.policy;

import com.adc.da.project.poi.NiceXWPFDocument;
import com.adc.da.project.poi.XWPFTemplate;
import com.adc.da.project.poi.config.Configure;
import com.adc.da.project.poi.datas.DocxRenderData;
import com.adc.da.project.poi.render.RenderContext;
import com.adc.da.project.poi.template.run.RunTemplate;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * word模板List循环渲染后合并
 * </p>
 * 
 * @author DingQiang
 * @version 1.3.0
 */
public class DocxRenderPolicy extends AbstractRenderPolicy<DocxRenderData> {

    @Override
    protected void beforeRender(RenderContext context) {
        clearPlaceholder(context, false);
    }

    @Override
    public void doRender(RunTemplate runTemplate, DocxRenderData data, XWPFTemplate template) throws Exception {
        NiceXWPFDocument doc = template.getXWPFDocument();
        XWPFRun run = runTemplate.getRun();

        List<NiceXWPFDocument> docMerges = getMergedDocxs(data, template.getConfig());
        doc = doc.merge(docMerges, run);

        template.reload(doc);
    }

    private List<NiceXWPFDocument> getMergedDocxs(DocxRenderData data, Configure configure) throws IOException {
        List<NiceXWPFDocument> docs = new ArrayList<NiceXWPFDocument>();
        byte[] docx = data.getDocx();
        List<?> dataList = data.getDataList();
        if (null == dataList || dataList.isEmpty()) {
            // 待合并的文档不是模板
            docs.add(new NiceXWPFDocument(new ByteArrayInputStream(docx)));
        } else {
            for (int i = 0; i < dataList.size(); i++) {
                XWPFTemplate temp = XWPFTemplate.compile(new ByteArrayInputStream(docx), configure);
                temp.render(dataList.get(i));
                docs.add(temp.getXWPFDocument());
            }
        }
        return docs;
    }

}
