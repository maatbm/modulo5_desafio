package modulo5_desafio.repository;

import jakarta.transaction.Transactional;
import modulo5_desafio.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.NativeQuery;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    @NativeQuery(value = "SELECT s.id, s.name, s.email, s.birth_date FROM students s WHERE deleted = false")
    List<Student> findActiveStudents();

    @NativeQuery(value = "SELECT s.id, s.name, s.email, s.birth_date FROM students s WHERE deleted = true")
    List<Student> findDeletedStudents();

    @Modifying
    @Transactional
    @NativeQuery(value = "UPDATE students SET deleted = true WHERE id = ?1 AND deleted = false")
    int softDeleteStudentById(Long id);

    @Modifying
    @Transactional
    @NativeQuery(value = "UPDATE students SET deleted = false WHERE id = ?1 AND deleted = true")
    int restoreStudentById(Long id);

    @Modifying
    @Transactional
    @NativeQuery(value = "UPDATE students SET name = ?2, email = ?3, birth_date = ?4 WHERE id = ?1")
    int updateStudentById(Long id, String name, String email, LocalDate birthDate);

    @NativeQuery(value = "SELECT s.id, s.name, s.email, s.birth_date FROM students s WHERE id = ?1 AND deleted = false")
    Optional<Student> findByIdAndDeletedFalse(Long id);

    Optional<Student> findByEmail(String email);
}
