package sdg.blog.jasper;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A sample service to run a Jasper Report and export it to a PDF file.
 */
public class DynamicColumnReportService {

    public void runReport(List<String> columnHeaders, List<List<String>> rows) throws JRException {

        System.out.println("Loading the .jrxml");
        InputStream is = getClass().getResourceAsStream("../../../DynamicColumns.jrxml");
        JasperDesign jasperReportDesign = JRXmlLoader.load(is);

        System.out.println("Adding the dynamic columns");
        DynamicReportBuilder reportBuilder = new DynamicReportBuilder(jasperReportDesign, columnHeaders.size());
        reportBuilder.addDynamicColumns();

        System.out.println("Compiling the report");
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperReportDesign);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("REPORT_TITLE", "Sample Dynamic Columns Report");
        DynamicColumnDataSource pdfDataSource = new DynamicColumnDataSource(columnHeaders, rows);
        System.out.println("Filling the report");
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, pdfDataSource);

        System.out.println("Exporting the report to pdf");
        JasperExportManager.exportReportToPdfFile(jasperPrint, "/tmp/DynamicColumns.pdf");
    }

}
