package com.practice.efubaccount.post.dto.response;

import com.practice.efubaccount.post.dto.summary.PostSummary;
import org.springframework.data.domain.Page;

import java.util.List;

public record PostPageResponse(
        List<PostSummary> posts,
        int currentPage,
        int pageSize,
        int totalPages,
        long totalPosts,
        boolean hasNext
) {
    public static PostPageResponse from(Page<PostSummary> page) {
        return new PostPageResponse(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalPages(),
                page.getTotalElements(),
                page.hasNext()
        );
    }
}
