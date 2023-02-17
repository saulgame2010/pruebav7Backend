package com.sgarciam.pruebatecnica.controllers;

import com.sgarciam.pruebatecnica.models.DocumentoJson;
import com.sgarciam.pruebatecnica.models.JsonObject;
import com.sgarciam.pruebatecnica.payload.response.MessageResponse;
import com.sgarciam.pruebatecnica.repository.DocumentoJsonRepository;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/document")
public class DocumentController {
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
    public void exportarObjetos(@RequestBody List<JsonObject> objetos, HttpServletResponse response) throws IOException {
        // Crear un archivo Excel y una hoja de cálculo
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Datos");

        // Escribir los encabezados en la primera fila
        Row encabezados = sheet.createRow(0);
        int colNum = 0;
        for (String propiedad : objetos.get(0).getProperties().keySet()) {
            Cell cell = encabezados.createCell(colNum++);
            cell.setCellValue(propiedad);
        }

        // Escribir los datos en las filas restantes
        int rowNum = 1;
        for (JsonObject objeto : objetos) {
            Row row = sheet.createRow(rowNum++);
            colNum = 0;
            for (Object valor : objeto.getProperties().values()) {
                Cell cell = row.createCell(colNum++);
                // Las siguientes sentencias If son para saber qué tipo de dato son las propiedades
                if (valor instanceof String) {
                    cell.setCellValue((String) valor);
                } else if (valor instanceof Number) {
                    cell.setCellValue(((Number) valor).doubleValue());
                } else if (valor instanceof Boolean) {
                    cell.setCellValue((Boolean) valor);
                } else {
                    cell.setCellValue(valor.toString());
                }
            }
        }

        // Establecer las cabeceras de respuesta y escribir el archivo Excel
        response.setHeader("Content-disposition", "attachment; filename=datos.xlsx");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        // Escribir el archivo Excel en la respuesta
        OutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        outputStream.close();
        workbook.close();
    }
}
