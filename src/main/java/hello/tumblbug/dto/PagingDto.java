package hello.tumblbug.dto;

import lombok.Data;

import java.util.List;

@Data
public class PagingDto<T> {

    private int pageNum;
    private int limit;
    private long total;
    private List<T> elements;

    private int startPage;
    private int endPage;
    private int totalPage;

    public PagingDto(int pageNum, int limit, long total, List<T> elements) {
        this.pageNum = pageNum;
        this.limit = limit;
        this.total = total;
        this.elements = elements;

        totalPage = (int) Math.ceil((double) total / limit);
        endPage = (int) Math.ceil(pageNum / 10.0) * 10;
        startPage = endPage - 9;
        if (totalPage < endPage) {
            endPage = totalPage;
        }
    }

    public boolean hasPrev() {
        return startPage > 1;
    }

    public boolean hasNext() {
        return endPage < totalPage;
    }
}
