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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static final Logger logger = LoggerFactory.getLogger(AglMatchService.class);

    public void importExcelData(String filePath) throws IOException {
        logger.info("Starting Excel data import from file: {}", filePath);
        FileInputStream excelFile = new FileInputStream(new File(filePath));
        XSSFWorkbook workbook = new XSSFWorkbook(excelFile);
        XSSFSheet sheet = workbook.getSheetAt(0);

        Long voucherRefBase = 9699999991000L;
        Long sequenceRef = 0L;
        Long currentVoucherRef = null;
        Long currentAccount = null;

        for (Row row : sheet) {
            if (row.getRowNum() == 0) continue; // Ignorer la première ligne (en-tête)

            // Log information about the current row
            logger.info("Processing row {}: Journal: {}, numero de piece: {}, numero de ligne: {}, compte: {}",
                    row.getRowNum(),
                    row.getCell(0),
                    row.getCell(1),
                    row.getCell(2),
                    row.getCell(3)

            );

            // Lire le compte (Account) - colonne 3
            Cell voucherNoCell = row.getCell(1);

            // Vérification si compte est nul ou vide
            if (voucherNoCell == null || voucherNoCell.getCellType() == CellType.BLANK) {
                logger.warn("Skipping row {}: voucherNo is null or blank", row.getRowNum());
                continue; // Passer à la ligne suivante
            }
            if (voucherNoCell.getCellType() != CellType.NUMERIC) {
                logger.warn("Skipping row {}: voucherNo is not numeric", row.getRowNum());
                continue; // Passer à la ligne suivante
            }



            AglMatch match = new AglMatch();
            LocalDateTime toDate = LocalDateTime.now().toLocalDate().atStartOfDay();
            match.setLastUpdate(toDate);

            match.setVoucherNo((long) voucherNoCell.getNumericCellValue());

            Cell accountCell = row.getCell(3);
            long account;
            if (accountCell != null && accountCell.getCellType() == CellType.NUMERIC) {
                account = (long) accountCell.getNumericCellValue();
            } else {
                continue;
            }

            // Si le compte a changé, réinitialiser `sequenceRef` et mettre à jour le `voucher_ref` pour le nouveau compte
            if (currentAccount == null || !currentAccount.equals(account)) {
                currentAccount = account;
                sequenceRef = 0L; // Réinitialiser le sequenceRef pour le nouveau compte
                currentVoucherRef = voucherRefBase + (++sequenceRef);
            }

            // Lire le Journal (VoucherType) - colonne 0
            Cell voucherTypeCell = row.getCell(0);
            if (voucherTypeCell != null && voucherTypeCell.getCellType() == CellType.STRING) {
                match.setVoucherType(voucherTypeCell.getStringCellValue());
            } else {
                match.setVoucherType("DEFAULT_TYPE");
            }

            // Lire le N° de Pièce (VoucherNo) - colonne 1
//            Cell voucherNoCell = row.getCell(1);
//            if (voucherNoCell != null && voucherNoCell.getCellType() == CellType.NUMERIC) {
//                match.setVoucherNo((long) voucherNoCell.getNumericCellValue());
//            } else {
//                match.setVoucherNo(0L); // Valeur par défaut si non définie
//            }

            // Lire le Numéro de ligne (SequenceNo) - colonne 2
            Cell sequenceNoCell = row.getCell(2);
            if (sequenceNoCell != null && sequenceNoCell.getCellType() == CellType.NUMERIC) {
                match.setSequenceNo((long) sequenceNoCell.getNumericCellValue());
            } else {
                match.setSequenceNo(0L); // Valeur par défaut si non définie
            }

            match.setSequenceRef(++sequenceRef);
            match.setVoucherDate(LocalDateTime.now());
            match.setRestAmount(BigDecimal.ZERO);
            match.setVoucherRef(voucherRefBase + sequenceRef);
            match.setType("M");
            match.setUserId("ylachgar");
            match.setVouRefType("vide"); // Valeur par défaut pour l'instant
            match.setClient("EM");

            // Sauvegarder l'objet dans la base de données
//            aglMatchRepository.save(match);

            // Log information about the current row
            logger.info("Processing row {}: LastUpdate: {}, SequenceNo: {}, SequenceRef: {}, VoucherDate: {}, RestAmount: {}, VoucherNo: {}, VoucherRef: {}, VoucherType: {}, Type: {}, UserId: {}, VouRefType: {}",
                    row.getRowNum(),
                    match.getLastUpdate(),
                    match.getSequenceNo(),
                    match.getSequenceRef(),
                    match.getVoucherDate(),
                    match.getRestAmount(),
                    match.getVoucherNo(),
                    match.getVoucherRef(),
                    match.getVoucherType(),
                    match.getType(),
                    match.getUserId(),
                    match.getVouRefType()
            );
        }
        workbook.close();
    }

    public void debugExcelData(String filePath) throws IOException {
        logger.info("Starting Excel data import from file: {}", filePath);
        FileInputStream excelFile = new FileInputStream(new File(filePath));
        XSSFWorkbook workbook = new XSSFWorkbook(excelFile);
        XSSFSheet sheet = workbook.getSheetAt(0);

        Long voucherRefBase = 9666999991000L;
        Long sequenceRef = 0L;
        String currentAccount = null;

        for (Row row : sheet) {
            logger.info("Processing row number: {}", row.getRowNum());

            if (row.getRowNum() == 0) continue; // Ignorer la première ligne (en-tête)

            // Lire le compte (Account) - colonne 3
            Cell accountCell = row.getCell(3);

            // Vérification si compte est nul ou vide
            if (accountCell == null || accountCell.getCellType() == CellType.BLANK) {
                logger.warn("Skipping row {}: Account is null or blank", row.getRowNum());
                continue; // Passer à la ligne suivante
            }

            // Gérer à la fois les valeurs numériques et textuelles du compte
            String account = null;
            if (accountCell.getCellType() == CellType.NUMERIC) {
                account = String.valueOf((long) accountCell.getNumericCellValue()); // Conversion du type numérique en string
            } else if (accountCell.getCellType() == CellType.STRING) {
                account = accountCell.getStringCellValue().trim();
            } else {
                logger.warn("Skipping row {}: Unsupported account cell type", row.getRowNum());
                continue; // Passer à la ligne suivante si le type de cellule n'est ni numérique ni texte
            }

            // Si le compte a changé, réinitialiser sequenceRef et mettre à jour voucherRefBase
            if (currentAccount == null || !currentAccount.equals(account)) {
                currentAccount = account; // Mettre à jour le compte actuel
                sequenceRef = 0L; // Réinitialiser le sequenceRef pour le nouveau compte
                voucherRefBase++;
            }

            AglMatch match = new AglMatch();
            LocalDateTime toDate = LocalDateTime.now().toLocalDate().atStartOfDay();
            match.setLastUpdate(toDate);

            // Lire le Journal (VoucherType) - colonne 0
            Cell voucherTypeCell = row.getCell(0);
            if (voucherTypeCell != null && voucherTypeCell.getCellType() == CellType.STRING) {
                match.setVoucherType(voucherTypeCell.getStringCellValue());
            } else {
                match.setVoucherType("DEFAULT_TYPE");
            }

            // Lire le N° de Pièce (VoucherNo) - colonne 1
            Cell voucherNoCell = row.getCell(1);
            if (voucherNoCell != null && voucherNoCell.getCellType() == CellType.NUMERIC) {
                match.setVoucherNo((long) voucherNoCell.getNumericCellValue());
            } else {
                match.setVoucherNo(0L); // Valeur par défaut si non définie
            }

            // Lire le Numéro de ligne (SequenceNo) - colonne 2
            Cell sequenceNoCell = row.getCell(2);
            if (sequenceNoCell != null && sequenceNoCell.getCellType() == CellType.NUMERIC) {
                match.setSequenceNo((long) sequenceNoCell.getNumericCellValue());
            } else {
                match.setSequenceNo(0L); // Valeur par défaut si non définie
            }

            match.setSequenceRef(++sequenceRef);
            match.setVoucherDate(toDate);
            match.setRestAmount(BigDecimal.ZERO);
            match.setVoucherRef(voucherRefBase);
            match.setType("M");
            match.setUserId("ylachgar");
            match.setVouRefType("vide"); // Valeur par défaut pour l'instant
            match.setClient("EM");

            // Sauvegarder l'objet dans la base de données
            logger.info("Before Saving the row ..... ");
            aglMatchRepository.save(match);

            // Log information about the current row
            logger.info("Processing row {}: compte: {}, LastUpdate: {}, SequenceNo: {}, SequenceRef: {}, VoucherDate: {}, RestAmount: {}, VoucherNo: {}, VoucherRef: {}, VoucherType: {}, Type: {}, UserId: {}, VouRefType: {}",
                    row.getRowNum(),
                    voucherRefBase,
                    match.getLastUpdate(),
                    match.getSequenceNo(),
                    match.getSequenceRef(),
                    match.getVoucherDate(),
                    match.getRestAmount(),
                    match.getVoucherNo(),
                    match.getVoucherRef(),
                    match.getVoucherType(),
                    match.getType(),
                    match.getUserId(),
                    match.getVouRefType()
            );
        }
        workbook.close();
    }



}


