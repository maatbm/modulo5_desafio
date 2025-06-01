package modulo5_desafio.service;

import modulo5_desafio.DTO.EngagementReportDTO;
import modulo5_desafio.model.Course;
import modulo5_desafio.model.Enrollment;
import modulo5_desafio.model.Student;
import modulo5_desafio.repository.EnrollmentRepository;
import modulo5_desafio.repository.StudentRepository;
import modulo5_desafio.repository.CourseRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EnrollmentService {
    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public EnrollmentService(EnrollmentRepository enrollmentRepository, StudentRepository studentRepository, CourseRepository courseRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    public String insertEnrollment(String studentId, String courseId) {
        try {
            Long studentIdLong = Long.parseLong(studentId);
            Long courseIdLong = Long.parseLong(courseId);
            if (studentIdLong <= 0 || courseIdLong <= 0) {
                return "IDs devem ser números inteiros e positivos";
            } else {
                Student student = studentRepository.findById(studentIdLong).orElse(null);
                Course course = courseRepository.findById(courseIdLong).orElse(null);
                if (student == null || course == null) {
                    return "Aluno ou curso não encontrados";
                }
                if (enrollmentRepository.findByStudentIdAndCourseId(studentIdLong, courseIdLong)) {
                    return "Matrícula já existe para o aluno e curso informados";
                }
                Enrollment enrollment = new Enrollment(student, course);
                enrollmentRepository.save(enrollment);
                return "Matrícula inserida com sucesso";
            }
        } catch (NumberFormatException e) {
            return "IDs devem ser números inteiros e positivos";
        } catch (Exception e) {
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
        try {
            return enrollmentRepository.findActiveEnrollments();
        } catch (Exception e) {
            return List.of();
        }
    }

    public List<Enrollment> getDeletedEnrollments() {
        try {
            return enrollmentRepository.findDeletedEnrollments();
        } catch (Exception e) {
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

    public List<EngagementReportDTO> generateEngagementReport() {
        try {
            List<Object[]> results = enrollmentRepository.engagementReport();
            List<EngagementReportDTO> report = new ArrayList<>();
            for (Object[] result : results) {
                Long courseId = ((Number) result[0]).longValue();
                String courseTitle = (String) result[1];
                Long totalEnrollments = ((Number) result[2]).longValue();
                Double averageAge = ((Number) result[3]).doubleValue();
                Long recentEnrollments = ((Number) result[4]).longValue();
                EngagementReportDTO dto = new EngagementReportDTO(courseId, courseTitle, totalEnrollments, averageAge, recentEnrollments);
                report.add(dto);
            }
            return report;
        } catch (Exception e) {
            return List.of();
        }
    }
}
