package ma.marjane.pointage.service;

import jakarta.transaction.Transactional;
import ma.marjane.pointage.entity.AglMatch;
import ma.marjane.pointage.repository.AglMatchRepository;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class AglMatchService {

    @Autowired
    private AglMatchRepository aglMatchRepository;

    public void importExcelData(String filePath) throws IOException { //
        FileInputStream excelFile = new FileInputStream(new File(filePath));
        XSSFWorkbook workbook = new XSSFWorkbook(excelFile);
        XSSFSheet sheet = workbook.getSheetAt(0);
        // Charger le fichier Excel à partir du dossier resources
//        ClassPathResource resource = new ClassPathResource("DGB.xlsx");
//        InputStream excelFile = resource.getInputStream();

        // Charger le fichier Excel
//        XSSFWorkbook workbook = new XSSFWorkbook(excelFile);
//        XSSFSheet sheet = workbook.getSheetAt(0);

        Long voucherRefBase = 9999999991000L;
        Long sequenceRef = 0L;

        for (Row row : sheet) {
            if (row.getRowNum() == 0) continue; // Ignorer la première ligne (en-tête)

            AglMatch match = new AglMatch();
            match.setLastUpdate(LocalDateTime.now());

            // Vérifier si la cellule n'est pas nulle avant de la lire
            Cell sequenceNoCell = row.getCell(2);
            if (sequenceNoCell != null && sequenceNoCell.getCellType() == CellType.NUMERIC) {
                match.setSequenceNo((long) sequenceNoCell.getNumericCellValue()); // Numéro de ligne
            } else {
                match.setSequenceNo(0L); // ou une autre valeur par défaut
            }

            match.setSequenceRef(++sequenceRef);
            match.setVoucherDate(LocalDateTime.now());
            match.setRestAmount(BigDecimal.ZERO);

            // Vérifier si la cellule N° de pièce n'est pas nulle
            Cell voucherNoCell = row.getCell(1);
            if (voucherNoCell != null && voucherNoCell.getCellType() == CellType.NUMERIC) {
                match.setVoucherNo((long) voucherNoCell.getNumericCellValue()); // N° de pièce
            } else {
                match.setVoucherNo(0L); // ou une autre valeur par défaut
            }

            match.setVoucherRef(voucherRefBase + sequenceRef);

            // Vérifier si la cellule Journal (VOUCHER_TYPE) n'est pas nulle
            Cell voucherTypeCell = row.getCell(0);
            if (voucherTypeCell != null && voucherTypeCell.getCellType() == CellType.STRING) {
                match.setVoucherType(voucherTypeCell.getStringCellValue()); // Journal
            } else {
                match.setVoucherType("DEFAULT_TYPE"); // ou une autre valeur par défaut
            }

            match.setType("M");
            match.setUserId("ylachgar");

            // Vérification et traitement de la cellule VOU_REF_TYPE
            Cell vouRefTypeCell = row.getCell(3);
            if (vouRefTypeCell != null) {
                if (vouRefTypeCell.getCellType() == CellType.STRING) {
                    match.setVouRefType(vouRefTypeCell.getStringCellValue());
                } else if (vouRefTypeCell.getCellType() == CellType.NUMERIC) {
                    match.setVouRefType(String.valueOf((long) vouRefTypeCell.getNumericCellValue())); // Conversion du numérique en chaîne
                } else {
                    match.setVouRefType(""); // Valeur par défaut
                }
            } else {
                match.setVouRefType(""); // Valeur par défaut
            }

            match.setClient("EM");

            // Sauvegarder l'objet dans la base de données
            aglMatchRepository.save(match);
        }
        workbook.close();
    }

//    @Transactional
//    public void importExcelData(String filePath) throws IOException {
//        // Traitement du fichier Excel et insertion dans la base de données
//    }
}

