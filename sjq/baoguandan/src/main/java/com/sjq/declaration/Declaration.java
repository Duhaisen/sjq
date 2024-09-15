package com.sjq.declaration;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.data.CellRenderData;
import com.deepoove.poi.data.ParagraphRenderData;
import com.deepoove.poi.data.RowRenderData;
import com.deepoove.poi.data.Rows;
import com.deepoove.poi.data.style.ParagraphStyle;
import com.deepoove.poi.data.style.Style;
import com.sjq.readlistener.ExcelReadListener;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 报关单
 *
 * @author dhs
 * @version 1.0
 * @date 2024/08/17
 */
public class Declaration {

    private String declarationPath = "./客户报关单资料";

    private String declarationTemplate = "./报关单模板.docx";


    public void process() throws IOException {

        File parentPath = new File(declarationPath);

        File[] files = parentPath.listFiles();

        assert files != null;

        for (File file : files) {

            String name = file.getName();

            if (!name.endsWith(".xlsx") && !name.endsWith(".xls")) {
                continue;
            }

            String outFileName = declarationPath + "\\" + name.replace(".xlsx", ".docx");

            Map<String, List<RowRenderData>> datas = new HashMap<>();

            ExcelReadListener<Map<Integer, String>> excelReadListener =
                    ExcelReadListener.readExcel(file, "CI RMB (for ED)", 15);

            List<Map<Integer, String>> body = excelReadListener.getBody();


            List<Map<Integer, String>> collect = body.stream().
                    filter(map -> map.get(2) != null && map.get(3) != null && map.get(4) != null).collect(Collectors.toList());

            List<RowRenderData> rowRenderDatas = new ArrayList<>();

            for (int i = 0; i < collect.size(); i++) {
                Map<Integer, String> excelDataMap = collect.get(i);
                rowRenderDatas.add(cover2RowRenderData(i + 1, excelDataMap));
            }

            // collect.stream().map(excelDataMap -> cover2RowRenderData(excelDataMap)).collect(Collectors.toList());


            datas.put("tableData", rowRenderDatas);

            Configure config = Configure.builder().bind("tableData", new DeclarationTableRenderPolicy()).build();
            XWPFTemplate template = XWPFTemplate.compile(declarationTemplate, config).render(datas);
            template.writeToFile(outFileName);
        }
    }


    public static RowRenderData cover2RowRenderData(int index, Map<Integer, String> excelDataMap) {

        return Rows.of(createCell1(index),
                createCell2(),
                createCell3(excelDataMap),
                createCell4(excelDataMap),
                createCell5(excelDataMap),
                createCell6(),
                createCell7(),
                createCell8(),
                createCell9()
        ).create();
    }

    public static int sizePos = 3;

    public static CellRenderData createCell1(Integer index) {
        CellRenderData cellRenderData = new CellRenderData();
        ParagraphRenderData paragraphRenderData = new ParagraphRenderData().addText(String.valueOf(index));
        cellRenderData.addParagraph(paragraphRenderData);
        paragraphRenderData.setParagraphStyle(ParagraphStyle.builder()
                .withAlign(ParagraphAlignment.LEFT).withDefaultTextStyle(Style.builder()
                        .buildFontFamily("宋体")
                        .buildFontSize(9)
                        .build())
                .build());
        return cellRenderData;
    }

    public static CellRenderData createCell2() {
        CellRenderData cellRenderData = new CellRenderData();
        ParagraphRenderData paragraphRenderData = new ParagraphRenderData().addText("4011100000");
        cellRenderData.addParagraph(paragraphRenderData);
        paragraphRenderData.setParagraphStyle(ParagraphStyle.builder()
                .withAlign(ParagraphAlignment.LEFT)
                .withDefaultTextStyle(Style.builder()
                        .buildFontFamily("宋体")
                        .buildFontSize(9)
                        .build())
                .build());
        return cellRenderData;
    }


    public static CellRenderData createCell3(Map<Integer, String> excelDataMap) {
        CellRenderData cellRenderData = new CellRenderData();
        ParagraphRenderData paragraphRenderData = new ParagraphRenderData().addText(getCell1(excelDataMap));

        cellRenderData.addParagraph(paragraphRenderData);
        paragraphRenderData.setParagraphStyle(ParagraphStyle.builder()
                .withAlign(ParagraphAlignment.LEFT).withDefaultTextStyle(Style.builder()
                        .buildFontFamily("宋体")
                        .buildFontSize(9)
                        .build())
                .build());
        return cellRenderData;
    }

    public static CellRenderData createCell4(Map<Integer, String> excelDataMap) {
        CellRenderData cellRenderData = new CellRenderData();
        ParagraphRenderData paragraphRenderData = new ParagraphRenderData().addText(getCell2(excelDataMap));
        cellRenderData.addParagraph(paragraphRenderData);
        paragraphRenderData.setParagraphStyle(ParagraphStyle.builder()
                .withAlign(ParagraphAlignment.RIGHT)
                .withDefaultTextStyle(Style.builder()
                        .buildFontFamily("宋体")
                        .buildFontSize(9)
                        .build())
                .build());

        return cellRenderData;
    }

    public static CellRenderData createCell5(Map<Integer, String> excelDataMap) {
        CellRenderData cellRenderData = new CellRenderData();

        ParagraphRenderData paragraphRenderData = new ParagraphRenderData().addText(getCell3(excelDataMap));
        cellRenderData.addParagraph(paragraphRenderData);
        paragraphRenderData.setParagraphStyle(ParagraphStyle.builder()
                .withAlign(ParagraphAlignment.RIGHT).withDefaultTextStyle(Style.builder()
                        .buildFontFamily("宋体")
                        .buildFontSize(9)
                        .build())
                .build());

        return cellRenderData;
    }

    public static CellRenderData createCell6() {
        CellRenderData cellRenderData = new CellRenderData();
        ParagraphRenderData paragraphRenderData = new ParagraphRenderData().addText("中国(CHN)");
        cellRenderData.addParagraph(paragraphRenderData);
        paragraphRenderData.setParagraphStyle(ParagraphStyle.builder()
                .withAlign(ParagraphAlignment.RIGHT)
                .withDefaultTextStyle(Style.builder()
                        .buildFontFamily("宋体")
                        .buildFontSize(9)
                        .build())
                .build());
        return cellRenderData;
    }

    public static CellRenderData createCell7() {
        CellRenderData cellRenderData = new CellRenderData();
        ParagraphRenderData paragraphRenderData = new ParagraphRenderData().addText("俄罗斯(RUS)");
        cellRenderData.addParagraph(paragraphRenderData);
        paragraphRenderData.setParagraphStyle(ParagraphStyle.builder()
                .withAlign(ParagraphAlignment.RIGHT)
                .withDefaultTextStyle(Style.builder()
                        .buildFontFamily("宋体")
                        .buildFontSize(9)
                        .build())
                .build());
        return cellRenderData;
    }

    public static CellRenderData createCell8() {
        CellRenderData cellRenderData = new CellRenderData();
        ParagraphRenderData paragraphRenderData = new ParagraphRenderData().addText("(37169)菏泽");
        cellRenderData.addParagraph(paragraphRenderData);
        paragraphRenderData.setParagraphStyle(ParagraphStyle.builder()
                .withAlign(ParagraphAlignment.RIGHT)
                .withDefaultTextStyle(Style.builder()
                        .buildFontFamily("宋体")
                        .buildFontSize(9)
                        .build())
                .build());
        return cellRenderData;
    }

    public static CellRenderData createCell9() {
        CellRenderData cellRenderData = new CellRenderData();
        ParagraphRenderData paragraphRenderData = new ParagraphRenderData().addText("全免(3)");
        cellRenderData.addParagraph(paragraphRenderData);
        paragraphRenderData.setParagraphStyle(ParagraphStyle.builder()
                .withAlign(ParagraphAlignment.RIGHT)
                .withDefaultTextStyle(Style.builder()
                        .buildFontFamily("宋体")
                        .buildFontSize(9)
                        .build())
                .build());
        return cellRenderData;
    }


    public static String getCell1(Map<Integer, String> excelDataMap) {
        String template = "半钢轿车用子午线轮胎\n" +
                "1|0|轿车用|%s|%s|欧瑞路ARIVO|%s";

        String size = excelDataMap.get(sizePos);
        String liSs = excelDataMap.get(sizePos + 1);
        String pattern = excelDataMap.get(sizePos + 2);

        String[] patterns = pattern.split(" ");

        return String.format(template, size, patterns[patterns.length - 1], liSs.substring(liSs.length() - 1));
    }

    public static String getCell2(Map<Integer, String> excelDataMap) {
        String template = "%s千克\n" + "%s条\n" + "%s千克\n";

        String weight = excelDataMap.get(sizePos + 8);
        String qty = excelDataMap.get(sizePos + 3);

        return String.format(template, weight, qty, weight);
    }

    public static String getCell3(Map<Integer, String> excelDataMap) {
        String template = "%s\n" + "%s\n人民币";

        String totalValue = excelDataMap.get(sizePos + 7).trim();
        String totalNumber = totalValue.substring(1).replaceAll(",", "");

        BigDecimal total = new BigDecimal(totalNumber);

        String weight = excelDataMap.get(sizePos + 8);

        BigDecimal weightNumber = new BigDecimal(weight);

        BigDecimal unitPrice = total.divide(weightNumber, 4, RoundingMode.HALF_UP);

        return String.format(template, unitPrice, totalNumber);
    }


    public String getDeclarationPath() {
        return declarationPath;
    }

    public void setDeclarationPath(String declarationPath) {
        this.declarationPath = declarationPath;
    }

    public String getDeclarationTemplate() {
        return declarationTemplate;
    }

    public void setDeclarationTemplate(String declarationTemplate) {
        this.declarationTemplate = declarationTemplate;
    }
}
