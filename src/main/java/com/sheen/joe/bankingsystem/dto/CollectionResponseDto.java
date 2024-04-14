package com.sheen.joe.bankingsystem.dto;

import java.util.List;

public record CollectionResponseDto<T extends CollectableDtoI>(
        List<T> content,
        int currentPage,
        int totalPages,
        long totalElements,
        boolean sorted
) {}
