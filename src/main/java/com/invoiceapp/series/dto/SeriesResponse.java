package com.invoiceapp.series.dto;

public record SeriesResponse(
        Long id,
        Long companyId,

        String code,
        String description,
        boolean active,
        String prefix,
        String formatPattern,
        Integer paddingLength,

        DocumentTypeNestedResponse documentType,
        BranchNestedResponse branch,
        WhouseNestedResponse whouse
) {

    // Μικρό nested DTO μόνο με τα πεδία που μας νοιάζουν
    public record DocumentTypeNestedResponse(
            Long id,
            String code,
            String description
    ) {}

    public record BranchNestedResponse(
            Long id,
            String code,
            String name
    ) {}

    public record WhouseNestedResponse(
            Long id,
            String code,
            String name
    ) {}
}
