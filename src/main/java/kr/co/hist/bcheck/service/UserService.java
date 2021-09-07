package kr.co.hist.bcheck.service;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import kr.co.hist.bcheck.dto.UserListResponse;
import kr.co.hist.bcheck.entity.UserEntity;
import kr.co.hist.bcheck.model.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 사용자 팝업 정보 제공을 위한 서비스 클래스
 * 검색 조건으로 사용자를 조회해서 리턴한다. (사용자아이디 or 성명)
 */
@XRayEnabled
@Slf4j
@Service
public class UserService {
    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 사용자 성명 또는 아이디로 직원을 검색해서 리스트로 리턴한다.
     *
     * @param word
     * @return
     */
    public UserListResponse search(String word) {
        UserListResponse res = new UserListResponse();

        try {
            Criteria criteria = new Criteria();
            criteria.orOperator(Criteria.where("emp_id").regex(word),
                    Criteria.where("emp_nm").regex(word));
            Query query = new Query(criteria);

            List<UserEntity> resultList = mongoTemplate.find(query, UserEntity.class);
            if (resultList == null) {
                return null;
            }
            List<UserInfo> list = new ArrayList<UserInfo>();
            for (UserEntity entity : resultList) {
                UserInfo info = UserInfo.builder().entity(entity).build();
                list.add(info);
            }
            res.setList(list);
        }catch(Exception ex) {
            log.debug(ex.toString());
            res = null;
        }
        return res;
    }
}
