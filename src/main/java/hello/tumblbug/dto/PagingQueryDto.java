package hello.tumblbug.dto;

import lombok.Data;

@Data
public class PagingQueryDto {

    private int pageNum;

    private int limit;

    public int getOffset() {
        return (pageNum - 1) * limit;
    }
}
