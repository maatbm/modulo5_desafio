package modulo5_desafio.repository;

import modulo5_desafio.model.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    Optional<Enrollment> findByStudentIdAndCourseId(Long studentId, Long courseId);
}
