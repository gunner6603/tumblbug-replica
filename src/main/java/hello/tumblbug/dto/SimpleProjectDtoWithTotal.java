package hello.tumblbug.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
public class SimpleProjectDtoWithTotal {

    private List<SimpleProjectDto> dtos;

    private Long total;
}
