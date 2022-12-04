package hello.tumblbug.dto;

import lombok.Data;

import java.util.List;

@Data
public class SimpleProjectDtosWithTotal {

    private List<SimpleProjectDto> dtos;

    private Long total;
}
