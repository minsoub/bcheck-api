package kr.co.hist.bcheck.repository;

import kr.co.hist.bcheck.entity.BookHistoryEntiry;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRegisterHistoryRepository   extends MongoRepository<BookHistoryEntiry, String> {
    List<BookHistoryEntiry> findByRefid(String refid);
}
