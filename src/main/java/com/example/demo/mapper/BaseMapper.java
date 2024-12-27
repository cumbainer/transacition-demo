package com.example.demo.mapper;

import java.util.List;

public interface BaseMapper<Domain, DTO> {
    Domain toDomain(DTO dto);
    DTO toDto(Domain domain);
    List<Domain> toDomains(List<DTO> dtos);
    List<DTO> toDtos(List<Domain> domains);
}
