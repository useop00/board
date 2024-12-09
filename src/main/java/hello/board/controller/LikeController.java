package hello.board.controller;

import hello.board.service.post.poststatistics.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/likes")
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/{postId}")
    public ResponseEntity<Map<String, Object>> toggleLike(@PathVariable Long postId, Principal principal) {
        String username = principal.getName();
        int updatedLikeCount = likeService.likePost(postId, username);

        Map<String, Object> response = new HashMap<>();
        response.put("likeCount", updatedLikeCount);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/{postId}/count")
    public ResponseEntity<Long> getLikeCount(@PathVariable Long postId) {
        Long likeCount = likeService.getLikeCount(postId);
        return ResponseEntity.ok(likeCount);
    }
}
