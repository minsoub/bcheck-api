package kr.co.hist.bcheck.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.hist.bcheck.model.BookSaveInfo;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Schema(description = "도서 리스트 정보")
public class BookListResponse {
    @Schema(description = "Book 리스트 정보")
    List<BookSaveInfo> list;
}
