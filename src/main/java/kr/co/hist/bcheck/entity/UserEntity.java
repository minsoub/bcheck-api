package kr.co.hist.bcheck.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document("bcheckemps")
@Schema(description = "User info")
public class UserEntity {
    @Id
    @Schema(description = "Object ID", example = "61270b8379833b136fe7bffb")
    private String id;

    @Schema(description = "회사코드", example = "HX")
    private String cmpny_code;

    @Schema(description = "등록일자", example = "20210223135334")
    private String regist_dtm;

    @Schema(description = "시작일자", example = "20210201")
    private String start_dt;

    @Schema(description = "수정일시", example = "20210218084558")
    private String updt_dtm;

    @Schema(description = "사용자ID", example = "mjoung@hist.co.kr")
    private String emp_id;

    @Schema(description = "등록자아이디", example = "mjoung@hist.co.kr")
    private String regist_emp_id;

    @Schema(description = "수정자아이디", example = "mjoung@hist.co.kr")
    private String updt_emp_id;

    @Schema(description = "직원명", example = "정민섭")
    private String emp_nm;

    @Schema(description = "패스워드", example = "xxxxxxxx")
    private String emp_password;

    @Schema(description = "직원 Role", example = "")
    private String emp_role_flag;

    @Schema(description = "전화번호", example = "01012345678")
    private String emp_tel_no;

    @Schema(description = "종료일자", example = "20211231")
    private String end_dt;

}
