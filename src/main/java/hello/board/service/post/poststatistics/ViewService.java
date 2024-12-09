package hello.board.service.post.poststatistics;

import hello.board.domain.post.PostStatistics;
import hello.board.repository.PostStatisticsRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class ViewService {

    private final PostStatisticsRepository postStatisticsRepository;
    private final RedisTemplate<String, String> redisTemplate;

    private static final String VIEW_COUNT_KEY_PREFIX = "post:viewed:"; // Redis 키 접두사

    @Transactional
    public void handleView(Long postId, HttpServletRequest request) {
        // Redis Key를 IP 또는 세션 기반으로 생성
        String clientIdentifier = getClientIdentifier(request);
        String redisKey = VIEW_COUNT_KEY_PREFIX + postId + ":" + clientIdentifier;

        // Redis에서 키 확인
        Boolean hasViewed = redisTemplate.hasKey(redisKey);

        if (Boolean.FALSE.equals(hasViewed)) {
            // 조회수 증가
            increaseViewCount(postId);

            // Redis에 키 저장 및 만료 시간 설정
            redisTemplate.opsForValue().set(redisKey, "true");
            redisTemplate.expire(redisKey, Duration.ofDays(1));
        }
    }

    private void increaseViewCount(Long postId) {
        PostStatistics statistics = postStatisticsRepository.findById(Long.valueOf(String.valueOf(postId)))
                .orElse(PostStatistics.builder()
                        .postId(String.valueOf(postId))
                        .viewCount(0)
                        .build());

        statistics.increaseViewCount();
        postStatisticsRepository.save(statistics);
    }

    private String getClientIdentifier(HttpServletRequest request) {
        String ipAddress = request.getRemoteAddr();
        String sessionId = request.getSession().getId();

        return ipAddress + ":" + sessionId;
    }

    public Integer getViewCount(Long postId) {
        return postStatisticsRepository.findById(Long.valueOf(String.valueOf(postId)))
                .map(PostStatistics::getViewCount)
                .orElse(0);
    }
}
