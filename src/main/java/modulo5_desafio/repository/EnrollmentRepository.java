package modulo5_desafio.repository;

import jakarta.transaction.Transactional;
import modulo5_desafio.model.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.NativeQuery;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    @NativeQuery(value = "SELECT e.id, e.student_id, e.course_id, e.enrollment_date FROM enrollments e WHERE deleted = false")
    List<Enrollment> findActiveEnrollments();

    @NativeQuery(value = "SELECT EXISTS(SELECT 1 FROM enrollments WHERE student_id = ?1 AND course_id = ?2 AND deleted = false)")
    boolean findByStudentIdAndCourseId(Long studentId, Long courseId);

    @NativeQuery(value = "SELECT s.id, s.name, s.email, s.birth_date, c.id, c.title, c.description, c.duration_hours FROM students s JOIN courses c ON c.id = ?2 WHERE s.id = ?1 AND s.deleted = false AND c.deleted = false")
    Optional<Object> findStudentAndCourseById(Long studentId, Long courseId);

    @Modifying
    @Transactional
    @NativeQuery(value = "UPDATE enrollments SET deleted = true WHERE id = ?1 AND deleted = false")
    boolean deleteEnrollmentById(Long enrollmentId);
}
