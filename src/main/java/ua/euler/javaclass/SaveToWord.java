package ua.euler.javaclass;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class SaveToWord {
    public String headerContent, footerContent, fileContent;
    public double a, d, b;
    public Calculate wd = new Calculate();


    public void toWord() throws IOException {



        headerContent = "ДЕРЖАВНИЙ НАУКОВО-ВИПРОБУВАЛЬНИЙ ІНСТИТУТ\n" +
                "ВИПРОБУВАНЬ І СЕРТИФІКАЦІЇ ОЗБРОЄННЯ ТА ВІЙСЬКОВОЇ ТЕХНІКИ ЗБРОЙНИХ СИЛ УКРАЇНИ";
        footerContent = " 14033 м. Чернігів ";

        fileContent = "Програма розрахунку геометричних розмірів кутового відбивача";

        // создаем модель docx документа, к которой будем прикручивать наполнение (колонтитулы, текст)
        XWPFDocument docxModel = new XWPFDocument();
        CTSectPr ctSectPr = docxModel.getDocument().getBody().addNewSectPr();
        // получаем экземпляр XWPFHeaderFooterPolicy для работы с колонтитулами
        XWPFHeaderFooterPolicy headerFooterPolicy = new XWPFHeaderFooterPolicy(docxModel, ctSectPr);

        // создаем верхний колонтитул Word файла
        CTP ctpHeaderModel = createHeaderModel(headerContent);


        // устанавливаем сформированный верхний
        // колонтитул в модель документа Word
        XWPFParagraph headerParagraph = new XWPFParagraph(ctpHeaderModel, docxModel);
        headerParagraph.setAlignment(ParagraphAlignment.CENTER);
        headerParagraph.setBorderBottom(Borders.BASIC_BLACK_SQUARES);

        XWPFRun hederparagraphConfig = headerParagraph.createRun();
        hederparagraphConfig.setFontSize(14);
        hederparagraphConfig.setFontFamily("Time New Roman");
        hederparagraphConfig.setColor("06357a");

        headerFooterPolicy.createHeader(
                XWPFHeaderFooterPolicy.DEFAULT,
                new XWPFParagraph[]{headerParagraph}
        );

        // создаем нижний колонтитул docx файла
        CTP ctpFooterModel = createFooterModel(footerContent);
        // устанавливаем сформированый нижний
        // колонтитул в модель документа Word
        XWPFParagraph footerParagraph = new XWPFParagraph(ctpFooterModel, docxModel);
        footerParagraph.setAlignment(ParagraphAlignment.CENTER);
        footerParagraph.setBorderTop(Borders.BASIC_BLACK_SQUARES);

        headerFooterPolicy.createFooter(
                XWPFHeaderFooterPolicy.DEFAULT,
                new XWPFParagraph[]{footerParagraph}
        );

        // создаем обычный параграф

        XWPFParagraph bodyParagraph = docxModel.createParagraph();
        bodyParagraph.setAlignment(ParagraphAlignment.LEFT);
        XWPFRun document = bodyParagraph.createRun();
        document.setFontSize(14);
        document.setFontFamily("Time New Roman");
        // HEX цвет без решетки #
        //paragraphConfig.setColor("06357a");
        document.setText(fileContent);
        document.addBreak();
        document.addBreak();
        document.setText("Результати розрахунку:");
        document.addBreak();
        document.setText("- Кут - " + wd.f + ";");
        document.addBreak();
        document.setText("- Відстань - " + wd.wave + ";");
        document.addBreak();
        document.setText("- Ширина променю - " + wd.length + ".");
        document.addBreak();


//        for (int i=1;i<=10;i++) {
//            document.addBreak();
//            document.setText(i + ")   " + Math.sin(i) + ";");
//        }

        // сохраняем модель docx документа в файл
        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("Зберегти як...");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Документ Word ", "*.docx");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setInitialFileName("Розрахунок кутового відбивача");
        File userDirectory = new File("D:/");
        fileChooser.setInitialDirectory(userDirectory);

        File file = fileChooser.showSaveDialog((new Stage()));


        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(new File(file.getAbsolutePath()));

        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
        try {
            docxModel.write(outputStream);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        try {
            outputStream.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        System.out.println("Успешно записан в файл");
    }

    private static CTP createHeaderModel(String headerContent) {
        // создаем хедер или верхний колонтитул
        CTP ctpHeaderModel = CTP.Factory.newInstance();
        CTR ctrHeaderModel = ctpHeaderModel.addNewR();
        CTText cttHeader = ctrHeaderModel.addNewT();

        cttHeader.setStringValue(headerContent);
        return ctpHeaderModel;
    }

    private static CTP createFooterModel(String footerContent) {
        // создаем футер или нижний колонтитул
        CTP ctpFooterModel = CTP.Factory.newInstance();
        CTR ctrFooterModel = ctpFooterModel.addNewR();
        CTText cttFooter = ctrFooterModel.addNewT();

        cttFooter.setStringValue(footerContent);
        return ctpFooterModel;
    }
    public void getVeraible(double a, double d, double b) {
        this.a = a;
        this.d = d;
        this.b = b;
    }

}

