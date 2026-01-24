package xyz.goraebap.blog.infra.service;

import lombok.RequiredArgsConstructor;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.springframework.stereotype.Service;
import xyz.goraebap.blog.infra.view_model.AdminCommentViewModel;
import xyz.goraebap.blog.infra.view_model.CommentViewModel;
import xyz.goraebap.blog.infra.view_model.Pagination;

import java.util.List;

import static jooq.Tables.COMMENTS;
import static jooq.Tables.POSTS;
import static org.jooq.impl.DSL.noCondition;

/**
 * 댓글 조회 서비스
 * - 클라이언트용 댓글 목록 조회
 * - 관리자 페이지용 댓글 목록 조회
 * - JOOQ DSL을 사용한 타입 안전 쿼리
 */
@Service
@RequiredArgsConstructor
public class CommentQueryService {

    private final DSLContext dsl;

    /**
     * 클라이언트용 댓글 목록 조회
     * @param postSlug 게시물 슬러그
     * @return 삭제되지 않은 댓글 목록 (생성일시 오름차순)
     */
    public List<CommentViewModel> getCommentsByPostSlug(String postSlug) {
        return dsl.select(
                        COMMENTS.ID,
                        COMMENTS.NICKNAME,
                        COMMENTS.COMMENT,
                        COMMENTS.AVATAR_NO,
                        COMMENTS.CREATED_AT
                )
                .from(COMMENTS)
                .innerJoin(POSTS).on(POSTS.ID.eq(COMMENTS.POST_ID))
                .where(POSTS.SLUG.eq(postSlug))
                .and(COMMENTS.DELETED_AT.isNull())
                .orderBy(COMMENTS.CREATED_AT.asc())
                .fetchInto(CommentViewModel.class);
    }

    /**
     * 관리자용 댓글 목록 조회 (페이지네이션)
     * @param search 검색어 (닉네임 또는 댓글 내용)
     * @param page 페이지 번호
     * @param size 페이지당 항목 수
     * @return 페이지네이션된 댓글 목록
     */
    public Pagination<AdminCommentViewModel> getAdminCommentsWithPagination(
            String search,
            int page,
            int size
    ) {
        int offset = (page - 1) * size;

        // 검색 조건: 닉네임 또는 댓글 내용에서 부분 일치 (대소문자 무시)
        Condition searchFilter = noCondition();
        if (search != null && !search.isEmpty()) {
            searchFilter = COMMENTS.NICKNAME.likeIgnoreCase("%" + search + "%")
                    .or(COMMENTS.COMMENT.likeIgnoreCase("%" + search + "%"));
        }

        // 댓글 목록 조회 (게시물 정보 포함)
        var items = dsl.select(
                        COMMENTS.ID,
                        COMMENTS.NICKNAME,
                        COMMENTS.COMMENT,
                        COMMENTS.AVATAR_NO,
                        COMMENTS.CREATED_AT,
                        COMMENTS.DELETED_AT,
                        COMMENTS.POST_ID,
                        POSTS.TITLE.as("post_title"),
                        POSTS.SLUG.as("post_slug"),
                        POSTS.POST_TYPE.as("post_type")
                )
                .from(COMMENTS)
                .leftJoin(POSTS).on(POSTS.ID.eq(COMMENTS.POST_ID))
                .where(COMMENTS.DELETED_AT.isNull())
                .and(searchFilter)
                .orderBy(COMMENTS.CREATED_AT.desc())
                .limit(size)
                .offset(offset)
                .fetchInto(AdminCommentViewModel.class);

        // 전체 개수 조회 (페이지네이션용)
        Condition countFilter = COMMENTS.DELETED_AT.isNull().and(searchFilter);
        int totalCount = dsl.fetchCount(COMMENTS, countFilter);

        return Pagination.of(items, page, totalCount, "desc", size);
    }
}
