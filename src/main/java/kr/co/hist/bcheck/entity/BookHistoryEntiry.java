package kr.co.hist.bcheck.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document("bcheckbookhistory")
@Schema(description = "도서관리자 변경정보")
public class BookHistoryEntiry {
    @Id
    @Schema(description = "ID", example = "6125c0165b53fc0b29f10942")
    private String id;

    @Schema(description = "책정보 ID", example = "6125c0165b53fc0b29f10942")
    private String refid;

    @Schema(description = "사용자 아이디", example = "minsoub@gmail.com")
    private String userid;

    @Schema(description = "사용자명", example = "정민섭")
    private String usernm;

    @Schema(description = "등록일자", example = "123456789")
    private Date regdt;

    @Schema(description = "ISBN", example = "9788960771291")
    private String isbn;
}
