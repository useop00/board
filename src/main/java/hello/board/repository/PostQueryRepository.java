package hello.board.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import hello.board.domain.post.MainPostDto;
import hello.board.domain.post.PostSearch;
import hello.board.domain.post.QMainPostDto;
import hello.board.domain.post.QPost;
import hello.board.domain.user.QUser;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PostQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public PostQueryRepository(EntityManager em) {
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    public Page<MainPostDto> searchPosts(PostSearch search, Pageable pageable) {
        BooleanExpression predicate = createPredicate(search);

        List<MainPostDto> content = fetchContent(pageable, predicate);
        long total = fetchTotalCount(predicate);

        return new PageImpl<>(content, pageable, total);
    }

    private List<MainPostDto> fetchContent(Pageable pageable, BooleanExpression predicate) {
        QPost post = QPost.post;
        QUser user = QUser.user;

        // 페이징된 결과 조회
        return jpaQueryFactory
                .select(new QMainPostDto(
                        post.id,
                        user.username,
                        user.nickname,
                        post.title,
                        post.content,
                        post.createDateTime
                ))
                .from(post)
                .leftJoin(post.user, user)
                .where(predicate)
                .orderBy(post.createDateTime.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }


    private long fetchTotalCount(BooleanExpression predicate) {
        QPost post = QPost.post;

        return jpaQueryFactory
                .select(post.count())
                .from(post)
                .where(predicate)
                .fetchOne();
    }


    private BooleanExpression createPredicate(PostSearch search) {
        BooleanExpression predicate = null;

        if ("title".equals(search.getSearchField())) {
            predicate = QPost.post.title.containsIgnoreCase(search.getKeyword());
        } else if ("content".equals(search.getSearchField())) {
            predicate = QPost.post.content.containsIgnoreCase(search.getKeyword());
        } else if ("nickname".equals(search.getSearchField())) {
            predicate = QPost.post.user.nickname.containsIgnoreCase(search.getKeyword());
        }

        return predicate;
    }
}
