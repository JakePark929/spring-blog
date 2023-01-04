package com.jake.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReplySaveRequestDto {
    private String username;
    private Long boardId;
    private String content;
}
