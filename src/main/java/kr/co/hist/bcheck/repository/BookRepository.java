package kr.co.hist.bcheck.repository;

import kr.co.hist.bcheck.entity.BookEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends MongoRepository<BookEntity, String> {

    List<BookEntity> findByIsbn(String isbn);
}