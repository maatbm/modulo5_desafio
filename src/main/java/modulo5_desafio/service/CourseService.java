package modulo5_desafio.service;

import modulo5_desafio.model.Course;
import modulo5_desafio.repository.CourseRepository;
import org.springframework.stereotype.Service;

@Service
public class CourseService {
    private final CourseRepository courseRepository;
    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public String insertCourse(String title, String description, String duration) {
        try{
            int durationHours = Integer.parseInt(duration);
            if (title == null || title.isBlank() || description == null || description.isBlank() || durationHours <= 0) {
                return ("Todos os campos devem estar preenchidos corretamente. Tente novamente.");
            } else if (courseRepository.findByTitle(title).isPresent()) {
                return ("Já existe um curso registrado com este título. Tente novamente.");
            } else {
                Course course = new Course(title, description, durationHours);
                courseRepository.save(course);
                return ("Curso criado com sucesso!");
            }
        }catch (NumberFormatException e){
            return ("A duração do curso deve ser um número inteiro positivo. Tente novamente.");
        }catch (Exception e) {
            return ("Ocorreu um erro ao criar o curso. Tente novamente.");
        }
    }
}
