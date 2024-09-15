package com.sjq;

import com.sjq.declaration.Declaration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

/**
 * @author dhs
 * @version 1.0
 * @date 2024/08/11
 */
public class BaoGuanDan {

    private static final Logger log = LoggerFactory.getLogger(BaoGuanDan.class);

    /**
     * 程序的入口函数，用于处理商业发票数据。
     *
     * @param args 命令行参数数组
     */
    public static void main(String[] args) throws IOException {
        log.info("生成客户报关单资料软件--启动");
        try {
            Declaration declaration = new Declaration();
            declaration.process();
        }catch (Exception e) {
            log.error("生成客户报关单资料软件--报错", e);
        }

    }

}
