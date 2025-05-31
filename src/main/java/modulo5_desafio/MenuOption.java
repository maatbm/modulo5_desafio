package modulo5_desafio;

public enum MenuOption {
    // Estudantes
    REGISTER_STUDENT(1, "Registrar aluno", Modulo5DesafioApplication::registerStudent),
    LIST_ACTIVE_STUDENTS(2, "Listar alunos ativos", Modulo5DesafioApplication::listActiveStudents),
    LIST_DELETED_STUDENTS(3, "Listar alunos deletados", Modulo5DesafioApplication::listDeletedStudents),
    LIST_ALL_STUDENTS(4, "Listar todos os alunos", Modulo5DesafioApplication::listAllStudents),
    DELETE_STUDENT(5, "Deletar aluno", Modulo5DesafioApplication::deleteStudent),
    RESTORE_STUDENT(6, "Restaurar aluno", Modulo5DesafioApplication::restoreStudent),
    UPDATE_STUDENT(7, "Atualizar aluno", Modulo5DesafioApplication::updateStudent),
    FIND_STUDENT_BY_EMAIL(8, "Buscar aluno por email", Modulo5DesafioApplication::findStudentByEmail),

    // Cursos
    REGISTER_COURSE(9, "Registrar curso", Modulo5DesafioApplication::registerCourse),
    LIST_ACTIVE_COURSES(10, "Listar cursos ativos", Modulo5DesafioApplication::listActiveCourses),
    LIST_DELETED_COURSES(11, "Listar cursos deletados", Modulo5DesafioApplication::listDeletedCourses),
    LIST_ALL_COURSES(12, "Listar todos os cursos", Modulo5DesafioApplication::listAllCourses),
    DELETE_COURSE(13, "Deletar curso", Modulo5DesafioApplication::deleteCourse),
    RESTORE_COURSE(14, "Restaurar curso", Modulo5DesafioApplication::restoreCourse),
    UPDATE_COURSE(15, "Atualizar curso", Modulo5DesafioApplication::updateCourse),
    FIND_COURSE_BY_TITLE(16, "Buscar curso por título", Modulo5DesafioApplication::findCourseByTitle),

    // Matrículas
    REGISTER_ENROLLMENT(17, "Registrar matrícula", Modulo5DesafioApplication::registerEnrollment),
    LIST_ACTIVE_ENROLLMENTS(18, "Listar matrículas ativas", Modulo5DesafioApplication::listActiveEnrollments),
    LIST_DELETED_ENROLLMENTS(19, "Listar matrículas deletadas", Modulo5DesafioApplication::listDeletedEnrollments),
    LIST_ALL_ENROLLMENTS(20, "Listar todas as matrículas", Modulo5DesafioApplication::listAllEnrollments),
    DELETE_ENROLLMENT(21, "Deletar matrícula", Modulo5DesafioApplication::deleteEnrollment),
    RESTORE_ENROLLMENT(22, "Restaurar matrícula", Modulo5DesafioApplication::restoreEnrollment),

    // Relatórios
    GENERATE_ENGAGEMENT_REPORT(23, "Gerar relatório de engajamento", Modulo5DesafioApplication::generateEngagementReport);

    public final int code;
    public final String label;
    public final Runnable action;

    MenuOption(int code, String label, Runnable action) {
        this.code = code;
        this.label = label;
        this.action = action;
    }

    public static MenuOption fromCode(int code) {
        for (MenuOption option : values()) {
            if (option.code == code) return option;
        }
        return null;
    }
}
