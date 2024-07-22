import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ExcelExporter {

    public static void main(String[] args) throws IOException {
        // Chemin vers le modèle Excel
        String templatePath = "path/to/template.xlsx";
        // Chemin vers le fichier Excel de sortie
        String outputPath = "path/to/output.xlsx";

        // Données à exporter
        List<List<String>> data = Arrays.asList(
                Arrays.asList("John Doe", "john.doe@example.com", "12345"),
                Arrays.asList("Jane Smith", "jane.smith@example.com", "67890")
        );

        // Charger le modèle Excel
        FileInputStream fis = new FileInputStream(templatePath);
        Workbook workbook = new XSSFWorkbook(fis);
        Sheet sheet = workbook.getSheetAt(0);

        // Commencer à écrire les données après les en-têtes
        int rowNum = sheet.getLastRowNum() + 1;

        // Écrire les données dans le fichier Excel
        for (List<String> rowData : data) {
            Row row = sheet.createRow(rowNum++);
            int colNum = 0;
            for (String field : rowData) {
                Cell cell = row.createCell(colNum++);
                cell.setCellValue(field);
            }
        }

        // Fermer le FileInputStream
        fis.close();

        // Sauvegarder le fichier Excel de sortie
        FileOutputStream fos = new FileOutputStream(outputPath);
        workbook.write(fos);
        fos.close();

        // Fermer le workbook
        workbook.close();

        System.out.println("Fichier Excel généré avec succès !");
    }
}
