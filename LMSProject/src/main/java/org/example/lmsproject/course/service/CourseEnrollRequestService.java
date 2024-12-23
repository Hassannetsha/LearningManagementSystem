package org.example.lmsproject.course.service;

import org.example.lmsproject.course.model.CourseEnrollRequest;
import org.example.lmsproject.course.model.Course;
import org.example.lmsproject.course.repository.CourseEnrollRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseEnrollRequestService {

    private final CourseEnrollRequestRepository courseEnrollRequestRepository;

    @Autowired
    public CourseEnrollRequestService(CourseEnrollRequestRepository courseEnrollRequestRepository) {
        this.courseEnrollRequestRepository = courseEnrollRequestRepository;
    }

    public CourseEnrollRequest save(CourseEnrollRequest enrollRequest) {
        return courseEnrollRequestRepository.save(enrollRequest);
    }

    public CourseEnrollRequest findById(long requestId) {
        return courseEnrollRequestRepository.findById(requestId).orElse(null);
    }

    public String getEnrollments(Course course) {
        List<CourseEnrollRequest> requests = courseEnrollRequestRepository.findAll().stream()
                .filter(courseEnrollRequest -> courseEnrollRequest.getCourse().equals(course)).toList();

        return requests.stream()
                .map(CourseEnrollRequest::toString)
                .collect(Collectors.joining(",\n\n    ", "[\n    ", "]\n"));
    }
}