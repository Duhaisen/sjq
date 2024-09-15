package com.sjq.declaration;

import com.deepoove.poi.data.RowRenderData;
import com.deepoove.poi.policy.DynamicTableRenderPolicy;
import com.deepoove.poi.policy.TableRenderPolicy;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import java.util.List;

/**
 * @author dhs
 * @version 1.0
 * @date 2024/08/17
 */
public class DeclarationTableRenderPolicy extends DynamicTableRenderPolicy {

    // 货品填充数据所在行数
    int goodsStartRow = 2;

    int lineNumber = 9;

    @Override
    public void render(XWPFTable table, Object data) throws Exception {
        if (null == data) {
            return;
        }

        List<RowRenderData> goods = (List<RowRenderData>) data;

        int startIndex = goodsStartRow;

        for (RowRenderData good : goods) {
            XWPFTableRow insertNewTableRow = table.insertNewTableRow(startIndex);
            for (int j = 0; j < lineNumber; j++) {
                insertNewTableRow.createCell();
            }
            TableRenderPolicy.Helper.renderRow(table.getRow(startIndex), good);
            startIndex++;
        }
    }
}