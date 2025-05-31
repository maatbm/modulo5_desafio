package modulo5_desafio.service;

import modulo5_desafio.model.Student;
import modulo5_desafio.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class StudentService {
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public String InsertStudent(String name, String email, String birthDate){
        try{
            LocalDate date = LocalDate.parse(birthDate);
            if (name == null || name.isBlank() || email == null || email.isBlank()) {
                return ("Todos os campos devem estar preenchidos. Tente novamente.");
            } else if (date.isAfter(LocalDate.now())) {
                return ("Data de nascimento não pode ser no futuro. Tente novamente.");
            } else if (!email.contains("@")) {
                return ("Email inválido. Tente novamente.");
            } else if (studentRepository.findByEmail(email).isPresent()) {
                return ("Já existe um aluno registrado com este email. Tente novamente.");
            } else {
                Student student = new Student(name, email, date);
                studentRepository.save(student);
                return ("Aluno registrado com sucesso!");
            }
        }catch (NumberFormatException e) {
            return ("Formato de data inválido. Use o formato AAAA-MM-DD.");
        } catch (Exception e) {
            return ("Ocorreu um erro ao registrar o aluno: " + e.getMessage());
        }
    }
}
