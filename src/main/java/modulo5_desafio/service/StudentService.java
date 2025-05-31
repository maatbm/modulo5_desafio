package modulo5_desafio.service;

import modulo5_desafio.model.Student;
import modulo5_desafio.repository.StudentRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService {
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public String insertStudent(String name, String email, String birthDate){
        try{
            LocalDate date = LocalDate.parse(birthDate);
            if (name == null || name.isBlank() || email == null || email.isBlank()) {
                return ("Todos os campos devem estar preenchidos. Tente novamente.");
            } else if (date.isAfter(LocalDate.now())) {
                return ("Data de nascimento não pode ser no futuro. Tente novamente.");
            } else if (!email.contains("@")) {
                return ("Email inválido. Tente novamente.");
            } else {
                Student student = new Student(name, email, date);
                studentRepository.save(student);
                return ("Aluno registrado com sucesso!");
            }
        }catch (NumberFormatException e) {
            return ("Formato de data inválido. Use o formato AAAA-MM-DD.");
        }catch (DataIntegrityViolationException e) {
            return ("Já existe um aluno registrado com este email. Tente novamente.");
        }catch (Exception e) {
            return ("Ocorreu um erro ao registrar o aluno: " + e.getMessage());
        }
    }

    public List<Student> getActiveStudents(){
        try {
            return studentRepository.findActiveStudents();
        } catch (Exception e) {
            return List.of();
        }
    }

    public List<Student> getDeletedStudents(){
        try {
            return studentRepository.findDeletedStudents();
        } catch (Exception e) {
            return List.of();
        }
    }

    public List<Student> getAllStudents() {
        try {
            return studentRepository.findAll();
        } catch (Exception e) {
            return List.of();
        }
    }

    public Optional<Student> getStudentByEmail(String email) {
        try {
            if(email == null || email.isBlank()) {
                return Optional.empty();
            } else if (!email.contains("@")) {
                return Optional.empty();
            }else {
                return studentRepository.findByEmail(email);
            }
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public String deleteStudent(String id) {
        try {
            Long studentId = Long.parseLong(id);
            int deleted = studentRepository.softDeleteStudentById(studentId);
            if(deleted == 1) {
                return "Aluno deletado com sucesso!";
            } else {
                return "Aluno não encontrado ou já deletado.";
            }
        }catch (NumberFormatException e){
            return "ID inválido. Por favor, insira um número válido.";
        }catch (Exception e) {
            return "Erro ao deletar aluno: " + e.getMessage();
        }
    }

    public String restoreStudent(String id) {
        try {
            Long studentId = Long.parseLong(id);
            int restored = studentRepository.restoreStudentById(studentId);
            if(restored == 1) {
                return "Aluno restaurado com sucesso!";
            } else {
                return "Aluno não encontrado ou já ativo.";
            }
        } catch (NumberFormatException e) {
            return "ID inválido. Por favor, insira um número válido.";
        } catch (Exception e) {
            return "Erro ao restaurar aluno: " + e.getMessage();
        }
    }

    public String updateStudent(String id, String name, String email, String birthDate) {
        try {
            Long studentId = Long.parseLong(id);
            LocalDate date = LocalDate.parse(birthDate);
            if (name == null || name.isBlank() || email == null || email.isBlank()) {
                return ("Todos os campos devem estar preenchidos. Tente novamente.");
            } else if (date.isAfter(LocalDate.now())) {
                return ("Data de nascimento não pode ser no futuro. Tente novamente.");
            } else if (!email.contains("@")) {
                return ("Email inválido. Tente novamente.");
            }else {
                int updated = studentRepository.updateStudentById(studentId, name, email, date);
                if(updated == 1) {
                    return ("Aluno atualizado com sucesso!");
                }else {
                    return ("Ocorreu um erro ao atualizar o aluno. Verifique se o ID está correto.");
                }
            }
        }catch (NumberFormatException e) {
            return ("Formato de data ou ID invválido. Use o formato AAAA-MM-DD para data e inteiros positivos para ID.");
        }catch (DataIntegrityViolationException e) {
            return ("Já existe um aluno registrado com este email. Tente novamente.");
        }catch (Exception e) {
            return ("Ocorreu um erro ao atualizar o aluno: " + e.getMessage());
        }
    }
}
