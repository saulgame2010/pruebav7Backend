package com.sgarciam.pruebatecnica.controllers;

import com.sgarciam.pruebatecnica.models.DocumentoJson;
import com.sgarciam.pruebatecnica.payload.response.MessageResponse;
import com.sgarciam.pruebatecnica.repository.DocumentoJsonRepository;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/document")
public class DocumentController {
    private final Logger logger = LoggerFactory.getLogger(DocumentController.class);
    @Autowired
    private DocumentoJsonRepository myDocumentRepository;

    @PostMapping("/items")
    public ResponseEntity<?> create(@Valid @RequestBody Map<String, Object> json) {
        //Extraemos la propiedad item del objeto de la petición
        String item = (String) json.get("item");
        //Verificamos que no sea nulo o que sea una cadena vacía
        if (item == null || item.equals("")) {
            //En caso de ser nulo o una cadena vacía retornamos una respuesta indicando que debe tener un valor el item
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("El Item no puede ser nulo"));
        }
        // Creamos un objeto DocumentoJson
        DocumentoJson documentoJson = new DocumentoJson(item);
        //Quitamos la propiedad item porque no nos interesa para guardarla en el objeto, ya que este ya se inicializó
        //con el valor del item
        json.remove("item");
        //Recorremos el json para obtener las claves y los valores que van a ser guardados en el objeto DocumentoJson
        for (Map.Entry<String, Object> pair : json.entrySet()) {
            if (pair.getValue() == null || pair.getValue().equals("")) {
                //En caso de que alguna propiedad no tenga un valor, regresamos una respuesta indicando que debe tenerlo
                return ResponseEntity.badRequest()
                        .body(new MessageResponse("La propiedad " + pair.getKey() + " debe tener un valor"));
            }
            //Una vez validado que las propiedades tienen valores, las guardamos en el objeto documentoJson
            documentoJson.setProperties(pair.getKey(), pair.getValue());
        }
        //Guardamos en la BD
        myDocumentRepository.save(documentoJson);
        return ResponseEntity.ok(new MessageResponse("Documento registrado exitosamente"));
    }

    @GetMapping("/items")
    public List<DocumentoJson> findDocuments(@RequestParam String query) {
        return myDocumentRepository.findByItemContainingIgnoreCase(query);
    }

    @PostMapping("/export")
    public void exportarObjetos(@RequestBody List<Map<String, Object>> data, HttpServletResponse response) throws IOException {
        // Create a new workbook and sheet
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Data");

        // El siguiente bloque crea un estilo para las celdas, en este caso se agregarán los bordes
        CellStyle style = workbook.createCellStyle();
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);

        // Add data to the sheet
        int rowIndex = 0;
        for (Map<String, Object> row : data) {
            Row spaceRow = sheet.createRow(rowIndex++);
//            int cellIndex = 0;
            for (Map.Entry<String, Object> entry : row.entrySet()) {
                Row sheetRow = sheet.createRow(rowIndex++);
                Cell cell = sheetRow.createCell(0);
                cell.setCellValue(entry.getKey());
                cell.setCellStyle(style);
                cell = sheetRow.createCell(1);
                cell.setCellValue(String.valueOf(entry.getValue()));
                cell.setCellStyle(style);
            }
        }

        // Write the workbook to a ByteArrayOutputStream
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        byte[] bytes = outputStream.toByteArray();

        // Set the response headers and write the file to the response body
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=data.xlsx");
        response.getOutputStream().write(bytes);
    }
}
