package org.example.lmsproject.reportExcel.service;

import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.lmsproject.quiz.Repositories.Quiz.QuizRepository;
import org.example.lmsproject.reportExcel.model.StudentPerformance;
import org.example.lmsproject.userPart.model.Student;
import org.example.lmsproject.userPart.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;


@Service
public class ReportService {

    public byte[] generateStudentPerformanceReport(List<StudentPerformance> performanceList) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();  // Create a new workbook
        XSSFSheet sheet = workbook.createSheet("Student Performance");  // Create a sheet

        // Add header row
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Student Name");
        header.createCell(1).setCellValue("Quiz Grade");
        header.createCell(2).setCellValue("Attendance (%)");
        header.createCell(3).setCellValue("Assignment Score");

        // Fill rows with data
        int rowNum = 1;
        for (StudentPerformance performance : performanceList) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(performance.getUsername());
            row.createCell(1).setCellValue(performance.getQuizGrade());
            row.createCell(2).setCellValue(performance.getAttendancePercentage());
            row.createCell(3).setCellValue(performance.getAssignmentScore());
        }

        // Write to ByteArrayOutputStream
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        return outputStream.toByteArray();  // Return the file as byte array
    }


}