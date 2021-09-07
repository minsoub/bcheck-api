package kr.co.hist.bcheck.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "도서관리자 변경정보")
public class BookRegisterChangeRequest {
    @Schema(description = "도서 ID", example = "6125c0165b53fc0b29f10942")
    private String id;

    @Schema(description = "ISBN", example = "9788960771291")
    private String isbn;

    @Schema(description = "관리자로 등록된 아이디", example = "mjoung@hist.co.kr")
    private String userid;

    @Schema(description = "관리자로 변경할 아이디")
    private String change_userid;
}
