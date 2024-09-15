package com.sjq.readlistener;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author dhs
 * @version 1.0
 * @date 2024/08/11
 */
public class ExcelReadListener<T> extends AnalysisEventListener<T> {
    // 读取的数据
    private final List<T> body = new ArrayList<>();
    // 表头数据 位置：名称
    private final List<Map<Integer, String>> header = new ArrayList<>();

    /**
     * Returns the header as a map.Override the current method to receive header data.
     *
     * @param headMap
     * @param context
     */
    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        this.header.add(headMap);
    }


    @Override
    public void invoke(T row, AnalysisContext analysisContext) {
        this.body.add(row);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
    }

    public List<T> getBody() {
        return body;
    }

    public List<Map<Integer, String>> getHeader() {
        return header;
    }


    public static ExcelReadListener<Map<Integer, String>> readExcel(
            File file,
            Object sheet,
            Integer headRowNumber) {
        ExcelReadListener<Map<Integer, String>> readListener = new ExcelReadListener<>();

        if (sheet instanceof Integer) {
            EasyExcel.read(file, readListener).sheet((Integer) sheet).headRowNumber(headRowNumber).doRead();
        } else {
            EasyExcel.read(file, readListener).sheet((String) sheet).headRowNumber(headRowNumber).doRead();
        }

        return readListener;
    }

    public static ExcelReadListener<Map<Integer, String>> readExcel(
            String file,
            Object sheet,
            Integer headRowNumber) {
        ExcelReadListener<Map<Integer, String>> readListener = new ExcelReadListener<>();

        if (sheet instanceof Integer) {
            EasyExcel.read(file, readListener).sheet((Integer) sheet).headRowNumber(headRowNumber).doRead();
        } else {
            EasyExcel.read(file, readListener).sheet((String) sheet).headRowNumber(headRowNumber).doRead();
        }

        return readListener;
    }

}
