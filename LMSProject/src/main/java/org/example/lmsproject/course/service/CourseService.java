package org.example.lmsproject.course.service;

import  java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.example.lmsproject.Notification.Services.MailboxService;
import org.example.lmsproject.course.model.Course;
import org.example.lmsproject.course.model.CourseEnrollRequest;
import org.example.lmsproject.course.model.CourseMaterial;
import org.example.lmsproject.course.repository.CourseRepository;
import org.example.lmsproject.userPart.model.Instructor;
import org.example.lmsproject.userPart.model.Student;
import org.example.lmsproject.userPart.model.User;
import org.example.lmsproject.userPart.service.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final StudentService studentService;
    private final CourseMaterialService courseMaterialService;
    private final CourseEnrollRequestService courseEnrollRequestService;

    private final MailboxService mailboxService;

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

    public String viewAvailableCourse(long id) {
        Optional<Course> course = courseRepository.findById(id);

        if (course.isPresent() && !course.get().getAvailable()) {
            return "This course is not available";
        }
        return course.toString();
    }

    public Course getCourseById(long id) {
        return courseRepository.findById(id).orElse(null);
    }

    public Course save(Course course) {
        return courseRepository.save(course);
    }

    public void createCourse(Course course, Instructor instructor) { // done
        course.setInstructor(instructor);
        courseRepository.save(course);
    }

    public String updateCourse(long id, Course updatedCourse, Instructor instructor) { // done
//        Course existingCourse = getCourseById(id);
        Optional<Course> existingCourse = courseRepository.findById(id);
        if (existingCourse.isPresent()) {
            if (instructor != existingCourse.get().getInstructor()) {
                return "You are not allowed to modify this course";
            }
            if (updatedCourse.getTitle() != null) existingCourse.get().setTitle(updatedCourse.getTitle());
            if (updatedCourse.getDescription() != null) existingCourse.get().setDescription(updatedCourse.getDescription());
            if (updatedCourse.getDuration() != 0) existingCourse.get().setDuration(updatedCourse.getDuration());
            if (updatedCourse.getAvailable() != null) existingCourse.get().setAvailable(updatedCourse.getAvailable());
            if (updatedCourse.getStudents() != null) existingCourse.get().setStudents(updatedCourse.getStudents());
            if (updatedCourse.getInstructor() != null) existingCourse.get().setInstructor(updatedCourse.getInstructor());
            if (updatedCourse.getAssignments() != null) existingCourse.get().setAssignments(updatedCourse.getAssignments());
            if (updatedCourse.getLessons() != null) existingCourse.get().setLessons(updatedCourse.getLessons());
            courseRepository.save(existingCourse.orElse(updatedCourse));
            String message = "Course " + existingCourse.get().getTitle() + " has been updated";
            mailboxService.addBulkNotifications(existingCourse.get().getStudents().stream().map(Student::getId).toList(), message);
        }
        return "Course doesn't exist";
    }

    public String viewEnrolledStudents(long id) {
        Course course = getCourseById(id);
        List<Long> studentIds = course.getStudents().stream()
                .map(Student::getId)
                .toList();

        return "{\"students\": " + studentIds + "}";
    }

    public ResponseEntity<String> enrollStudentInCourse(long courseId, String studentUsername) { // done
        Course course = courseRepository.findById(courseId).orElse(null);
        if (course == null)
            return ResponseEntity.badRequest().body("Course not found");
        if (!course.getAvailable())
            return ResponseEntity.badRequest().body("Course is not available");
        Student student = studentService.findStudentByUsername(studentUsername);
        if (student == null)
            return ResponseEntity.badRequest().body("Student not found");

        CourseEnrollRequest enrollRequest = new CourseEnrollRequest();
        enrollRequest.setStatus("Pending");
        enrollRequest.setCourse(course);
        enrollRequest.setStudent(student);
        course.getCourseEnrollRequests().add(enrollRequest);
        student.getCourseEnrollRequests().add(enrollRequest);
        courseEnrollRequestService.save(enrollRequest);
        courseRepository.save(course);
        studentService.save(student);

        User instructor = course.getInstructor();
        if (instructor==null) { throw new IllegalStateException("No Instructor Affiliated With This Course"); }

        mailboxService.addNotification(instructor.getId(), enrollRequest);

        return ResponseEntity.ok("A new enrollment request has been sent to the instructor");
    }

    public ResponseEntity<String> updateEnrollmentStatus(Instructor instructor, long requestId, boolean isAccepted) { //done
        CourseEnrollRequest enrollRequest = courseEnrollRequestService.findById(requestId);
        if (enrollRequest == null) {
            return ResponseEntity.badRequest().body("Enrollment request not found");
        }

        enrollRequest.setStatus((isAccepted)? "Accepted" : "Rejected");
        courseEnrollRequestService.save(enrollRequest);

        Course course = enrollRequest.getCourse();
        Student student = enrollRequest.getStudent();
        if (instructor != course.getInstructor()) {
            return ResponseEntity.badRequest().body("You are not allowed to modify this course");
        }

        mailboxService.addNotification(student.getId(), enrollRequest);

        if (isAccepted) {
            course.addStudent(student);
            student.getCourses().add(course);
            courseRepository.save(course);
            studentService.save(student);
            return ResponseEntity.ok("Student has been accepted");
        }
        courseRepository.save(course);
        studentService.save(student);

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
            List<Long> studentIds = course.getStudents().stream()
                .map(Student::getId)
                .collect(Collectors.toList());
            mailboxService.addBulkNotifications(studentIds, courseMaterial);
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