package ma.marjane.pointage.controller;

import ma.marjane.pointage.service.AglMatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class AglMatchController {

    @Autowired
    private AglMatchService aglMatchService;

    @PostMapping("/import")
    public ResponseEntity<String> importData(@RequestParam("filePath") String filePath) {
        try {
            aglMatchService.importExcelData(filePath);
            return ResponseEntity.ok("Données importées avec succès.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de l'import : " + e.getMessage());
        }
    }
}

