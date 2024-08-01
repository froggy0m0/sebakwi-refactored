package com.froggy.sebakwi.checkupList.dto;

import static lombok.AccessLevel.PRIVATE;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
@Builder(access = PRIVATE)
public class ApiResponse<T> {

    private Long totalCount;
    private Integer totalPages;
    private List<T> checkupListArray;

    public static <T, R> ApiResponse<R> createResponse(Page<T> result, Function<T, R> converter) {

        List<R> content = result.getContent().stream()
            .map(converter)
            .collect(Collectors.toList());

        return ApiResponse.<R>builder()
            .totalCount(result.getTotalElements())
            .totalPages(result.getTotalPages())
            .checkupListArray(content)
            .build();
    }
}
