package hello.board.domain.post;

import lombok.Getter;

import java.util.Objects;

@Getter
public class PostSearch {

    private final String keyword;
    private final String searchField;

    public PostSearch(String keyword, String searchField) {
        this.keyword = Objects.requireNonNullElse(keyword, "");
        this.searchField = Objects.requireNonNullElse(searchField, "");
    }
}
