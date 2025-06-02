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
    int softDeleteCourse(Long id);

    @Modifying
    @Transactional
    @NativeQuery(value = "UPDATE courses SET deleted = false WHERE id = ?1 AND deleted = true")
    int restoreCourse(Long id);

    @Modifying
    @Transactional
    @NativeQuery(value = "UPDATE courses SET title = ?2, description = ?3, duration_hours = ?4 WHERE id = ?1")
    int updateCourse(Long id, String title, String description, int durationHours);

    @NativeQuery(value = "SELECT c.id, c.title, c.description, c.duration_hours FROM courses c WHERE id = ?1 AND deleted = false")
    Optional<Course> findByIdAndDeletedFalse(Long id);

    List<Course> findByTitleContainingIgnoreCase(String title);
}
