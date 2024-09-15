package com.sjq;

import com.sjq.bo.CommercialInvoice;
import com.sjq.invoice.DailyPackingInvoice;
import com.sjq.invoice.InvoiceReader;
import com.sjq.invoice.ShippingDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * @author dhs
 * @version 1.0
 * @date 2024/08/11
 */
public class XiangDan {

    private static final Logger log = LoggerFactory.getLogger(XiangDan.class);

    /**
     * 程序的入口函数，用于处理商业发票数据。
     *
     * @param args 命令行参数数组
     */
    public static void main(String[] args) throws IOException {
        log.info("工厂箱单发票分析软件--开始！！！！！");
        try {
            List<CommercialInvoice> datas = new InvoiceReader().process();
            log.info("工厂箱单发票分析软件--读取完成！！！！ {}条", datas.size());

            log.info("工厂箱单发票分析软件-- 按日箱单发票汇总 开始分析！！！！");
            // 每日箱单发票汇总
            new DailyPackingInvoice().process(datas);
            log.info("工厂箱单发票分析软件-- 按日箱单发票汇总 分析完成！！！");

            log.info("工厂箱单发票分析软件-- 走货总明细 开始生成报表！！！！");
            new ShippingDetails().process(datas);
            log.info("工厂箱单发票分析软件-- 走货总明细 报表生成完成！！！");

            log.info("工厂箱单发票分析软件--结束！！！！！");
        }catch (Exception e) {
            log.error("工厂箱单发票分析软件--报错@@@@@", e);
        }



    }

}
