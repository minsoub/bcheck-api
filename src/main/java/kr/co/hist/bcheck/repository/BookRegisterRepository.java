package kr.co.hist.bcheck.repository;

import kr.co.hist.bcheck.entity.BookEntity;
import kr.co.hist.bcheck.entity.BookRegisterEntity;
import org.bson.types.ObjectId;
import org.springframework.data.jpa.domain.Specification;
//import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRegisterRepository extends MongoRepository<BookRegisterEntity, String> {
    BookRegisterEntity findByIsbn(String isbn);

    //@Query(value="select mt from MY_TABLE mt where mt.userid = %word% or mt.isbn = %word% or mt.info.title = %word%")
    //List<BookRegisterEntity> findAll(Specification<BookRegisterEntity> spec);

    //@Query("select mt from BookRegisterEntity mt where mt.userid LIKE %:word%")
    //@Query("{'isbn' : { '$regex' : ?0 , $options: 'i'}}")
    //List<BookRegisterEntity> findSearch(@Param("word") String word);

    //List<BookRegisterEntity> findSearch(Query query);


}
