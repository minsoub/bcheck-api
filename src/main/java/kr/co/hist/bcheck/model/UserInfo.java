package kr.co.hist.bcheck.model;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.hist.bcheck.entity.UserEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "사용자 정보")
public class UserInfo {
    @Schema(description = "사용자 아이디", example = "minsoub@gmail.com")
    private String emp_id;

    @Schema(description = "사용자명", example = "정민섭")
    private String emp_nm;

    @Builder
    public UserInfo(UserEntity entity) {
        this.emp_id = entity.getEmp_id();
        this.emp_nm = entity.getEmp_nm();
    }
}
