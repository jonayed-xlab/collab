package com.jbtech.collab.dto.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CategoryCount {
    private String category;
    private Long count;
}
