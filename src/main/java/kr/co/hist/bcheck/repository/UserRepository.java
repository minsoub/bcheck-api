package kr.co.hist.bcheck.repository;

import kr.co.hist.bcheck.entity.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<UserEntity, String> {
    @Query("{ 'emp_id' : ?0 }")
    UserEntity findByEmp_id(String emp_id);
}
