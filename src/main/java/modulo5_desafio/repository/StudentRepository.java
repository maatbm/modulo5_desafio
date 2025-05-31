package modulo5_desafio.repository;

import modulo5_desafio.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    @NativeQuery(value = "SELECT s.id, s.name, s.email, s.birth_date FROM students s WHERE deleted = false")
    List<Student> findActiveStudents();
    Optional<Student> findByEmail(String email);
}
