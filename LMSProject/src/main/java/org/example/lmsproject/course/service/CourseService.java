package org.example.lmsproject.course.service;

import org.example.lmsproject.Notification.Services.MailboxService;
import org.example.lmsproject.course.model.CourseEnrollRequest;
import org.example.lmsproject.course.model.Course;
import org.example.lmsproject.course.model.CourseMaterial;
import org.example.lmsproject.userPart.model.Student;
import org.example.lmsproject.userPart.model.Instructor;
import org.example.lmsproject.course.repository.CourseRepository;
import org.example.lmsproject.userPart.model.User;
import org.example.lmsproject.userPart.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final StudentService studentService;
    private final CourseMaterialService courseMaterialService;
    private final CourseEnrollRequestService courseEnrollRequestService;

    // added for Notification Logic (& added in constructor)
    private MailboxService mailboxService;
    //
    @Autowired
    public CourseService(CourseRepository courseRepository, StudentService studentService,
                         CourseMaterialService courseMaterialService,
                         CourseEnrollRequestService courseEnrollRequestService,
                         MailboxService mailboxService) {
        this.courseRepository = courseRepository;
        this.studentService = studentService;
        this.courseMaterialService = courseMaterialService;
        this.courseEnrollRequestService = courseEnrollRequestService;
        this.mailboxService = mailboxService;
    }

    public boolean courseExists(long courseId) {
        return courseRepository.existsById(courseId);
    }

    public boolean courseExists(String courseName) {
        return courseRepository.existsByTitle(courseName);
    }

    public String getAllCourses() {
         List<Course> courses = courseRepository.findAll();

        return courses.stream()
                .map(Course::toString)
                .collect(Collectors.joining(",\n\n    ", "[\n    ", "]\n"));
    }

    public String getAvailableCourses() {
        List<Course> availableCourses = courseRepository.findAll().stream().filter(Course::getAvailable).toList();
        return availableCourses.stream()
                .map(Course::toString)
                .collect(Collectors.joining(",\n\n    ", "[\n    ", "]\n"));
    }

    public String viewCourse(long id) {
        Course course = courseRepository.findById(id).orElse(null);
        if (course == null) {
            return "";
        }
        return course.toString();
    }

    public Course getCourseById(long id) {
        return courseRepository.findById(id).orElse(null);
    }

    public Course save(Course course) {
        return courseRepository.save(course);
    }

    public void createCourse(Course course, Instructor instructor) {
        course.setInstructor(instructor);  // Assign the instructor to the course
        courseRepository.save(course);  // Save the course to the database
    }

    public void updateCourse(long id, Course updatedCourse) {
        Course existingCourse = getCourseById(id);
        if (existingCourse != null) {
            if (updatedCourse.getTitle() != null) existingCourse.setTitle(updatedCourse.getTitle());
            if (updatedCourse.getDescription() != null) existingCourse.setDescription(updatedCourse.getDescription());
            if (updatedCourse.getDuration() != 0) existingCourse.setDuration(updatedCourse.getDuration());
            if (updatedCourse.getAvailable() != null) existingCourse.setAvailable(updatedCourse.getAvailable());
            if (updatedCourse.getStudents() != null) existingCourse.setStudents(updatedCourse.getStudents());
            if (updatedCourse.getInstructor() != null) existingCourse.setInstructor(updatedCourse.getInstructor());
            if (updatedCourse.getAssignments() != null) existingCourse.setAssignments(updatedCourse.getAssignments());
            if (updatedCourse.getLessons() != null) existingCourse.setLessons(updatedCourse.getLessons());
            courseRepository.save(existingCourse);
        }
    }


    public String viewEnrolledStudents(long id) {
        Course course = getCourseById(id);
        List<Long> studentIds = course.getStudents().stream()
                .map(Student::getId)
                .toList();

        return "{\"students\": " + studentIds + "}";
    }

    public ResponseEntity<String> enrollStudentInCourse(long courseId, String studentUsername) {
        // send a notification to instructor that a new enrollment request has been made
        // get instructor from course.getInstructor()

        Course course = courseRepository.findById(courseId).orElse(null);
        if (course == null)
            return ResponseEntity.badRequest().body("Course not found");
        if (!course.getAvailable())
            return ResponseEntity.badRequest().body("Course is not available");
        Student student = studentService.findStudentByUsername(studentUsername);
        if (student == null)
            return ResponseEntity.badRequest().body("Student not found");

        CourseEnrollRequest enrollRequest = new CourseEnrollRequest();
        enrollRequest.setCourse(course);
        enrollRequest.setStudent(student);
        course.getCourseEnrollRequests().add(enrollRequest);
        student.getCourseEnrollRequests().add(enrollRequest);
        courseEnrollRequestService.save(enrollRequest);
        courseRepository.save(course);
        studentService.save(student);

        // added Notification Logic //////////////////////////////////////////////////////////////////////////

        //7a get el instructor & ill send the enrollRequest to the mapper
        User instructor = course.getInstructor();
        if (instructor==null) { throw new IllegalStateException("No Instructor Affiliated With This Course"); }

        mailboxService.addNotification(instructor.getId(), enrollRequest);

        /////////////////////////////////////////////////////////////////////////////////////////////////////

        return ResponseEntity.ok("A new enrollment request has been sent to the instructor");
    }

    public ResponseEntity<String> updateEnrollmentStatus(long requestId, boolean isAccepted) {
        // send a notification to student that he has been accepted or rejected
        CourseEnrollRequest enrollRequest = courseEnrollRequestService.findById(requestId);
        if (enrollRequest == null) {
            return ResponseEntity.badRequest().body("Enrollment request not found");
        }

        enrollRequest.setAccepted(isAccepted);
        courseEnrollRequestService.save(enrollRequest);

        Course course = enrollRequest.getCourse();
        Student student = enrollRequest.getStudent();

        if (isAccepted) {
            course.addStudent(student);
            student.getCourses().add(course);
            courseRepository.save(course);
            studentService.save(student);
            return ResponseEntity.ok("Student has been accepted");
        }
        courseRepository.save(course);
        studentService.save(student);

        // added Notification Logic //////////////////////////////////////////////////////////////////////////

        // Mapper Should Handle Acceptance or Refusal
        mailboxService.addNotification(student.getId(), enrollRequest);

        /////////////////////////////////////////////////////////////////////////////////////////////////////

        return ResponseEntity.ok("Student has been rejected");
    }

    public void removeStudentFromCourse(long courseId, long studentId) {
        Course course = getCourseById(courseId);
        Student student = studentService.getStudentById(studentId);
        if (student != null) {
            course.removeStudent(student);
            student.getCourses().remove(course);
            courseRepository.save(course);
            studentService.save(student);
        }
    }

    public ResponseEntity<String> uploadMaterial(long id, MultipartFile file) {
        Course course = courseRepository.findById(id).orElse(null);
        if (course == null) {
            return ResponseEntity.status(404).body("Course not found");
        }

        ResponseEntity<String> uploadResponse = courseMaterialService.uploadMaterial(course, file);
        if (uploadResponse.getStatusCode() != HttpStatus.OK) {
            return uploadResponse;
        }

        CourseMaterial courseMaterial = courseMaterialService.getByFilename(file.getOriginalFilename());
        if (courseMaterial != null) {
            course.addMaterial(courseMaterial);
            courseRepository.save(course);
            return ResponseEntity.ok("File uploaded and associated with the course successfully");
        }

        return ResponseEntity.ok("File upload failed to associate with the course");
    }

    public ResponseEntity<byte[]> getMaterial(String filename) {
        return courseMaterialService.getMaterial(filename);
    }

    public String getEnrollments(Course course) {
        return courseEnrollRequestService.getEnrollments(course);
    }

    public ResponseEntity<String> deleteMaterial(String filename) {
        return courseMaterialService.deleteMaterial(filename);
    }

    public void deleteCourse(long id) {
        courseRepository.deleteById(id);
    }
}