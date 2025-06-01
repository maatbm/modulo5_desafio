package modulo5_desafio.DTO;

public record EngagementReportDTO(
        Long courseId,
        String courseTitle,
        Long totalEnrollments,
        Double averageAge,
        Long recentEnrollments
){}
