package modulo5_desafio;

import io.github.cdimascio.dotenv.Dotenv;
import modulo5_desafio.DTO.EngagementReportDTO;
import modulo5_desafio.model.Course;
import modulo5_desafio.model.Enrollment;
import modulo5_desafio.model.Student;
import modulo5_desafio.service.CourseService;
import modulo5_desafio.service.EnrollmentService;
import modulo5_desafio.service.StudentService;

import static modulo5_desafio.util.Utils.printSuccess;
import static modulo5_desafio.util.Utils.printError;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@SpringBootApplication
public class Modulo5DesafioApplication {
    private static StudentService studentService;
    private static CourseService courseService;
    private static EnrollmentService enrollmentService;

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();
        dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
        ApplicationContext context = SpringApplication.run(Modulo5DesafioApplication.class, args);
        studentService = context.getBean(StudentService.class);
        courseService = context.getBean(CourseService.class);
        enrollmentService = context.getBean(EnrollmentService.class);

        int receivedOption = -1; // Inicializa a opção com um valor inválido para entrar no loop do menu
        do {
            try {
                System.out.println("\n=== MENU ===");
                for (MenuOption option : MenuOption.values()) {
                    System.out.println(option.code + ". " + option.label);
                }
                System.out.println("0. Sair");
                System.out.print("Escolha uma opção: ");
                receivedOption = scanner.nextInt();
                scanner.nextLine();

                if (receivedOption == 0) {
                    System.out.println("Saindo do sistema. Até logo!");
                    break;
                }

                MenuOption menuOption = MenuOption.fromCode(receivedOption);
                if (menuOption != null) {
                    menuOption.action.run();
                } else {
                    System.err.println("Opção inválida! Tente novamente.");
                }
            } catch (Exception e) {
                System.err.println("Ocorreu um erro inesperado: " + e.getMessage() + ". Tente novamente.");
                scanner.nextLine();
            }
        } while (receivedOption != 0);
        scanner.close();
    }

    // FUNÇÕES PARA GERENCIAMENTO DE ALUNOS
    protected static void registerStudent() {
        System.out.println("\n=== Registrar Aluno ===");
        System.out.print("Insira o nome: ");
        String name = scanner.nextLine();
        System.out.print("Insira o email: ");
        String email = scanner.nextLine();
        System.out.print("Insira a data de nascimento (ANO-MÊS-DIA): ");
        String birthDate = scanner.nextLine();
        String result = studentService.insertStudent(name, email, birthDate);
        if (result.contains("sucesso")) {
            printSuccess(result);
        } else {
            printError(result);
        }
    }

    protected static void listActiveStudents() {
        System.out.println("\n=== Lista de Alunos Ativos ===");
        List<Student> students = studentService.getActiveStudents();
        if (students.isEmpty()) {
            printError("Nenhum aluno ativo encontrado.");
        } else {
            students.forEach(s -> {
                System.out.println("ID: " + s.getId());
                System.out.println("Nome: " + s.getName());
                System.out.println("Email: " + s.getEmail());
                System.out.println("Data de nascimento(ANO-MÊS-DIA): " + s.getBirthDate());
                System.out.println("------------------------");
            });
        }
    }

    protected static void listDeletedStudents() {
        System.out.println("\n=== Lista de Alunos Deletados ===");
        List<Student> students = studentService.getDeletedStudents();
        if (students.isEmpty()) {
            printError("Nenhum aluno deletado encontrado.");
        } else {
            students.forEach(s -> {
                System.out.println("ID: " + s.getId());
                System.out.println("Nome: " + s.getName());
                System.out.println("Email: " + s.getEmail());
                System.out.println("Data de nascimento(ANO-MÊS-DIA): " + s.getBirthDate());
                System.out.println("------------------------");
            });
        }
    }

    protected static void listAllStudents() {
        System.out.println("\n=== Lista de Todos os Alunos ===");
        List<Student> students = studentService.getAllStudents();
        if (students.isEmpty()) {
            printError("Nenhum aluno encontrado.");
        } else {
            students.forEach(s -> {
                System.out.println("ID: " + s.getId());
                System.out.println("Nome: " + s.getName());
                System.out.println("Email: " + s.getEmail());
                System.out.println("Data de nascimento(ANO-MÊS-DIA): " + s.getBirthDate());
                System.out.println("------------------------");
            });
        }
    }

    protected static void deleteStudent() {
        System.out.println("\n=== Deletar Aluno ===");
        System.out.println("Insira o ID do aluno que deseja deletar:");
        String studentId = scanner.nextLine();
        String result = studentService.deleteStudent(studentId);
        if (result.contains("sucesso")) {
            printSuccess(result);
        } else {
            printError(result);
        }
    }

    protected static void restoreStudent() {
        System.out.println("\n=== Restaurar Aluno ===");
        System.out.print("Insira o ID do aluno que deseja restaurar: ");
        String studentId = scanner.nextLine();
        String result = studentService.restoreStudent(studentId);
        if (result.contains("sucesso")) {
            printSuccess(result);
        } else {
            printError(result);
        }
    }

    protected static void updateStudent() {
        System.out.println("\n=== Atualizar Aluno ===");
        System.out.print("Insira o ID do aluno: ");
        String studentId = scanner.nextLine();
        System.out.print("Insira o novo nome: ");
        String name = scanner.nextLine();
        System.out.print("Insira o novo email: ");
        String email = scanner.nextLine();
        System.out.print("Insira a nova data de nascimento (ANO-MÊS-DIA): ");
        String birthDate = scanner.nextLine();
        String result = studentService.updateStudent(studentId, name, email, birthDate);
        if (result.contains("sucesso")) {
            printSuccess(result);
        } else {
            printError(result);
        }
    }

    protected static void findStudentByEmail() {
        System.out.println("\n=== Buscar Aluno por Email ===");
        System.out.print("Insira o email do aluno: ");
        String email = scanner.nextLine();
        Optional<Student> student = studentService.getStudentByEmail(email);
        if (student.isEmpty()) {
            System.err.println("Nenhum aluno encontrado com o email: " + email);
        } else {
            Student s = student.get();
            System.out.println("ID: " + s.getId());
            System.out.println("Nome: " + s.getName());
            System.out.println("Email: " + s.getEmail());
            System.out.println("Data de nascimento(ANO-MÊS-DIA): " + s.getBirthDate());
            System.out.println("------------------------");
        }
    }

    // FUNÇÕES PARA GERENCIAMENTO DE CURSOS
    protected static void registerCourse() {
        System.out.println("\n=== Registrar Curso ===");
        System.out.print("Insira o título do curso: ");
        String title = scanner.nextLine();
        System.out.print("Insira uma descrição: ");
        String description = scanner.nextLine();
        System.out.print("Insira a duração do curso (em horas): ");
        String hours = scanner.nextLine();
        String result = courseService.insertCourse(title, description, hours);
        if (result.contains("sucesso")) {
            printSuccess(result);
        } else {
            printError(result);
        }
    }

    protected static void listActiveCourses() {
        System.out.println("\n=== Lista de Cursos Ativos ===");
        List<Course> courses = courseService.getActiveCourses();
        if (courses.isEmpty()) {
            System.err.println("Nenhum curso ativo encontrado.");
        } else {
            courses.forEach(c -> {
                System.out.println("ID: " + c.getId());
                System.out.println("Título: " + c.getTitle());
                System.out.println("Descrição: " + c.getDescription());
                System.out.println("Duração (horas): " + c.getDurationHours());
                System.out.println("------------------------");
            });
        }
    }

    protected static void listDeletedCourses() {
        System.out.println("\n=== Lista de Cursos Deletados ===");
        List<Course> courses = courseService.getDeletedCourses();
        if (courses.isEmpty()) {
            System.err.println("Nenhum curso deletado encontrado.");
        } else {
            courses.forEach(c -> {
                System.out.println("ID: " + c.getId());
                System.out.println("Título: " + c.getTitle());
                System.out.println("Descrição: " + c.getDescription());
                System.out.println("Duração (horas): " + c.getDurationHours());
                System.out.println("------------------------");
            });
        }
    }

    protected static void listAllCourses() {
        System.out.println("\n=== Lista de Todos os Cursos ===");
        List<Course> courses = courseService.getAllCourses();
        if (courses.isEmpty()) {
            System.err.println("Nenhum curso encontrado.");
        } else {
            courses.forEach(c -> {
                System.out.println("ID: " + c.getId());
                System.out.println("Título: " + c.getTitle());
                System.out.println("Descrição: " + c.getDescription());
                System.out.println("Duração (horas): " + c.getDurationHours());
                System.out.println("------------------------");
            });
        }
    }

    protected static void deleteCourse() {
        System.out.println("\n=== Deletar Curso ===");
        System.out.print("Insira o ID do curso que deseja deletar: ");
        String courseId = scanner.nextLine();
        String result = courseService.deleCourse(courseId);
        if (result.contains("sucesso")) {
            printSuccess(result);
        } else {
            printError(result);
        }
    }

    protected static void restoreCourse() {
        System.out.println("\n=== Restaurar Curso ===");
        System.out.print("Insira o ID do curso que deseja restaurar: ");
        String courseId = scanner.nextLine();
        String result = courseService.restoreCourse(courseId);
        if (result.contains("sucesso")) {
            printSuccess(result);
        } else {
            printError(result);
        }
    }

    protected static void updateCourse() {
        System.out.println("\n=== Atualizar Curso ===");
        System.out.print("Insira o ID do curso: ");
        String courseId = scanner.nextLine();
        System.out.print("Insira o novo título: ");
        String title = scanner.nextLine();
        System.out.print("Insira a nova descrição: ");
        String description = scanner.nextLine();
        System.out.print("Insira a nova duração (em horas): ");
        String hours = scanner.nextLine();
        String result = courseService.updateCourse(courseId, title, description, hours);
        if (result.contains("sucesso")) {
            printSuccess(result);
        } else {
            printError(result);
        }
    }

    protected static void findCourseByTitle() {
        System.out.println("\n=== Buscar Curso por Título ===");
        System.out.print("Insira o título do curso(ou parte dele): ");
        String title = scanner.nextLine();
        List<Course> courses = courseService.getCourseByTitle(title);
        if (courses.isEmpty()) {
            System.err.println("Nenhum curso encontrado com o título: " + title);
        } else {
            courses.forEach(c -> {
                System.out.println("ID: " + c.getId());
                System.out.println("Título: " + c.getTitle());
                System.out.println("Descrição: " + c.getDescription());
                System.out.println("Duração (horas): " + c.getDurationHours());
                System.out.println("------------------------");
            });
        }
    }

    // FUNÇÕES PARA GERENCIAMENTO DE MATRÍCULAS
    protected static void registerEnrollment() {
        System.out.println("\n=== Registrar Matrícula ===");
        System.out.print("Insira o ID do aluno: ");
        String studentId = scanner.nextLine();
        System.out.print("Insira o ID do curso: ");
        String courseId = scanner.nextLine();
        String result = enrollmentService.insertEnrollment(studentId, courseId);
        if (result.contains("sucesso")) {
            printSuccess(result);
        } else {
            printError(result);
        }
    }

    protected static void listActiveEnrollments() {
        System.out.println("\n=== Lista de Matrículas Ativas ===");
        List<Enrollment> enrollments = enrollmentService.getActiveEnrollments();
        if (enrollments.isEmpty()) {
            System.err.println("Nenhuma matrícula ativa encontrada.");
        } else {
            enrollments.forEach(e -> {
                System.out.println("ID: " + e.getId());
                System.out.println("Aluno: " + e.getStudent().getName() + " (ID: " + e.getStudent().getId() + ")");
                System.out.println("Curso: " + e.getCourse().getTitle() + " (ID: " + e.getCourse().getId() + ")");
                System.out.println("Data de Matrícula: " + e.getEnrollmentDate());
                System.out.println("------------------------");
            });
        }
    }

    protected static void listDeletedEnrollments() {
        System.out.println("\n=== Lista de Matrículas Deletadas ===");
        List<Enrollment> enrollments = enrollmentService.getDeletedEnrollments();
        if (enrollments.isEmpty()) {
            System.err.println("Nenhuma matrícula deletada encontrada.");
        } else {
            enrollments.forEach(e -> {
                System.out.println("ID: " + e.getId());
                System.out.println("Aluno: " + e.getStudent().getName() + " (ID: " + e.getStudent().getId() + ")");
                System.out.println("Curso: " + e.getCourse().getTitle() + " (ID: " + e.getCourse().getId() + ")");
                System.out.println("Data de Matrícula: " + e.getEnrollmentDate());
                System.out.println("------------------------");
            });
        }
    }

    protected static void listAllEnrollments() {
        System.out.println("\n=== Lista de Todas as Matrículas ===");
        List<Enrollment> enrollments = enrollmentService.getAllEnrollments();
        if (enrollments.isEmpty()) {
            System.err.println("Nenhuma matrícula encontrada.");
        } else {
            enrollments.forEach(e -> {
                System.out.println("ID: " + e.getId());
                System.out.println("Aluno: " + e.getStudent().getName() + " (ID: " + e.getStudent().getId() + ")");
                System.out.println("Curso: " + e.getCourse().getTitle() + " (ID: " + e.getCourse().getId() + ")");
                System.out.println("Data de Matrícula: " + e.getEnrollmentDate());
                System.out.println("------------------------");
            });
        }
    }

    protected static void deleteEnrollment() {
        System.out.println("\n=== Deletar Matrícula ===");
        System.out.print("Insira o ID da matrícula que deseja deletar: ");
        String enrollmentId = scanner.nextLine();
        String result = enrollmentService.deleteEnrollment(enrollmentId);
        if (result.contains("sucesso")) {
            printSuccess(result);
        } else {
            printError(result);
        }
    }

    protected static void restoreEnrollment() {
        System.out.println("\n=== Restaurar Matrícula ===");
        System.out.print("Insira o ID da matrícula que deseja restaurar: ");
        String enrollmentId = scanner.nextLine();
        String result = enrollmentService.restoreEnrollment(enrollmentId);
        if (result.contains("sucesso")) {
            printSuccess(result);
        } else {
            printError(result);
        }
    }

    protected static void generateEngagementReport() {
        System.out.println("\n=== Relatório de Engajamento ===");
        List<EngagementReportDTO> report = enrollmentService.generateEngagementReport();
        if (report.isEmpty()) {
            System.err.println("Nenhum dado encontrado para gerar o relatório de engajamento.");
        } else {
            report.forEach(r -> {
                System.out.println("ID do Curso: " + r.courseId());
                System.out.println("Título do Curso: " + r.courseTitle());
                System.out.println("Total de Matrículas: " + r.totalEnrollments());
                System.out.println("Idade Média dos Alunos: " + r.averageAge());
                System.out.println("Matrículas Recentes (últimos 30 dias): " + r.recentEnrollments());
                System.out.println("------------------------");
            });
        }
    }
}

