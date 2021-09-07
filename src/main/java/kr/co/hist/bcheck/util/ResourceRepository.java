package kr.co.hist.bcheck.util;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;

//@NoRepositoryBean
public interface ResourceRepository  { // <T, I extends Serializable> extends MongoRepository<T, I> {
   // List<T> findAll(Query query);
}
