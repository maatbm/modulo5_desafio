package modulo5_desafio;

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
import java.time.Period;
import java.util.List;
import java.util.Scanner;

@SpringBootApplication
public class Modulo5DesafioApplication {
	private static StudentRepository studentRepository;
	private static CourseRepository courseRepository;
	private static EnrollmentRepository enrollmentRepository;
	private static final Scanner scanner = new Scanner(System.in);

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(Modulo5DesafioApplication.class, args);
		studentRepository = context.getBean(StudentRepository.class);
		courseRepository = context.getBean(CourseRepository.class);
		enrollmentRepository = context.getBean(EnrollmentRepository.class);

		while (true) {
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

			int option = scanner.nextInt();
			scanner.nextLine();

			switch (option) {
				case 1 -> registerStudent();
				case 2 -> listStudents();
				case 3 -> findStudentByEmail();
				case 4 -> registerCourse();
				case 5 -> listCourses();
				case 6 -> findCourseByName();
				case 7 -> registerEnrollment();
				case 8 -> listEnrollments();
				case 9 -> generateEngagementReport();
				case 0 -> {
					System.out.println("Saindo...");
					scanner.close();
					System.exit(0);
				}
				default -> System.out.println("Opção inválida! Tente novamente.");
			}
		}
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
			if(name == null || name.isBlank() || email == null || email.isBlank()) {
				System.err.println("Todos os campos devem estar preenchidos. Tente novamente.");
			} else if (birthDate.isAfter(LocalDate.now())) {
				System.err.println("Data de nascimento não pode ser no futuro. Tente novamente.");
			}else if (!email.contains("@")) {
				System.err.println("Email inválido. Tente novamente.");
			} else if (studentRepository.findByEmail(email).isPresent()) {
				System.err.println("Já existe um aluno registrado com este email. Tente novamente.");
			}else {
				Student student = new Student(name, email, birthDate);
				studentRepository.save(student);
				System.out.println("Aluno registrado com sucesso!");
			}
		}catch (Exception e) {
			System.err.println("Erro ao registrar aluno: " + e.getMessage() + ". Tente novamente.");
		}
	}

	private static void listStudents() {
		System.out.println("\n=== Lista de Alunos ===");
		try {
			List<Student> students = studentRepository.findAll();
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
		}catch (Exception e) {
			System.err.println("Erro ao listar alunos: " + e.getMessage());
		}
	}

	private static void findStudentByEmail() {
		System.out.println("\n=== Buscar Aluno por Email ===");
		try{
			System.out.print("Insira o email do aluno: ");
			String email = scanner.nextLine();
			if (email == null || email.isBlank()) {
				System.err.println("O email não pode estar vazio. Tente novamente.");
			} else if (!email.contains("@")) {
				System.err.println("Email inválido. Tente novamente.");
			}else {
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
			}else if (courseRepository.findByTitle(title).isPresent()) {
				System.err.println("Já existe um curso registrado com este título. Tente novamente.");
			}else {
				Course course = new Course(title, description, durationHours);
				courseRepository.save(course);
				System.out.println("Curso criado com sucesso!");
			}
		}catch (NumberFormatException e){
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

	private static void findCourseByName() {
		System.out.print("Insira o título do curso (ou parte dele): ");
		String name = scanner.nextLine();
		List<Course> courses = courseRepository.findByTitleContainingIgnoreCase(name);
		courses.forEach(c -> System.out.println("ID: " + c.getId() + ", título: " + c.getTitle() + ", Descrição: " + c.getDescription() + ", Duração: " + c.getDurationHours()));
		if (courses.isEmpty()) System.out.println("Nenhum curso encontrado com o título: " + name);
	}

	private static void registerEnrollment() {
		System.out.print("Insira o ID do aluno: ");
		Long studentId = scanner.nextLong();
		System.out.print("Insira o ID do curso: ");
		Long courseId = scanner.nextLong();
		scanner.nextLine();

		Student student = studentRepository.findById(studentId).orElse(null);
		Course course = courseRepository.findById(courseId).orElse(null);

		if (student == null || course == null) {
			System.out.println("EStudante ou curso não encontrado!");
			return;
		}

		Enrollment enrollment = new Enrollment();
		enrollment.setStudent(student);
		enrollment.setCourse(course);
		enrollment.setEnrollmentDate(LocalDate.now());
		enrollmentRepository.save(enrollment);
		System.out.println("Matrícula registrada com sucesso!");
	}

	private static void listEnrollments() {
		List<Enrollment> enrollments = enrollmentRepository.findAll();
		enrollments.forEach(e -> System.out.println("ID: " + e.getId() + ", Estudante: " + e.getStudent().getName() + ", Curso: " + e.getCourse().getTitle() + ", Data: " + e.getEnrollmentDate()));
	}

	private static void generateEngagementReport() {
		List<Course> courses = courseRepository.findAll();
		System.out.println("\n=== Relatório ===");
		for (Course course : courses) {
			List<Enrollment> enrollments = course.getEnrollments();
			long totalStudents = enrollments.size();

			double averageAge = enrollments.stream()
					.map(e -> Period.between(e.getStudent().getBirthDate(), LocalDate.now()).getYears())
					.mapToInt(Integer::intValue)
					.average()
					.orElse(0.0);

			long recentEnrollments = enrollmentRepository.findByCourseIdAndEnrollmentDateAfter(
					course.getId(), LocalDate.now().minusDays(30)).size();

			System.out.println("Curso: " + course.getTitle());
			System.out.println("Total de estudantes: " + totalStudents);
			System.out.printf("Idade média: %.2f years%n", averageAge);
			System.out.println("Novas matrículas nos últimos 30 dias: " + recentEnrollments);
			System.out.println("------------------------");
		}
	}
}