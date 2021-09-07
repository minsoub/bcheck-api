package kr.co.hist.bcheck.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.hist.bcheck.dto.IsbnResponse;
import kr.co.hist.bcheck.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "[020] 사용자 관리(팝업정보 제공)", description = "사용자 리스트 팝업정보 제공")
@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
public class UserController {

    final UserService service;

    @Operation(description = "사용자 검색")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자 검색을 통해 사용자 정보를 리턴한다",
                    content = @Content(schema = @Schema(implementation = IsbnResponse.class)))
    })
    @GetMapping(value="/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> search(
            @Parameter(description = "검색단어", example="mjoung@hist.co.kr") @RequestParam(required = false) String word
    ) {
        return ResponseEntity.ok(service.search(word));
    }
}
