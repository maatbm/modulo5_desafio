package modulo5_desafio;

import io.github.cdimascio.dotenv.Dotenv;
import modulo5_desafio.model.Course;
import modulo5_desafio.model.Enrollment;
import modulo5_desafio.model.Student;
import modulo5_desafio.repository.CourseRepository;
import modulo5_desafio.repository.EnrollmentRepository;
import modulo5_desafio.repository.StudentRepository;
import modulo5_desafio.service.CourseService;
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
    private static StudentRepository studentRepository;
    private static StudentService studentService;
    private static CourseRepository courseRepository;
    private static CourseService courseService;
    private static EnrollmentRepository enrollmentRepository;
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();
        dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
        ApplicationContext context = SpringApplication.run(Modulo5DesafioApplication.class, args);
        studentRepository = context.getBean(StudentRepository.class);
        studentService = context.getBean(StudentService.class);
        courseRepository = context.getBean(CourseRepository.class);
        courseService = context.getBean(CourseService.class);
        enrollmentRepository = context.getBean(EnrollmentRepository.class);

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
        if(student.isEmpty()){
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

    protected static void listCourses() {
        System.out.println("\n=== Lista de Cursos Ativos ===");
        try {
            List<Course> courses = courseRepository.findActiveCourses();
            if (courses.isEmpty()) {
                System.err.println("Nenhum curso registrado.");
            } else {
                courses.forEach(c -> {
                    System.out.println("ID: " + c.getId());
                    System.out.println("Título: " + c.getTitle());
                    System.out.println("Descrição: " + c.getDescription());
                    System.out.println("Duração (horas): " + c.getDurationHours());
                    System.out.println("------------------------");
                });
            }
        } catch (Exception e) {
            System.err.println("Erro ao listar cursos: " + e.getMessage());
        }
    }

    protected static void findCourseByTitle() {
        System.out.println("\n=== Buscar Curso por Título ===");
        try {
            System.out.print("Insira o título do curso (ou parte dele): ");
            String title = scanner.nextLine();
            if (title == null || title.isBlank()) {
                System.err.println("O título não pode estar vazio. Tente novamente.");
            } else {
                List<Course> courses = courseRepository.findByTitleContainingIgnoreCase(title);
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
        } catch (Exception e) {
            System.err.println("Erro ao buscar curso por título: " + e.getMessage());
        }
    }

    protected static void registerEnrollment() {
        System.out.println("\n=== Registrar Matrícula ===");
        try {
            System.out.print("Insira o ID do aluno: ");
            String studentId = scanner.nextLine();
            System.out.print("Insira o ID do curso: ");
            String courseId = scanner.nextLine();
            long studentIdLong = Long.parseLong(studentId);
            long courseIdLong = Long.parseLong(courseId);
            if (studentIdLong <= 0 || courseIdLong <= 0) {
                System.err.println("IDs devem ser números positivos. Tente novamente.");
            } else {
                Student student = studentRepository.findById(studentIdLong).orElse(null);
                Course course = courseRepository.findById(courseIdLong).orElse(null);
                if (student == null) {
                    System.err.println("Nenhum aluno encontrado com o ID: " + studentIdLong);
                } else if (course == null) {
                    System.err.println("Nenhum curso encontrado com o ID: " + courseIdLong);
                } else if (enrollmentRepository.findByStudentIdAndCourseId(studentIdLong, courseIdLong).isPresent()) {
                    System.err.println("Este aluno já está matriculado neste curso.");
                } else {
                    Enrollment enrollment = new Enrollment(student, course);
                    enrollmentRepository.save(enrollment);
                    System.out.println("Matrícula registrada com sucesso!");
                }
            }
        } catch (NumberFormatException e) {
            System.err.println("Erro: IDs devem ser números inteiros positivos. Tente novamente.");
        } catch (Exception e) {
            System.err.println("Ocorreu um erro inesperado: " + e.getMessage() + ". Tente novamente.");
        }
    }

    protected static void listEnrollments() {
        System.out.println("\n=== Lista de Matrículas Ativas ===");
        try {
            List<Enrollment> enrollments = enrollmentRepository.findActiveEnrollments();
            if (enrollments.isEmpty()) {
                System.err.println("Nenhuma matrícula registrada.");
            } else {
                enrollments.forEach(e -> {
                    System.out.println("ID: " + e.getId());
                    System.out.println("Aluno: " + e.getStudent().getName() + " (ID: " + e.getStudent().getId() + ")");
                    System.out.println("Curso: " + e.getCourse().getTitle() + " (ID: " + e.getCourse().getId() + ")");
                    System.out.println("Data de matrícula: " + e.getEnrollmentDate());
                    System.out.println("------------------------");
                });
            }
        } catch (Exception e) {
            System.err.println("Erro ao listar matrículas: " + e.getMessage());
        }
    }

    protected static void generateEngagementReport() {
        System.out.println("\n=== Relatório de Engajamento ===");
        try {
            List<Object[]> reportData = courseRepository.engagementReport();
            if (reportData.isEmpty()) {
                System.err.println("Nenhum dado de engajamento encontrado.");
            } else {
                for (Object[] row : reportData) {
                    Long courseId = (Long) row[0];
                    String courseTitle = (String) row[1];
                    Long totalEnrollments = (Long) row[2];
                    double averageAge = row[3] != null ? ((Number) row[3]).doubleValue() : 0.0;
                    Long recentEnrollments = (Long) row[4];
                    System.out.println("Curso ID: " + courseId);
                    System.out.println("Título do Curso: " + courseTitle);
                    System.out.println("Total de Matrículas: " + totalEnrollments);
                    System.out.println("Idade Média dos Alunos: " + averageAge);
                    System.out.println("Matrículas Recentes (últimos 30 dias): " + recentEnrollments);
                    System.out.println("------------------------");
                }
            }
        } catch (Exception e) {
            System.err.println("Erro ao gerar relatório de engajamento: " + e.getMessage());
        }
    }
}