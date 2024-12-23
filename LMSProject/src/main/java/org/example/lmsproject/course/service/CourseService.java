package org.example.lmsproject.course.service;

import  java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.example.lmsproject.Notification.Services.MailboxService;
import org.example.lmsproject.Notification.TextMappers.NotificationAndEmailMapper;
import org.example.lmsproject.course.model.Course;
import org.example.lmsproject.course.model.CourseEnrollRequest;
import org.example.lmsproject.course.model.CourseEnrollRequestNotification;
import org.example.lmsproject.course.model.CourseMaterial;
import org.example.lmsproject.course.model.CourseMaterialNotification;
import org.example.lmsproject.course.repository.CourseRepository;
import org.example.lmsproject.userPart.model.Instructor;
import org.example.lmsproject.userPart.model.Student;
import org.example.lmsproject.userPart.model.User;
import org.example.lmsproject.userPart.service.InstructorService;
import org.example.lmsproject.userPart.service.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import org.springframework.web.server.ResponseStatusException;

@Service
@AllArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final StudentService studentService;
    private final CourseMaterialService courseMaterialService;
    private final CourseEnrollRequestService courseEnrollRequestService;

    private final MailboxService mailboxService;
    private final InstructorService instructorService;

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

    public String getEnrolledCourses(String studentUsername) {
        return studentService.getEnrolledCourses(studentUsername);
    }

    public String viewCourse(long id) {
        Course course = courseRepository.findById(id).orElse(null);
        if (course == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found");
        }
        return course.toString();
    }

    public String viewAvailableCourse(long id) {
        Optional<Course> course = courseRepository.findById(id);

        if (course.isPresent() && !course.get().getAvailable()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This course is not available");
        }
        return course.toString();
    }

    public Course getCourseById(long id) {
        return courseRepository.findById(id).orElse(null);
    }

    public Course save(Course course) {
        return courseRepository.save(course);
    }

    public void createCourse(Course course, String instructorUsername) {
        Instructor instructor = instructorService.findByUsername(instructorUsername);
        course.setInstructor(instructor);
        courseRepository.save(course);
    }

    public String updateCourse(long id, Course updatedCourse, String instructorUsername) {
        Optional<Course> existingCourse = courseRepository.findById(id);
        Instructor instructor = instructorService.findByUsername(instructorUsername);
        if (existingCourse.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found");
        }
        if (instructor != existingCourse.get().getInstructor()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You are not allowed to modify this course");
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
        NotificationAndEmailMapper courseNotification = new CourseNotification(existingCourse.get());
        mailboxService.addBulkNotifications(existingCourse.get().getStudents().stream().map(Student::getId).toList(), courseNotification);

        return existingCourse.get().toString();
    }

    public String viewEnrolledStudents(long id) {
        Course course = getCourseById(id);
        List<Long> studentIds = course.getStudents().stream()
                .map(Student::getId)
                .toList();

        return "{\"students\": " + studentIds + "}";
    }

    public String enrollStudentInCourse(long courseId, String studentUsername) {
        Course course = courseRepository.findById(courseId).orElse(null);
        if (course == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found");
        if (!course.getAvailable())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not available");
        Student student = studentService.findStudentByUsername(studentUsername);
        if (student == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found");

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
        if (instructor==null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Instructor not found");


        NotificationAndEmailMapper courseEnrollNotification = new CourseEnrollRequestNotification(enrollRequest);
        mailboxService.addNotification(instructor.getId(), courseEnrollNotification);
        return "A new enrollment request has been sent to the instructor";
    }

    public String updateEnrollmentStatus(String instructorUsername, long requestId, boolean isAccepted) {
        CourseEnrollRequest enrollRequest = courseEnrollRequestService.findById(requestId);
        if (enrollRequest == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Enrollment request not found");
        }

        enrollRequest.setStatus((isAccepted)? "Accepted" : "Rejected");
        courseEnrollRequestService.save(enrollRequest);

        Course course = enrollRequest.getCourse();
        Student student = enrollRequest.getStudent();
        Instructor instructor = instructorService.findByUsername(instructorUsername);
        if (instructor != course.getInstructor()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You are not allowed to modify this course");
        }
        NotificationAndEmailMapper courseEnrollNotification = new CourseEnrollRequestNotification(enrollRequest);
        mailboxService.addNotification(student.getId(), courseEnrollNotification);

        if (isAccepted) {
            course.addStudent(student);
            student.getCourses().add(course);
            courseRepository.save(course);
            studentService.save(student);
            return "Student has been accepted";
        }
        courseRepository.save(course);
        studentService.save(student);

        return "Student has been rejected";
    }

    public void removeStudentFromCourse(long courseId, String instructorUsername, long studentId) {
        Course course = getCourseById(courseId);
        Instructor instructor = instructorService.findByUsername(instructorUsername);
        if (course.getInstructor() != instructor)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You are not allowed to modify this course");
        Student student = studentService.getStudentById(studentId);
        if (student == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found");
        }
        course.removeStudent(student);
        student.getCourses().remove(course);
        courseRepository.save(course);
        studentService.save(student);
    }

    public String uploadMaterial(long id, String instructorUsername, MultipartFile file) {
        Optional<Course> course = courseRepository.findById(id);
        if (course.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found");

        Instructor instructor = instructorService.findByUsername(instructorUsername);
        if (course.get().getInstructor() != instructor) {
            System.out.println("test");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You are not allowed to modify this course");
        }
        courseMaterialService.uploadMaterial(course.get(), file);

        CourseMaterial courseMaterial = courseMaterialService.getByFilename(file.getOriginalFilename());
        if (courseMaterial != null) {
            course.get().addMaterial(courseMaterial);
            courseRepository.save(course.get());
            List<Long> studentIds = course.get().getStudents().stream()
                .map(Student::getId)
                .collect(Collectors.toList());
            NotificationAndEmailMapper courseMaterialNotification = new CourseMaterialNotification(courseMaterial);
            mailboxService.addBulkNotifications(studentIds, courseMaterialNotification);
            return "File uploaded and associated with the course successfully";
        }

        return "File upload failed to associate with the course";
    }

    public Optional<byte[]> getMaterial(long courseId, String studentUsername, String filename) {
        // Check if the course exists
        Optional<Course> course = courseRepository.findById(courseId);
        if (course.isEmpty()) {
            return Optional.empty();
        }

        Student student = studentService.findStudentByUsername(studentUsername);
        if (!course.get().getStudents().contains(student)) {
            return Optional.empty();
        }

        return courseMaterialService.getMaterial(filename);
    }


    public String getEnrollments(String instructorUsername, Course course) {
        Instructor instructor = instructorService.findByUsername(instructorUsername);
        if (course.getInstructor() != instructor)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You are not allowed to modify this course");
        return courseEnrollRequestService.getEnrollments(course);
    }

    public String deleteMaterial(String instructorUsername, long courseId, String filename) {
        Optional<Course> course = courseRepository.findById(courseId);
        Instructor instructor = instructorService.findByUsername(instructorUsername);
        if (course.isPresent() && course.get().getInstructor() != instructor)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You are not allowed to modify this course");
        return courseMaterialService.deleteMaterial(filename);
    }

    public String deleteCourse(String instructorUsername, long id) {
        Course course = courseRepository.findById(id).orElse(null);
        Instructor instructor = instructorService.findByUsername(instructorUsername);
        if ( course.getInstructor() != instructor)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You are not allowed to modify this course");
        for (Student student : course.getStudents()) {
            student.getCourses().remove(course);
        }
        System.out.println(course.getCourseId());
        studentService.saveAll(course.getStudents());
        course.getStudents().clear();
        courseRepository.save(course);
        courseRepository.deleteById(id);
        courseRepository.delete(course);
        return "Course deleted successfully";
    }
}