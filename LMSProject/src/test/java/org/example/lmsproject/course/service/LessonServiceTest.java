 package org.example.lmsproject.course.service;

 import org.example.lmsproject.course.model.Course;
 import org.example.lmsproject.course.model.Lesson;
 import org.example.lmsproject.course.repository.CourseRepository;
 import org.example.lmsproject.course.repository.LessonRepository;
 import org.example.lmsproject.course.service.LessonService;
 import org.example.lmsproject.Notification.Services.MailboxService;
 import org.example.lmsproject.userPart.model.Student;
 import org.junit.jupiter.api.Test;
 import org.junit.jupiter.api.extension.ExtendWith;
 import org.mockito.InjectMocks;
 import org.mockito.Mock;
 import org.mockito.junit.jupiter.MockitoExtension;

 import java.time.LocalDateTime;
 import java.util.Collections;
 import java.util.List;
 import java.util.Optional;

 import static org.junit.jupiter.api.Assertions.*;
 import static org.mockito.Mockito.*;

 @ExtendWith(MockitoExtension.class)
 public class LessonServiceTest {
     @Mock
     private LessonRepository lessonrepo;
     @Mock
     private CourseRepository courserepo;
     @Mock
     private MailboxService mailboxService;

     @InjectMocks
     private LessonService lessonservice;

//     @Test
//     public void test_generateotp() {
//         Long lessonid = 1L;
//
//         Course mockcourse = new Course("Course1", "Description1", 30, true);
//         Student mockStudent = new Student();
//         mockStudent.setId(1L);
//         mockcourse.setStudents(Collections.singletonList(mockStudent));
//         Lesson mocklesson = new Lesson();
//         mocklesson.setId(lessonid);
//         mocklesson.setTitle("Lesson1");
//         mocklesson.setCourse(mockcourse);
//
//         when(lessonrepo.findById(lessonid)).thenReturn(Optional.of(mocklesson));
//
//         String otp = lessonservice.generateOtp(lessonid);
//
//         assertNotNull(otp);
//         assertEquals(6, otp.length()); // Ensure OTP length is 6
//         assertTrue(otp.matches("\\d{6}")); // Check OTP is numeric
//         verify(mailboxService, times(1)).addBulkNotifications(anyList(), anyString());
//     }




     @Test
     public void test_validateotp() {
         Long lessonid = 1L;

         Course mockcourse = new Course("Course1", "Description1", 30, true);
         Student mockStudent = new Student();
         mockStudent.setId(1L);
         mockcourse.setStudents(Collections.singletonList(mockStudent));

         Lesson mocklesson = new Lesson();
         mocklesson.setId(lessonid);
         mocklesson.setTitle("Lesson1");
         mocklesson.setCourse(mockcourse);
         when(lessonrepo.findById(lessonid)).thenReturn(Optional.of(mocklesson));

         String generatedOtp = lessonservice.generateOtp(lessonid);
         boolean isValid = lessonservice.validateOtp(lessonid, generatedOtp);

         assertTrue(isValid);

         boolean isInvalid = lessonservice.validateOtp(lessonid, "000000");
         assertFalse(isInvalid);
     }


     @Test
     public void test_createLesson() {
         Long courseid = 1L;
         Course mockcourse = new Course("Course1", "Description1", 30, true);
         Lesson newlesson = new Lesson();
         newlesson.setTitle("Lesson1");
         newlesson.setDescription("Description1");
         newlesson.setDateTime(LocalDateTime.now());

         when(courserepo.findById(courseid)).thenReturn(Optional.of(mockcourse));
         when(lessonrepo.save(any(Lesson.class))).thenReturn(newlesson);

         Lesson createdLesson = lessonservice.createLesson(courseid, newlesson);

         assertNotNull(createdLesson);
         assertEquals(newlesson.getTitle(), createdLesson.getTitle());
         assertEquals(newlesson.getDescription(), createdLesson.getDescription());
         assertEquals(mockcourse, createdLesson.getCourse());
         verify(courserepo, times(1)).findById(courseid);
         verify(lessonrepo, times(1)).save(any(Lesson.class));
     }

     @Test
     public void test_createLesson_courseNotFound() {
         Long courseId = 99L; // Non-existing course ID
         Lesson newLesson = new Lesson();
         newLesson.setTitle("Lesson2");
         newLesson.setDescription("Description2");
         newLesson.setDateTime(LocalDateTime.now());

         when(courserepo.findById(courseId)).thenReturn(Optional.empty());

         IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
             lessonservice.createLesson(courseId, newLesson);
         });

         assertEquals("Course not found", thrown.getMessage());
     }

     @Test
     public void test_updateLesson() {
         Long lessonid = 1L;
         Lesson existinglesson = new Lesson();
         existinglesson.setId(lessonid);
         existinglesson.setTitle("Title1");
         existinglesson.setDescription("Description1");

         Lesson updatedlesson = new Lesson();
         updatedlesson.setTitle("Title2");
         updatedlesson.setDescription("Description2");
         updatedlesson.setDateTime(LocalDateTime.now());

         when(lessonrepo.findById(lessonid)).thenReturn(Optional.of(existinglesson));
         when(lessonrepo.save(any(Lesson.class))).thenReturn(updatedlesson);

         Lesson result = lessonservice.updateLesson(lessonid, updatedlesson);

         assertNotNull(result);
         assertEquals(updatedlesson.getTitle(), result.getTitle());
         assertEquals(updatedlesson.getDescription(), result.getDescription());
         verify(lessonrepo, times(1)).findById(lessonid);
         verify(lessonrepo, times(1)).save(any(Lesson.class));
     }

     @Test
     public void test_deleteLesson() {
         Long lessonid = 1L;
         Lesson existinglesson = new Lesson();
         existinglesson.setId(lessonid);
         when(lessonrepo.existsById(lessonid)).thenReturn(true);
         lessonservice.deleteLesson(lessonid);
         verify(lessonrepo, times(1)).existsById(lessonid);
         verify(lessonrepo, times(1)).deleteById(lessonid);
     }

     @Test
     public void test_deleteLesson_notFound() {
         Long lessonid = 99L;
         when(lessonrepo.existsById(lessonid)).thenReturn(false);
         IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
             lessonservice.deleteLesson(lessonid);
         });
         assertEquals("Lesson not found", thrown.getMessage());
     }
 }
