package kr.co.hist.bcheck.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.hist.bcheck.model.BookInfo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "도서관리자 등록정보")
public class BookRegisterRequest {
    @Schema(description = "ISBN", example = "9788960771291")
    private String isbn;

    @Schema(description = "관리자 아이디", example = "mjoung@hist.co.kr")
    private String userid;

    @Schema(description = "도서정보")
    private BookInfo bookinfo;
}
