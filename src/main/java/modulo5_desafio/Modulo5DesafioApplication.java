package modulo5_desafio;

import io.github.cdimascio.dotenv.Dotenv;
import modulo5_desafio.model.Course;
import modulo5_desafio.model.Enrollment;
import modulo5_desafio.model.Student;
import modulo5_desafio.repository.CourseRepository;
import modulo5_desafio.repository.EnrollmentRepository;
import modulo5_desafio.repository.StudentRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

@SpringBootApplication
public class Modulo5DesafioApplication {
    private static StudentRepository studentRepository;
    private static CourseRepository courseRepository;
    private static EnrollmentRepository enrollmentRepository;
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();
        dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
        ApplicationContext context = SpringApplication.run(Modulo5DesafioApplication.class, args);
        studentRepository = context.getBean(StudentRepository.class);
        courseRepository = context.getBean(CourseRepository.class);
        enrollmentRepository = context.getBean(EnrollmentRepository.class);

        int option = -1; // Inicializa a opção com um valor inválido para entrar no loop do menu
        do {
            try {
                System.out.println("\n=== Bem-vindo! ===");
                System.out.println("1. Registrar novo aluno");
                System.out.println("2. Listar alunos");
                System.out.println("3. Buscar aluno por e-mail");
                System.out.println("4. Registrar novo curso");
                System.out.println("5. Listar cursos");
                System.out.println("6. Buscar curso por nome");
                System.out.println("7. Registrar matrícula");
                System.out.println("8. Listar matrículas");
                System.out.println("9. Gerar relatório");
                System.out.println("0. Sair");
                System.out.print("Escolha uma opção: ");
                option = scanner.nextInt();
                scanner.nextLine();

                switch (option) {
                    case 1 -> registerStudent();
                    case 2 -> listStudents();
                    case 3 -> findStudentByEmail();
                    case 4 -> registerCourse();
                    case 5 -> listCourses();
                    case 6 -> findCourseByTitle();
                    case 7 -> registerEnrollment();
                    case 8 -> listEnrollments();
                    case 9 -> generateEngagementReport();
                    case 0 -> {
                        System.out.println("Saindo do sistema. Até logo!");
                        scanner.close();
                    }
                    default -> System.err.println("Opção inválida! Tente novamente.");
                }
            } catch (Exception e) {
                System.err.println("Ocorreu um erro inesperado: " + e.getMessage() + ". Tente novamente.");
                scanner.nextLine();
            }
        }while (option != 0);
    }

    private static void registerStudent() {
        System.out.println("\n=== Registrar Aluno ===");
        try {
            System.out.print("Insira o nome: ");
            String name = scanner.nextLine();
            System.out.print("Insira o email: ");
            String email = scanner.nextLine();
            System.out.print("Insira a data de nascimento (ANO-MÊS-DIA): ");
            LocalDate birthDate = LocalDate.parse(scanner.nextLine());
            if (name == null || name.isBlank() || email == null || email.isBlank()) {
                System.err.println("Todos os campos devem estar preenchidos. Tente novamente.");
            } else if (birthDate.isAfter(LocalDate.now())) {
                System.err.println("Data de nascimento não pode ser no futuro. Tente novamente.");
            } else if (!email.contains("@")) {
                System.err.println("Email inválido. Tente novamente.");
            } else if (studentRepository.findByEmail(email).isPresent()) {
                System.err.println("Já existe um aluno registrado com este email. Tente novamente.");
            } else {
                Student student = new Student(name, email, birthDate);
                studentRepository.save(student);
                System.out.println("Aluno registrado com sucesso!");
            }
        } catch (Exception e) {
            System.err.println("Erro ao registrar aluno: " + e.getMessage() + ". Tente novamente.");
        }
    }

    private static void listStudents() {
        System.out.println("\n=== Lista de Alunos Ativos ===");
        try {
            List<Student> students = studentRepository.findActiveStudents();
            if (students.isEmpty()) {
                System.err.println("Nenhum aluno registrado.");
            } else {
                students.forEach(s -> {
                    System.out.println("ID: " + s.getId());
                    System.out.println("Nome: " + s.getName());
                    System.out.println("Email: " + s.getEmail());
                    System.out.println("Data de nascimento(ANO-MÊS-DIA): " + s.getBirthDate());
                    System.out.println("------------------------");
                });
            }
        } catch (Exception e) {
            System.err.println("Erro ao listar alunos: " + e.getMessage());
        }
    }

    private static void findStudentByEmail() {
        System.out.println("\n=== Buscar Aluno por Email ===");
        try {
            System.out.print("Insira o email do aluno: ");
            String email = scanner.nextLine();
            if (email == null || email.isBlank()) {
                System.err.println("O email não pode estar vazio. Tente novamente.");
            } else if (!email.contains("@")) {
                System.err.println("Email inválido. Tente novamente.");
            } else {
                Student student = studentRepository.findByEmail(email).orElse(null);
                if (student != null) {
                    System.out.println("ID: " + student.getId());
                    System.out.println("Nome: " + student.getName());
                    System.out.println("Email: " + student.getEmail());
                    System.out.println("Data de nascimento(ANO-MÊS-DIA): " + student.getBirthDate());
                } else {
                    System.err.println("Nenhum aluno encontrado com o email: " + email);
                }
            }
        } catch (Exception e) {
            System.err.println("Erro ao buscar aluno por email: " + e.getMessage());
        }
    }

    private static void registerCourse() {
        System.out.println("\n=== Registrar Curso ===");
        try {
            System.out.print("Insira o título do curso: ");
            String title = scanner.nextLine();
            System.out.print("Insira uma descrição: ");
            String description = scanner.nextLine();
            System.out.print("Insira a duração do curso (em horas): ");
            String hours = scanner.nextLine();
            int durationHours = Integer.parseInt(hours);
            if (title == null || title.isBlank() || description == null || description.isBlank() || durationHours <= 0) {
                System.err.println("Todos os campos devem estar preenchidos corretamente. Tente novamente.");
            } else if (courseRepository.findByTitle(title).isPresent()) {
                System.err.println("Já existe um curso registrado com este título. Tente novamente.");
            } else {
                Course course = new Course(title, description, durationHours);
                courseRepository.save(course);
                System.out.println("Curso criado com sucesso!");
            }
        } catch (NumberFormatException e) {
            System.err.println("Erro: Duração do curso deve ser um número inteiro positivo. Tente novamente.");
        } catch (Exception e) {
            System.err.println("Erro ao registrar curso: " + e.getMessage() + ". Tente novamente.");
        }
    }

    private static void listCourses() {
        System.out.println("\n=== Lista de Cursos ===");
        try {
            List<Course> courses = courseRepository.findAll();
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

    private static void findCourseByTitle() {
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

    private static void registerEnrollment() {
        System.out.println("\n=== Registrar Matrícula ===");
        try {
            if (studentRepository.count() == 0 || courseRepository.count() == 0) {
                System.err.println("Nenhum aluno ou curso registrado. Por favor, registre um aluno e um curso primeiro.");
            } else {
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
            }
        } catch (NumberFormatException e) {
            System.err.println("Erro: IDs devem ser números inteiros positivos. Tente novamente.");
        } catch (Exception e) {
            System.err.println("Ocorreu um erro inesperado: " + e.getMessage() + ". Tente novamente.");
        }
    }

    private static void listEnrollments() {
        System.out.println("\n=== Lista de Matrículas ===");
        try {
            List<Enrollment> enrollments = enrollmentRepository.findAll();
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

    private static void generateEngagementReport() {
        System.out.println("\n=== Relatório de Engajamento ===");
        try{
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