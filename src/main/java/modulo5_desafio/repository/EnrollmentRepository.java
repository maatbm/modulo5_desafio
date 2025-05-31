package modulo5_desafio.repository;

import modulo5_desafio.model.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    @NativeQuery(value = "SELECT e.id, e.student_id, e.course_id, e.enrollment_date FROM enrollments e WHERE deleted = false")
    List<Enrollment> findActiveEnrollments();
    Optional<Enrollment> findByStudentIdAndCourseId(Long studentId, Long courseId);
}
