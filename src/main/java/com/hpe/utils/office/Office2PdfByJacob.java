package com.hpe.utils.office;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

import javax.naming.Name;
import java.io.File;

/**
 * 类描述： 提供统一的将office格式的文件转换成pdf格式的工具类
 * 创建人：Jeff
 * 创建时间：2018年1月27日 下午1:35:31
 * @version 1.0
 */
public class Office2PdfByJacob implements Runnable{

    private static final int wdFormatPDF = 17; 				// word保存为pdf格式宏，值为17
    private static final int xlTypePDF = 0; 				// xlTypePDF为特定值0
    private static final int ppSaveAsPDF = 32; 				// ppSaveAsPDF为特定值32
    // 输入路径
    private String inputSrc;
    // 输出路径
    private String outputSrc;
    // 文件类型
    private String type;

    public Office2PdfByJacob(String a, String fileName){
        this.inputSrc = a;
        this.outputSrc = "D:\\IDEA workspace\\upload\\pdf" + "\\" + fileName.substring(0, fileName.lastIndexOf(".")) + ".pdf";
        this.type = fileName.substring(fileName.lastIndexOf(".")+1);
    }
    /**
     * 将word(包含：doc, docx, wps格式)转换成pdf
     *
     * @param srcFilePath   源文件路径
     * @param pdfFilePath   生成PDF后存的路径
     * @return boolean (转换成功返回true, 转换失败返回true)
     */
    public static boolean word2Pdf(String srcFilePath, String pdfFilePath) {
        long startTime = System.currentTimeMillis();
        ActiveXComponent app = null;
        Dispatch doc = null;
        try {
            ComThread.InitSTA();
            app = new ActiveXComponent("Word.Application");
            app.setProperty("Visible", false);
            Dispatch docs = app.getProperty("Documents").toDispatch();
            doc = Dispatch.invoke(docs, "Open", Dispatch.Method,
                    new Object[] { srcFilePath, new Variant(false), new Variant(true), // 是否只读
                            new Variant(false), new Variant("pwd") },
                    new int[1]).toDispatch();
            // Dispatch.put(doc, "Compatibility", false); //兼容性检查,为特定值false不正确
            Dispatch.put(doc, "RemovePersonalInformation", false);
            Dispatch.call(doc, "ExportAsFixedFormat", pdfFilePath, wdFormatPDF);
            long endTime = System.currentTimeMillis();
            System.out.println("转换耗时："+(endTime - startTime) / 1000 + "秒");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("转换失败");
            return false;
        } finally {
            if (doc != null) {
                Dispatch.call(doc, "Close", false);
            }
            if (app != null) {
                app.invoke("Quit", 0);
            }
            ComThread.Release();
        }
    }

    /**
     * 将excel(包含：xls, xlsx, et格式)转换成pdf
     *
     * @param srcFilePath   源文件路径
     * @param pdfFilePath   生成PDF后存的路径
     * @return boolean (转换成功返回true, 转换失败返回true)
     */
    public static boolean excel2Pdf(String srcFilePath, String pdfFilePath) {
        long startTime = System.currentTimeMillis();
        ActiveXComponent app = null;
        Dispatch excel = null;
        try {
            ComThread.InitSTA();
            app = new ActiveXComponent("Excel.Application");
            app.setProperty("Visible", false);
            Dispatch excels = app.getProperty("Workbooks").toDispatch();
            excel = Dispatch.call(excels, "Open", srcFilePath, false, true).toDispatch();
            Dispatch.call(excel, "ExportAsFixedFormat", xlTypePDF, pdfFilePath);
            long endTime = System.currentTimeMillis();
            System.out.println("转换耗时："+(endTime - startTime) / 1000 + "秒");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("转换失败");
            return false;
        } finally {
            if (excel != null) {
                Dispatch.call(excel, "Close");
            }
            if (app != null) {
                app.invoke("Quit");
            }
            ComThread.Release();
        }
    }

    /**
     * 将ppt(包含：ppt, pptx, dps格式)转换成pdf
     *
     * @param srcFilePath   源文件路径
     * @param pdfFilePath   生成PDF后存的路径
     * @return boolean (转换成功返回true, 转换失败返回true)
     */
    public static boolean ppt2Pdf(String srcFilePath, String pdfFilePath) {
        long startTime = System.currentTimeMillis();
        ActiveXComponent app = null;
        Dispatch ppt = null;
        try {
            ComThread.InitSTA();
            app = new ActiveXComponent("PowerPoint.Application");
            Dispatch ppts = app.getProperty("Presentations").toDispatch();

            ppt = Dispatch.call(ppts, "Open", srcFilePath, false, // ReadOnly
                    true // Untitled指定文件是否有标题
            ).toDispatch();
            Dispatch.call(ppt, "SaveAs", pdfFilePath, ppSaveAsPDF);
            long endTime = System.currentTimeMillis();
            System.out.println("转换耗时："+(endTime - startTime) / 1000 + "秒");
            return true; // set flag true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("转换失败");
            return false;
        } finally {
            if (ppt != null) {
                Dispatch.call(ppt, "Close");
            }
            if (app != null) {
                app.invoke("Quit");
            }
            ComThread.Release();
        }
    }

    public static void main(String[] args) {
        String srcFilePath = "C:\\Users\\Administrator\\Desktop\\开题报告.doc";
        String pdfFilePath = "D:\\IDEA workspace\\upload\\pdf\\计科软件测试15-2 张相杰 201503061071.pdf";
        word2Pdf(srcFilePath, pdfFilePath);
        //excel2Pdf(srcFilePath, pdfFilePath);
        //ppt2Pdf(srcFilePath, pdfFilePath);
    }

    @Override
    public void run() {
        File file = new File("D:\\IDEA workspace\\upload\\pdf");
        if (!file.exists()){file.mkdirs();}
        // 将word(包含：doc, docx, wps格式)转换成pdf
        if ("doc".equals(type)|| "docx".equals(type)|| "wps".equals(type)){
            word2Pdf(inputSrc, outputSrc);
        }else if ("ppt".equals(type)|| "pptx".equals(type)|| "dps".equals(type)){
            // pt, pptx, dps格式)转换成pdf
            ppt2Pdf(inputSrc, outputSrc);
        }else if ("xls".equals(type)|| "xlsx".equals(type)|| "et".equals(type)){
            // 将excel(包含：xls, xlsx, et格式)转换成pdf
            excel2Pdf(inputSrc, outputSrc);
        }else {

        }
    }
}
