package modulo5_desafio.service;

import modulo5_desafio.model.Course;
import modulo5_desafio.model.Enrollment;
import modulo5_desafio.model.Student;
import modulo5_desafio.repository.EnrollmentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class EnrollmentService {
    private final EnrollmentRepository enrollmentRepository;
    public EnrollmentService(EnrollmentRepository enrollmentRepository) {
        this.enrollmentRepository = enrollmentRepository;
    }

    public String insertEnrollment(String studentId, String courseId) {
        try{
            Long studentIdLong = Long.parseLong(studentId);
            Long courseIdLong = Long.parseLong(courseId);
            if (studentIdLong <= 0 || courseIdLong <= 0) {
                return "IDs devem ser números inteiros e positivos";
            }else {
                if (enrollmentRepository.findByStudentIdAndCourseId(studentIdLong, courseIdLong)) {
                    return "Matrícula já existe para o aluno e curso informados";
                } else {
                    Optional<Object> studentAndCourse = enrollmentRepository.findStudentAndCourseById(studentIdLong, courseIdLong);
                    if(studentAndCourse.isEmpty()){
                        return ("Aluno ou curso não encontrados");
                    }else {
                        Object[] result = (Object[]) studentAndCourse.get();
                        Student student = new Student((String) result[1], (String) result[2], (LocalDate) result[3]);
                        Course course = new Course((String) result[5], (String) result[6], (Integer) result[7]);
                        Enrollment enrollment = new Enrollment(student, course);
                        enrollmentRepository.save(enrollment);
                        return "Matrícula inserida com sucesso";
                    }
                }
            }
        }catch (NumberFormatException e) {
            return "IDs devem ser números inteiros e positivos";
        }catch (Exception e){
            return "Erro ao inserir matrícula: " + e.getMessage();
        }
    }

    public String deleteEnrollment(String enrollmentId) {
        try {
            long id = Long.parseLong(enrollmentId);
            if (id <= 0) {
                return "ID deve ser um número inteiro e positivo";
            } else {
                int result = enrollmentRepository.deleteEnrollmentById(id);
                if (result == 1) {
                    return "Matrícula excluída com sucesso";
                } else {
                    return "Matrícula não encontrada ou já excluída";
                }
            }
        } catch (NumberFormatException e) {
            return "ID deve ser um número inteiro e positivo";
        } catch (Exception e) {
            return "Erro ao excluir matrícula: " + e.getMessage();
        }
    }

    public String restoreEnrollment(String enrollmentId) {
        try {
            long id = Long.parseLong(enrollmentId);
            if (id <= 0) {
                return "ID deve ser um número inteiro e positivo";
            } else {
                int result = enrollmentRepository.restoreEnrollmentById(id);
                if (result == 1) {
                    return "Matrícula restaurada com sucesso";
                } else {
                    return "Matrícula não encontrada ou já ativa";
                }
            }
        } catch (NumberFormatException e) {
            return "ID deve ser um número inteiro e positivo";
        } catch (Exception e) {
            return "Erro ao restaurar matrícula: " + e.getMessage();
        }
    }

    public List<Enrollment> getActiveEnrollments() {
        try{
            return enrollmentRepository.findActiveEnrollments();
        }catch (Exception e){
            return List.of();
        }
    }

    public List<Enrollment> getDeletedEnrollments() {
        try{
            return enrollmentRepository.findDeletedEnrollments();
        }catch (Exception e){
            return List.of();
        }
    }

    public List<Enrollment> getAllEnrollments() {
        try {
            return enrollmentRepository.findAll();
        } catch (Exception e) {
            return List.of();
        }
    }

    public List<Object[]> generateEngagementReport() {
        try {
            return enrollmentRepository.engagementReport();
        } catch (Exception e) {
            return List.of();
        }
    }
}
