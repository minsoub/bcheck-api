package kr.co.hist.bcheck.controller;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.hist.bcheck.dto.*;
import kr.co.hist.bcheck.service.BookService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "[010] 도서 관리(도서 조회, 추가, 수정, 삭제)", description = "도서 추가,수정,삭제")
@RestController
@RequestMapping("/api/book")
@AllArgsConstructor
public class BookController {

    final BookService bookService;

    @Operation(description = "ISBN 검색")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "ISBN 검색을 통해 책 정보를 리턴한다",
                    content = @Content(schema = @Schema(implementation = IsbnResponse.class)))
    })
    @GetMapping(value="/isbn", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> isbnSearch(
           @Parameter(description = "ISBN No.", example="isbn-123456") @RequestParam(required = true) String isbn
    ) {
        return ResponseEntity.ok(bookService.search(isbn));
    }

    @Operation(description = "도서 상세 검색")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상세 검색을 통해 책 리스트 정보를 리턴한다",
                    content = @Content(schema = @Schema(implementation = IsbnResponse.class)))
    })
    @GetMapping(value="/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> search(
            @Parameter(description = "서적명", example="HEAD FIRST") @RequestParam(required = false) String book_name,
            @Parameter(description = "관리자명", example="정민섭") @RequestParam(required = false) String name
    ) {
        return ResponseEntity.ok(bookService.search(book_name, name));
    }

    @Operation(description = "도서 관리자 등록")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "도서 관리자 등록 결과를 리턴한다",
                    content = @Content(schema = @Schema(implementation = GeneralResponse.class)))
    })
    @PostMapping(value="/register", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> register(
            @RequestBody BookRegisterRequest request
    ) {
        return ResponseEntity.ok(bookService.register(request));
    }

    @Operation(description = "도서 관리자 변경")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "도서 관리자 등록 결과를 리턴한다",
                    content = @Content(schema = @Schema(implementation = GeneralResponse.class)))
    })
    @PostMapping(value="/change", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> change(
            @RequestBody BookRegisterChangeRequest request
    ) {
        return ResponseEntity.ok(bookService.change(request));
    }

    @Operation(description = "도서리스트 검색")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "검색을 통해 책 정보를 리턴한다",
                    content = @Content(schema = @Schema(implementation = BookListResponse.class)))
    })
    @GetMapping(value="/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> list(
            @Parameter(description = "검색단어", example="isbn-123456") @RequestParam(required = false) String word
    ) {
        return ResponseEntity.ok(bookService.list(word));
    }

    @Operation(description = "도서 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "ID를 통한 도서 삭제",
                    content = @Content(schema = @Schema(implementation = GeneralResponse.class)))
    })
    @DeleteMapping(value="/delete", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> delete(
            @Parameter(description = "도서ID", example="6125c0165b53fc0b29f10942") @RequestParam(required = true) String id,
            @Parameter(description = "삭제사유", example="3") @RequestParam(required = true) String del_type,
            @Parameter(description = "기타사유", example="기타사유입력") @RequestParam(required = false) String del_content,
            @Parameter(description = "사용자이디", example="mjoung@hist.co.kr") @RequestParam(required = true) String userid
    ) {
        return ResponseEntity.ok(bookService.delete(id, userid, del_type, del_content));
    }
}
