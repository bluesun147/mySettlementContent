package com.haechan.mysettlement.global.config;

import com.haechan.mysettlement.domain.contract.dto.ContractExcelDto;
import com.haechan.mysettlement.domain.revenue.dto.RevenueExcelDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

// https://january-diary.tistory.com/entry/SpringBoot-엑셀-DB에-업로드하기
// https://www.bezkoder.com/spring-boot-upload-excel-file-database/

@Slf4j
@RequiredArgsConstructor
public class ExcelHelper {

    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    static String[] HEADERs = { "ost", "distributor", "fee", "date" };
    static String SHEET = "Sheet1";


    public static boolean hadExcelFormat(MultipartFile file) {
        if (!TYPE.equals(file.getContentType())) {
            return false;
        }

        return true;
    }

    // 수익계산서 업로드
    public static List<RevenueExcelDto> excelToRevenueList(InputStream is) {
        try {
            Workbook workbook = new XSSFWorkbook(is);

            Sheet sheet = workbook.getSheet(SHEET);

            Iterator<Row> rows = sheet.iterator();


            List<RevenueExcelDto> revenueExcelDtoList = new ArrayList<RevenueExcelDto>();

            int rowNumber = 0;

            outer : while (rows.hasNext()) {

                Row currentRow = rows.next();

                // skip header
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }

                Iterator<Cell> cellsInRow = currentRow.iterator();

                // 이게 null로 초기화 되고 안쪽 while 에서 값 채워짐
                // 안쪽에서 안채워지면 null인 상태로 저장됨.
                RevenueExcelDto revenueExcelDto = new RevenueExcelDto();


                int cellIdx = 0;
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();

                    // 빈칸일 시
                    if (currentCell.getCellType() == CellType.BLANK) {
                        break;
                    }


                    switch (cellIdx) {
                        case 0:
                            revenueExcelDto.setOstId((long) currentCell.getNumericCellValue());
                            if (revenueExcelDto.getOstId() == null) {
                                break outer;
                            }
                            break;

                        case 1:
                            revenueExcelDto.setDistributorId((long) currentCell.getNumericCellValue());
                            break;

                        case 2:
                            revenueExcelDto.setFee(currentCell.getNumericCellValue());
                            break;

                        case 3:
                            Date dateCellValue = currentCell.getDateCellValue();
                            String dateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dateCellValue);
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                            LocalDateTime localDateTime = LocalDateTime.parse(dateStr, formatter);
                            revenueExcelDto.setDate(localDateTime);
                            break;

                        default:
                            break;
                    }
                    cellIdx++;
                }

                // 전부 null 일 경우 저장하면 안되고 건너뛰어야 함.
                // 가장 처음 필드인 ostId가 null이면 건너뛰게 하긴 했는데 다른 방법 찾자
                if (revenueExcelDto.getOstId() != null) {
                    revenueExcelDtoList.add(revenueExcelDto);
                }
            }
            workbook.close();

            return revenueExcelDtoList;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        }
    }

    // 계약서 업로드
    public static List<ContractExcelDto> excelToContractList(InputStream is) {
        try {
            Workbook workbook = new XSSFWorkbook(is);

            Sheet sheet = workbook.getSheet(SHEET);

            Iterator<Row> rows = sheet.iterator();


            List<ContractExcelDto> contractExcelDtoList = new ArrayList<ContractExcelDto>();

            int rowNumber = 0;

            outer : while (rows.hasNext()) {

                Row currentRow = rows.next();

                // skip header
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }

                Iterator<Cell> cellsInRow = currentRow.iterator();

                // 이게 null로 초기화 되고 안쪽 while 에서 값 채워짐
                // 안쪽에서 안채워지면 null인 상태로 저장됨.
                ContractExcelDto contractExcelDto = new ContractExcelDto();


                int cellIdx = 0;
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();

                    // 빈칸일 시
                    if (currentCell.getCellType() == CellType.BLANK) {
                        break;
                    }

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    switch (cellIdx) {

                        case 0:
                            contractExcelDto.setOstId((long) currentCell.getNumericCellValue());
                            if (contractExcelDto.getOstId() == null) {
                                break outer;
                            }
                            break;

                        case 1:
                            contractExcelDto.setProducerId((long) currentCell.getNumericCellValue());
                            break;

                        case 2:
                            contractExcelDto.setDistributorId((long) currentCell.getNumericCellValue());
                            break;

                        case 3:
                            contractExcelDto.setSingerId((long) currentCell.getNumericCellValue());
                            break;

                        case 4:
                            contractExcelDto.setProducerPercent(currentCell.getNumericCellValue());
                            break;

                        case 5:
                            contractExcelDto.setSingerPercent(currentCell.getNumericCellValue());
                            break;

                        case 6:
                            String startDateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(currentCell.getDateCellValue());
                            LocalDateTime startDate = LocalDateTime.parse(startDateStr, formatter);
                            contractExcelDto.setStartDate(startDate);
                            break;

                        case 7:
                            String endDateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(currentCell.getDateCellValue());
                            LocalDateTime endDate = LocalDateTime.parse(endDateStr, formatter);
                            contractExcelDto.setEndDate(endDate);
                            break;

                        default:
                            break;
                    }
                    cellIdx++;
                }

                // 전부 null 일 경우 저장하면 안되고 건너뛰어야 함.
                // 가장 처음 필드인 ostId가 null이면 건너뛰게 하긴 했는데 다른 방법 찾자
                if (contractExcelDto.getOstId() != null) {
                    contractExcelDtoList.add(contractExcelDto);
                }
            }
            workbook.close();

            return contractExcelDtoList;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        }
    }
}