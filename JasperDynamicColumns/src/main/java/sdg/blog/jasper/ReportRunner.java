package sdg.blog.jasper;

import net.sf.jasperreports.engine.JRException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Contains a main method to run the sample project
 */
public class ReportRunner {

     public static void main(String[] args) {

         //This is rather boring data -- you would have something much more interesting in a real application.
         // Try changing this method to build the report with more columns.  All you need to change is the columnHeaders
         // and rows collections.

         List<String> columnHeaders = Arrays.asList(new String[] {"Col1", "Col2", "Col3", "Col4"});
         List<List<String>> rows = new ArrayList<List<String>>();
         List<String> row1 = Arrays.asList(new String[] {"Data1", "Data2", "Data3", "Data4"});
         List<String> row2 = Arrays.asList(new String[] {"Data5", "Data6", "Data7", "Data8"});
         List<String> row3 = Arrays.asList(new String[] {"Data9", "Data10", "Data11", "Data12"});

         rows.add(row1);
         rows.add(row2);
         rows.add(row3);

         DynamicColumnReportService service = new DynamicColumnReportService();
         try {
             service.runReport(columnHeaders, rows);
         } catch (JRException e) {
             e.printStackTrace();
         }
     }
}
