package org.example.lmsproject.course.service;

import org.example.lmsproject.course.model.Course;
import org.example.lmsproject.Notification.Services.MailboxService;
import org.example.lmsproject.course.model.CourseEnrollRequest;
import org.example.lmsproject.course.model.CourseMaterial;
import org.example.lmsproject.course.repository.CourseRepository;
import org.example.lmsproject.userPart.model.Instructor;
import org.example.lmsproject.userPart.model.Student;
import org.example.lmsproject.userPart.service.StudentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;
    @Mock
    private StudentService studentService;
    @Mock
    private CourseMaterialService courseMaterialService;
    @Mock
    private CourseEnrollRequestService courseEnrollRequestService;
    @Mock
    private MailboxService mailboxService;

    @InjectMocks
    private CourseService courseService;

    @Test
    public void testCreateCourse() {
        Instructor instructor = new Instructor("instructor1", "password1", "instructor1@example.com");
        Course course = new Course("math", "math description", 9, true);
        Course savedCourse = new Course("math", "math description", 9, true);
        course.setCourseId(null);
        savedCourse.setCourseId(1L);

        when(courseRepository.save(course)).thenReturn(savedCourse);

        courseService.createCourse(course, instructor);

        assertNotNull(course.getInstructor());
        assertEquals(instructor, course.getInstructor());
        verify(courseRepository, times(1)).save(course);
    }

    @Test
    public void testUpdateCourse() {
        long courseId = 1L;
        Course existingCourse = new Course("math", "math description", 9, true);
        Course updatedCourse = new Course("advanced math", "math description", 20, false);
        existingCourse.setCourseId(courseId);

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(existingCourse));

        courseService.updateCourse(courseId, updatedCourse);

        assert "advanced math".equals(existingCourse.getTitle());
        assert existingCourse.getDuration() == 20;
        assert !existingCourse.getAvailable();

        verify(courseRepository, times(1)).save(existingCourse);
    }

    @Test
    public void viewCourse() {
        Long courseId = 1L;
        Course course = new Course("math", "math description", 9, true);
        course.setCourseId(courseId);
        Instructor instructor = new Instructor("instructor1", "password1", "instructor1@example.com");
        instructor.setId(2L);
        course.setInstructor(instructor);

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));

        String result = courseService.viewCourse(courseId);
        System.out.println(result);
        System.out.println("Course{" +
                "\n    courseId=" + courseId +
                ",\n    title='" + "math" + '\'' +
                ",\n    description='" + "math description" + '\'' +
                ",\n    duration=" + 9 +
                ",\n    available=" + true +
                ",\n    instructorId=" + instructor.getId() +
                "}");

        assertNotNull(result);
        assertEquals("Course{" +
                "\n    courseId=" + courseId +
                ",\n    title='" + "math" + '\'' +
                ",\n    description='" + "math description" + '\'' +
                ",\n    duration=" + 9 +
                ",\n    available=" + true +
                ",\n    instructorId=" + instructor.getId() +
                "}", result);

    }

    @Test
    public void testEnrollStudentInCourse() {
        long id = 1L;
        String studentUsername = "student1";
        Course course = new Course("math", "math description", 9, true);
        course.setCourseId(id);
        Instructor instructor = new Instructor("instructor1", "password1", "instructor1@example.com");
        course.setInstructor(instructor);

        Student student = new Student("student1", "password2", "student1@example.com");
        student.setId(id);
        CourseEnrollRequest request = new CourseEnrollRequest();
        request.setStatus("Pending");
        request.setCourse(course);
        request.setStudent(student);
        request.setCourseEnrollmentId(id);

        when(courseRepository.findById(id)).thenReturn(Optional.of(course));
        when(studentService.findStudentByUsername(studentUsername)).thenReturn(student);
        when(courseEnrollRequestService.save(any(CourseEnrollRequest.class))).thenReturn(request);

        ResponseEntity<String> response = courseService.enrollStudentInCourse(id, studentUsername);

        assertEquals("A new enrollment request has been sent to the instructor", response.getBody());

        verify(courseEnrollRequestService, times(1)).save(any(CourseEnrollRequest.class));
        verify(studentService, times(1)).findStudentByUsername(studentUsername);
        verify(mailboxService, times(1)).addNotification(eq(instructor.getId()), any(CourseEnrollRequest.class));
    }

    @Test
    public void testEnrollStudentInCourse_CourseUnavailable() {
        long id = 1L;
        String studentUsername = "student1";
        Course course = new Course("math", "math description", 9, false);
        course.setCourseId(id);
        Instructor instructor = new Instructor("instructor1", "password1", "instructor1@example.com");
        course.setInstructor(instructor);

        Student student = new Student("student1", "password2", "student1@example.com");
        student.setId(id);

        when(courseRepository.findById(id)).thenReturn(Optional.of(course));

        ResponseEntity<String> response = courseService.enrollStudentInCourse(id, studentUsername);

        assertEquals("Course is not available", response.getBody());

        verify(courseRepository, times(0)).save(course);
        verify(studentService, times(0)).findStudentByUsername(studentUsername);
        verify(mailboxService, times(0)).addNotification(eq(instructor.getId()), any(CourseEnrollRequest.class));
    }

    @Test
    public void updateEnrollmentStatus_StudentAccepted() {
        long id = 1L;
        boolean isAccepted = true;

        Course course = new Course("math", "math description", 9, true);
        course.setCourseId(id);
        Student student = new Student("student1", "password2", "student1@example.com");
        CourseEnrollRequest request = new CourseEnrollRequest();
        request.setStatus("Accepted");
        request.setCourse(course);
        request.setStudent(student);
        request.setCourseEnrollmentId(id);

        when(courseEnrollRequestService.findById(id)).thenReturn(request);

        ResponseEntity<String> response = courseService.updateEnrollmentStatus(id, isAccepted);

        assert "Student has been accepted".equals(response.getBody());
        verify(courseEnrollRequestService, times(1)).save(any(CourseEnrollRequest.class));
        verify(studentService, times(1)).save(student);
        verify(courseRepository, times(1)).save(course);

    }

    @Test
    public void updateEnrollmentStatus_StudentRejected() {
        long id = 1L;
        boolean isAccepted = false;

        Course course = new Course("math", "math description", 9, true);
        course.setCourseId(id);
        Student student = new Student("student1", "password2", "student1@example.com");
        CourseEnrollRequest request = new CourseEnrollRequest();
        request.setStatus("Accepted");
        request.setCourse(course);
        request.setStudent(student);
        request.setCourseEnrollmentId(id);

        when(courseEnrollRequestService.findById(id)).thenReturn(request);

        ResponseEntity<String> response = courseService.updateEnrollmentStatus(id, isAccepted);

        assert "Student has been rejected".equals(response.getBody());
        verify(courseEnrollRequestService, times(1)).save(any(CourseEnrollRequest.class));
        verify(studentService, times(1)).save(student);
    }


    @Test
    public void removeStudentFromCourse(){
        long id = 1L;
        Course course = new Course("math", "math description", 9, true);
        course.setCourseId(id);
        Student student = new Student("student1", "password2", "student1@example.com");
        student.setId(id);

        when(courseRepository.findById(id)).thenReturn(Optional.of(course));
        when(studentService.getStudentById(id)).thenReturn(student);

        courseService.removeStudentFromCourse(id, student.getId());

        assertFalse(course.getStudents().contains(student));
        assertFalse(student.getCourses().contains(course));
        verify(courseRepository, times(1)).save(course);
        verify(studentService, times(1)).save(student);
    }

    @Test
    public void uploadMaterial_Success()  {
        long courseId = 1L;
        MultipartFile file = mock(MultipartFile.class);
        String filename = "material.pdf";

        when(file.getOriginalFilename()).thenReturn(filename);

        Course course = new Course("Math", "Math course description", 10, true);
        course.setCourseId(courseId);

        CourseMaterial courseMaterial = new CourseMaterial();
        courseMaterial.setFilename(filename);

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(courseMaterialService.uploadMaterial(course, file)).thenReturn(ResponseEntity.ok("Upload successful"));
        when(courseMaterialService.getByFilename(filename)).thenReturn(courseMaterial);

        ResponseEntity<String> response = courseService.uploadMaterial(courseId, file);

        verify(courseRepository, times(1)).save(course);  // Verify course was saved
        assertEquals("File uploaded and associated with the course successfully", response.getBody());
    }

    @Test
    public void uploadMaterial_CourseNotFound() {
        long courseId = 1L;
        MultipartFile file = mock(MultipartFile.class);

        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        ResponseEntity<String> response = courseService.uploadMaterial(courseId, file);

        assertEquals("Course not found", response.getBody());
    }

    @Test
    public void uploadMaterial_FailureToAssociate() {
        long courseId = 1L;
        MultipartFile file = mock(MultipartFile.class);
        String filename = "material.pdf";

        when(file.getOriginalFilename()).thenReturn(filename);

        Course course = new Course("Math", "Math course description", 10, true);
        course.setCourseId(courseId);

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(courseMaterialService.uploadMaterial(course, file)).thenReturn(ResponseEntity.ok("Upload successful"));
        when(courseMaterialService.getByFilename(filename)).thenReturn(null);  // Return null to simulate failure

        ResponseEntity<String> response = courseService.uploadMaterial(courseId, file);

        verify(courseRepository, times(0)).save(course);  // Verify course was saved
        assertEquals("File upload failed to associate with the course", response.getBody());
    }

    @Test
    public void testDeleteCourse() {
        long courseId = 1L;
        Course course = new Course("math", "math description", 9, true);
        course.setCourseId(courseId);

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));

        courseService.deleteCourse(courseId);

        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        assertFalse(courseRepository.findById(courseId).isPresent());
        verify(courseRepository, times(1)).deleteById(courseId);
    }
}
