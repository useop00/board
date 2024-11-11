package hello.board.domain.post;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostSearch {
    private String keyword = "";
    private String searchField = "";

    public PostSearch(String keyword, String searchField) {
        this.keyword = (keyword != null) ? keyword : "";
        this.searchField = (searchField != null) ? searchField : "";
    }
}
