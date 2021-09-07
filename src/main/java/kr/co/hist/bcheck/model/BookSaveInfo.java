package kr.co.hist.bcheck.model;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.hist.bcheck.entity.BookRegisterEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "저장된 도서 상세 정보")
public class BookSaveInfo {

    @Builder
    public BookSaveInfo(BookRegisterEntity entity) {
        this.id = entity.getId();
        this.userid = entity.getUserid();
        this.usernm = entity.getUsernm();
        this.title = entity.getInfo().getTitle();
        this.link = entity.getInfo().getLink();
        this.image = entity.getInfo().getImage();
        this.author = entity.getInfo().getAuthor();
        this.price = entity.getInfo().getPrice();
        this.discount = entity.getInfo().getDiscount();
        this.pubdate = entity.getInfo().getPubdate();
        this.publisher = entity.getInfo().getPublisher();
        this.description = entity.getInfo().getDescription();
        this.isbn = entity.getInfo().getIsbn();
    }
    @Schema(description = "Book ID", example = "6127101a81b25b13705157f9")
    private String id;

    @Schema(description = "사용자 아이디", example = "minsoub@gmail.com")
    private String userid;

    @Schema(description = "사용자명", example = "정민섭")
    private String usernm;

    @Schema(description = "책제목", example = "OKGOSU")
    private String title;

    @Schema(description = "링크정보", example = "http://book.naver.com/bookdb/book_detail.php?bid=6259425")
    private String link;

    @Schema(description = "이미지 정보", example = "https://bookthumb-phinf.pstatic.net/cover/062/594/06259425.jpg?type=m1&udate=20141122")
    private String image;

    @Schema(description = "작가", example = "정민섭")
    private String author;

    @Schema(description = "가격", example = "48000")
    private String price;

    @Schema(description = "할인가격", example = "43200")
    private String discount;

    @Schema(description = "발행인", example = "OKGOSU")
    private String publisher;

    @Schema(description = "발행일자", example = "20100419")
    private String pubdate;

    @Schema(description = "ISBN", example = "8960771295 9788960771291")
    private String isbn;

    @Schema(description = "설명", example = "OKGOSU")
    private String description;
}
