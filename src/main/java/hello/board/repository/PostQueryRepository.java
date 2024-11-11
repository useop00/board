package hello.board.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import hello.board.domain.post.*;
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
        QPost post = QPost.post;
        QUser user = QUser.user;

        BooleanExpression predicate = createPredicate(search);

        // 페이징된 결과 조회
        List<MainPostDto> content = jpaQueryFactory
                .select(new QMainPostDto(
                        post.id,
                        user.username,
                        user.nickname,
                        post.title,
                        post.content,
                        post.postDate
                ))
                .from(post)
                .leftJoin(post.user, user)
                .where(predicate)
                .orderBy(post.postDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 전체 게시글 수 조회
        long total = jpaQueryFactory
                .select(post.count())
                .from(post)
                .where(predicate)
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
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
