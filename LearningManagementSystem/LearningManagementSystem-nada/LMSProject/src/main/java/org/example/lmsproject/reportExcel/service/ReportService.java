package org.example.lmsproject.reportExcel.service;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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

    @Autowired
    private StudentRepository studentRepository;


    public byte[] generateStudentPerformanceReport(List<StudentPerformance> performanceList) throws Exception {
        XSSFWorkbook workbook = new XSSFWorkbook(); // Create a new workbook
        XSSFSheet sheet = workbook.createSheet("Student Performance"); // Create a sheet

        // Add header row
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Student Name");
        header.createCell(1).setCellValue("Course");
        header.createCell(2).setCellValue("Grade");
        header.createCell(3).setCellValue("Attendance");

        // Fill rows with data
        int rowNum = 1;
        for (StudentPerformance performance : performanceList) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(performance.getStudentName());
            row.createCell(1).setCellValue(performance.getCourse());
            row.createCell(2).setCellValue(performance.getGrade());
            row.createCell(3).setCellValue(performance.getAttendance());
        }

        // Write to ByteArrayOutputStream
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        return outputStream.toByteArray(); // Return the file as byte array
    }

    public List<StudentPerformance> getStudentPerformance() {
        List<Student> students = studentRepository.findAll(); // Get all students
        List<StudentPerformance> performanceList = new ArrayList<>();

        // Calculate performance for each student
        for (Student student : students) {
            // Fetch performance data from database or other source
            double grade = 85.0; // Placeholder grade
            int attendance = 95; // Placeholder attendance
            performanceList.add(new StudentPerformance(student.getUsername(), "Math", grade, attendance));
        }

        return performanceList;
    }
}