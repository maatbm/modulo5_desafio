package modulo5_desafio;

public enum MenuOption {
    REGISTRAR_ALUNO(1, "Registrar novo aluno",Modulo5DesafioApplication::registerStudent),
    LISTAR_ALUNOS(2, "Listar alunos ativos",Modulo5DesafioApplication::listStudents),
    BUSCAR_ALUNO_EMAIL(3,"Buscar aluno por email", Modulo5DesafioApplication::findStudentByEmail),
    REGISTRAR_CURSO(4,"Registrar novo curso", Modulo5DesafioApplication::registerCourse),
    LISTAR_CURSOS(5,"Listar cursos ativos", Modulo5DesafioApplication::listCourses),
    BUSCAR_CURSO_TITULO(6,"Buscar curso por título", Modulo5DesafioApplication::findCourseByTitle),
    REGISTRAR_MATRICULA(7, "Registrar nova matrícula",Modulo5DesafioApplication::registerEnrollment),
    LISTAR_MATRICULAS(8, "Listar matrículas ativas",Modulo5DesafioApplication::listEnrollments),
    RELATORIO_ENGAJAMENTO(9,"Relatório de engajamento", Modulo5DesafioApplication::generateEngagementReport);

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
