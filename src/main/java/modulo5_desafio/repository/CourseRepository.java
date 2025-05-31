package modulo5_desafio.repository;

import jakarta.transaction.Transactional;
import modulo5_desafio.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.NativeQuery;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {
    @NativeQuery(value = "SELECT c.id, c.title, c.description, c.duration_hours FROM courses c WHERE deleted = false")
    List<Course> findActiveCourses();

    @NativeQuery(value = "SELECT c.id, c.title, c.description, c.duration_hours FROM courses c WHERE deleted = true")
    List<Course> findDeletedCourses();

    @Modifying
    @Transactional
    @NativeQuery(value = "UPDATE courses SET deleted = true WHERE id = ?1 AND deleted = false")
    boolean softDeleteCourse(Long id);

    @Modifying
    @Transactional
    @NativeQuery(value = "UPDATE courses SET deleted = false WHERE id = ?1 AND deleted = true")
    boolean restoreCourse(Long id);

    @Modifying
    @Transactional
    @NativeQuery(value = "UPDATE courses SET title = ?2, description = ?3, duration_hours = ?4 WHERE id = ?1")
    boolean updateCourse(Long id, String title, String description, int durationHours);

    List<Course> findByTitleContainingIgnoreCase(String title);

    Optional<Course> findByTitle(String title);

    @NativeQuery(value = "SELECT c.id AS course_id, c.title AS course_title, COUNT(e.id) AS total_enrollments, AVG(EXTRACT(YEAR FROM AGE(CURRENT_DATE, s.birth_date))) AS average_year, SUM(CASE WHEN e.enrollment_date >= CURRENT_DATE - INTERVAL '30 days' THEN 1 ELSE 0 END) AS recent_enrollments FROM courses c LEFT JOIN enrollments e ON e.course_id = c.id LEFT JOIN students s ON s.id = e.student_id GROUP BY c.id, c.title")
    List<Object[]> engagementReport();
}
