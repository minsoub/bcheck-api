package kr.co.hist.bcheck.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.hist.bcheck.model.UserInfo;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Schema(description = "User 리스트 정보")
public class UserListResponse {
    @Schema(description = "User 리스트 정보")
    List<UserInfo> list;
}
