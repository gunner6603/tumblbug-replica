package hello.tumblbug.domain;

import lombok.Getter;

@Getter
public enum Category {

    BOOK("도서"), FILM("영화"), GOODS("굿즈");

    private final String description;

    private Category(String description) {
        this.description = description;
    }
}
