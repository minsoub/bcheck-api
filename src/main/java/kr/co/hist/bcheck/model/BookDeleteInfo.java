package kr.co.hist.bcheck.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
@Schema(description = "Book 삭제정보")
public class BookDeleteInfo {
    @Schema(description = "삭제일자", example = "123456789")
    private Date deldt;

    @Schema(description = "삭제 아이디", example = "minsoub@gmail.com")
    private String delid;

    @Schema(description = "삭제 사유", example = "3")
    private String del_type;

    @Schema(description = "기타 삭제 사유", example = "기타 삭제 사유 입력")
    private String del_content;
}
