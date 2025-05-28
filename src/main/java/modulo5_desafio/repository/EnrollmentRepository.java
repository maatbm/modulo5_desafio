package modulo5_desafio.repository;

import modulo5_desafio.model.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    @Query("SELECT e FROM Enrollment e WHERE e.course.id = :courseId AND e.enrollmentDate >= :date")
    List<Enrollment> findByCourseIdAndEnrollmentDateAfter(Long courseId, LocalDate date);
}
