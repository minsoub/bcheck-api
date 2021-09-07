package kr.co.hist.bcheck.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.hist.bcheck.model.BookInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Document("isbnhistory")
@Schema(description = "ISBN Search")
public class BookEntity {
    @Id
    @Schema(description = "ID", example = "61270b8379833b136fe7bffb")
    private String id;

    @Schema(description = "사용자 아이디", example = "minsoub@gmail.com")
    private String userid;

    @Schema(description = "조회일자", example = "123456789")
    private Date searchdt;

    @Schema(description = "ISBN", example = "9788960771291")
    private String isbn;

    @Schema(description = "Book 상세 정보")
    private BookInfo info;
}
