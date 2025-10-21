package xyz.goraebap.spring_progressive_demo.app.todo;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface TodoQueryMapper {

    /**
     * 동적 쿼리로 Todo 조회
     *
     * @param title       제목 검색어 (선택)
     * @param isCompleted 완료 여부 (선택)
     * @param cursor      커서 기준 생성일자 (선택)
     * @param sortOrder   정렬 순서 (ASC/DESC, 기본 DESC)
     * @param perPage     페이지당 조회 개수
     */
    List<Todo> findWithCursor(
            @Param("title") String title,
            @Param("isCompleted") Boolean isCompleted,
            @Param("cursor") LocalDateTime cursor,
            @Param("sortOrder") String sortOrder,
            @Param("perPage") Integer perPage
    );
}
