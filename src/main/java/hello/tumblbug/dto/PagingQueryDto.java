package hello.tumblbug.dto;

import lombok.Data;

@Data
public class PagingQueryDto {

    private int pageNum = 1;

    private int limit = 16;

    public int getOffset() {
        return (pageNum - 1) * limit;
    }
}
