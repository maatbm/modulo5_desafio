package modulo5_desafio.service;

import modulo5_desafio.model.Course;
import modulo5_desafio.repository.CourseRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

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
            }else {
                Course course = new Course(title, description, durationHours);
                courseRepository.save(course);
                return ("Curso criado com sucesso!");
            }
        }catch (NumberFormatException e){
            return ("A duração do curso deve ser um número inteiro positivo. Tente novamente.");
        }catch (DataIntegrityViolationException e){
            return ("Já existe um curso registrado com este título. Tente novamente.");
        }catch (Exception e) {
            return ("Ocorreu um erro ao criar o curso. Tente novamente.");
        }
    }

    public String deleCourse(String id){
        try{
            Long courseId = Long.parseLong(id);
            boolean deleted = courseRepository.softDeleteCourse(courseId);
            if (deleted) {
                return ("Curso deletado com sucesso!");
            } else {
                return ("Curso não encontrado ou já deletado.");
            }
        }catch (NumberFormatException e){
            return ("O ID do curso deve ser um número inteiro positivo. Tente novamente.");
        }catch (Exception e){
            return ("Ocorreu um erro ao deletar o curso. Tente novamente.");
        }
    }

    public String restoreCourse(String id){
        try{
            Long courseId = Long.parseLong(id);
            boolean restored = courseRepository.restoreCourse(courseId);
            if (restored) {
                return ("Curso restaurado com sucesso!");
            } else {
                return ("Curso não encontrado ou já ativo.");
            }
        }catch (NumberFormatException e){
            return ("O ID do curso deve ser um número inteiro positivo. Tente novamente.");
        }catch (Exception e){
            return ("Ocorreu um erro ao restaurar o curso. Tente novamente.");
        }
    }

    public String updateCourse(String id, String title, String description, String duration) {
        try {
            Long courseId = Long.parseLong(id);
            int durationHours = Integer.parseInt(duration);
            if (title == null || title.isBlank() || description == null || description.isBlank() || durationHours <= 0) {
                return ("Todos os campos devem estar preenchidos corretamente. Tente novamente.");
            } else {
                boolean updated = courseRepository.updateCourse(courseId, title, description, durationHours);
                if (updated) {
                    return ("Curso atualizado com sucesso!");
                } else {
                    return ("Curso não encontrado ou não foi possível atualizar.");
                }
            }
        }catch (NumberFormatException e) {
            return ("O ID do curso e a duração devem ser números inteiros positivos. Tente novamente.");
        }catch (DataIntegrityViolationException e){
            return ("Já existe um curso registrado com este título. Tente novamente.");
        }catch (Exception e) {
            return ("Ocorreu um erro ao atualizar o curso. Tente novamente.");
        }
    }

    public List<Course> getActiveCourses() {
        try {
            return courseRepository.findActiveCourses();
        }catch (Exception e){
            return List.of();
        }
    }

    public List<Course> getDeletedCourses() {
        try {
            return courseRepository.findDeletedCourses();
        }catch (Exception e){
            return List.of();
        }
    }

    public List<Course> getAllCourses() {
        try {
            return courseRepository.findAll();
        }catch (Exception e){
            return List.of();
        }
    }

    public List<Course> getCourseByTitle(String title) {
        try {
            return courseRepository.findByTitleContainingIgnoreCase(title);
        } catch (Exception e) {
            return List.of();
        }
    }
}
