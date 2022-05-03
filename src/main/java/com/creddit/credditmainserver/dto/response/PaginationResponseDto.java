package com.creddit.credditmainserver.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaginationResponseDto<T> {
    private int totalPage;
    private Boolean hasNext;
    private T data;
}
