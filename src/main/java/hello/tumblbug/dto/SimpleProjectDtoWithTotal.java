package hello.tumblbug.dto;

import lombok.Data;

import java.util.List;

@Data
public class SimpleProjectDtoWithTotal {

    private List<SimpleProjectDto> dtos;

    private Integer total;
}
