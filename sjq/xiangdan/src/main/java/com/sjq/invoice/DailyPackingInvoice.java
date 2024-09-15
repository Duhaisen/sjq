package com.sjq.invoice;

import com.alibaba.excel.EasyExcel;

import java.util.List;

/**
 * 按日期汇总箱单发票
 *
 * @author dhs
 * @version 1.0
 * @date 2024/08/17
 */
public class DailyPackingInvoice {


    String writeFileName = "./按日箱单发票汇总/按日箱单发票汇总.xlsx";

    String writeTemplateFileName = "./按日箱单发票汇总模板.xlsx";

    public void process(List<?> datas) {
        EasyExcel.write(writeFileName).withTemplate(writeTemplateFileName).sheet().doFill(datas);
    }


}
