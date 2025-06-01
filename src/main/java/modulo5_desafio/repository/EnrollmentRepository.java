package modulo5_desafio.repository;

import jakarta.transaction.Transactional;
import modulo5_desafio.model.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.NativeQuery;

import java.util.List;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    @NativeQuery(value = "SELECT e.id, e.student_id, e.course_id, e.enrollment_date FROM enrollments e WHERE deleted = false")
    List<Enrollment> findActiveEnrollments();

    @NativeQuery(value = "SELECT e.id, e.student_id, e.course_id, e.enrollment_date FROM enrollments e WHERE deleted = true")
    List<Enrollment> findDeletedEnrollments();

    @NativeQuery(value = "SELECT EXISTS(SELECT 1 FROM enrollments WHERE student_id = ?1 AND course_id = ?2 AND deleted = false)")
    boolean findByStudentIdAndCourseId(Long studentId, Long courseId);

    @Modifying
    @Transactional
    @NativeQuery(value = "UPDATE enrollments SET deleted = true WHERE id = ?1 AND deleted = false")
    int deleteEnrollmentById(Long enrollmentId);

    @Modifying
    @Transactional
    @NativeQuery(value = "UPDATE enrollments SET deleted = false WHERE id = ?1 AND deleted = true")
    int restoreEnrollmentById(Long enrollmentId);

    @NativeQuery(value = "SELECT c.id AS course_id, c.title AS course_title, COUNT(e.id) AS total_enrollments, AVG(EXTRACT(YEAR FROM AGE(CURRENT_DATE, s.birth_date))) AS average_year, SUM(CASE WHEN e.enrollment_date >= CURRENT_DATE - INTERVAL '30 days' THEN 1 ELSE 0 END) AS recent_enrollments FROM courses c LEFT JOIN enrollments e ON e.course_id = c.id LEFT JOIN students s ON s.id = e.student_id GROUP BY c.id, c.title")
    List<Object[]> engagementReport();
}
