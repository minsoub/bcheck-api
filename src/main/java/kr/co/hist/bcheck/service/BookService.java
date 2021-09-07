package kr.co.hist.bcheck.service;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.hist.bcheck.dto.*;
import kr.co.hist.bcheck.entity.BookEntity;
import kr.co.hist.bcheck.entity.BookHistoryEntiry;
import kr.co.hist.bcheck.entity.UserEntity;
import kr.co.hist.bcheck.model.BookDeleteInfo;
import kr.co.hist.bcheck.model.BookInfo;
import kr.co.hist.bcheck.entity.BookRegisterEntity;
import kr.co.hist.bcheck.model.BookSaveInfo;
import kr.co.hist.bcheck.repository.BookRegisterHistoryRepository;
import kr.co.hist.bcheck.repository.BookRegisterRepository;
import kr.co.hist.bcheck.repository.BookRepository;
import kr.co.hist.bcheck.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@XRayEnabled
@Slf4j
@Service
public class BookService {

    @Value("${openapi.naver.url}")
    String naverURL;

    @Value("${openapi.naver.clientID}")
    String clientID;

    @Value("${openapi.naver.clientSecret}")
    String clientSecret;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private BookRepository repo;

    @Autowired
    private BookRegisterRepository registerRepo;

    @Autowired
    private BookRegisterHistoryRepository historyRepo;

    @Autowired
    UserRepository userRepository;

    //@Autowired
    //private FilterBuilderService filterBuilderService;

    /**
     * 외부 연계를 통해서 ISBN 의 책 정보를 가져온다.
     * @param isbn
     * @return
     */
    public IsbnResponse search(String isbn)
    {
        //get /v1/search/book_adv
        //HOST: openapi.naver.com
        //Content-Type: plain/text
        //X-Naver-Client-Id: 8LHwtR6GWztI7XY6_zt1
        //X-Naver-Client-Secret: qJ8C9_odvW
        try {
            String url = naverURL+"?d_isbn="+isbn;
            log.debug(url);
            CloseableHttpClient client = HttpClientBuilder.create().build();
            HttpGet req = new HttpGet(url);
            req.addHeader("X-Naver-Client-Id", clientID);
            req.addHeader("X-Naver-Client-Secret", clientSecret);

            HttpResponse res = client.execute(req);
            if (res.getStatusLine().getStatusCode() == 200) {
                ResponseHandler<String> handler = new BasicResponseHandler();
                String body = handler.handleResponse(res);
                System.out.println(body);
                //Gson gsonObj = new Gson();

                JSONObject obj = new JSONObject(body);
                if (obj.getInt("total") == 0) {   // 조회 도서가 없다면..
                    return null;
                }else {
                    JSONArray array = obj.getJSONArray("items");

                    JSONObject data = array.getJSONObject(0);  // 첫번째 정보
                    ObjectMapper mapper = new ObjectMapper();

                    log.debug(data.toString());
                    IsbnResponse isbnRes = mapper.readValue(data.toString(), IsbnResponse.class);
                    log.debug(isbnRes.toString());

                    // History 등록
                    BookEntity entity = new BookEntity();
                    BookInfo info = mapper.readValue(data.toString(), BookInfo.class);
                    entity.setInfo(info);
                    entity.setUserid("test");
                    entity.setSearchdt(new Date());
                    entity.setIsbn(info.getIsbn());
                    repo.insert(entity);

                    return isbnRes;
                }
            }else {
                System.out.println("response is error => " + res.getStatusLine().getStatusCode());
            }
        }catch(Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }


    /**
     * 도서 상세 검색을 통해서 리스트 정보를 조회한다.
     *
     * @param book_name  도서명
     * @param name       관리자명
     * @return
     */
    public BookListResponse search(String book_name, String name) {

        BookListResponse res = new BookListResponse();

        try {
            Criteria criteria = new Criteria();

            if (book_name != null && name != null) {
                criteria.andOperator(
                        Criteria.where("delYn").is("N"),
                        Criteria.where("usernm").regex(name),
                        Criteria.where("info.title").regex(book_name)
                );
            }else if (book_name != null && name == null) {
                criteria.andOperator(
                        Criteria.where("delYn").is("N"),
                        Criteria.where("info.title").regex(book_name)
                );
            }else if(book_name == null && name != null) {
                criteria.andOperator(
                        Criteria.where("delYn").is("N"),
                        Criteria.where("usernm").regex(name)
                );
            }else {
                criteria.andOperator(
                        Criteria.where("delYn").is("N")
                );
            }

            Query query = new Query(criteria);
            List<BookRegisterEntity> resultList = mongoTemplate.find(query, BookRegisterEntity.class);

            if (resultList == null) {
                return null;
            }
            List<BookSaveInfo> result = new ArrayList<BookSaveInfo>();

            for (BookRegisterEntity data: resultList)
            {
                if (data.getInfo() == null) continue;
                BookSaveInfo info = BookSaveInfo.builder().entity(data).build();
                result.add(info);
            }
            res.setList(result);

            return res;
        }catch(Exception ex) {
            ex.printStackTrace();
            log.debug(ex.toString());
            return null;
        }
    }

    /**
     * 도서 관리자를 등록한다.
     *
     * @param request
     * @return
     */
    public GeneralResponse register(BookRegisterRequest request) {

        BookRegisterEntity entity = new BookRegisterEntity();
        GeneralResponse res = new GeneralResponse();

        try {
            //BookRegisterEntity result = registerRepo.findByIsbn(request.getIsbn());
            //System.out.println("isbn => " + request.getIsbn());
            //System.out.println(result);
            //if (result != null) {
            //    res.setStatus(-9);
            //    res.setMessage("이미 도서정보 관리자가 등록되어있습니다");
            //    return res;
            //}
            log.debug("search userid => " + request.getUserid());
            UserEntity user = userRepository.findByEmp_id(request.getUserid());

            BookInfo info = request.getBookinfo();
            entity.setInfo(info);
            entity.setUserid(request.getUserid());
            entity.setUsernm(user.getEmp_nm());
            entity.setRegdt(new Date());
            entity.setIsbn(request.getIsbn());
            entity.setDelYn("N");
            registerRepo.insert(entity);

            log.debug("objectid => " + entity.getId());

            // 관리자 History 등록
            BookHistoryEntiry his = new BookHistoryEntiry();
            his.setRefid(entity.getId());
            his.setUsernm(user.getEmp_nm());
            his.setUserid(request.getUserid());
            his.setRegdt(new Date());
            his.setIsbn(request.getIsbn());
            historyRepo.insert(his);

            res.setStatus(0);
            res.setMessage("성공적으로 등록하였습니다!!!");
        }catch(Exception ex) {
            ex.printStackTrace();
            log.debug(ex.toString());
            res.setStatus(-1);
            res.setMessage(ex.toString());
        }
        return res;
    }

    /**
     * 도서 관리자를 변경한다
     *
     * @param request
     * @return
     */
    public GeneralResponse change(BookRegisterChangeRequest request) {

        BookRegisterEntity entity = new BookRegisterEntity();
        GeneralResponse res = new GeneralResponse();

        try {

            BookRegisterEntity result = registerRepo.findById(request.getId()).orElse(null);
            //log.debug(result);
            System.out.println("isbn => " + request.getIsbn());
            System.out.println(result);
            if (result == null) {
                res.setStatus(-9);
                res.setMessage("도서정보 관리자가 등록되지 않았습니다");
                return res;
            }
            UserEntity user = userRepository.findByEmp_id(request.getChange_userid());

            result.setUserid(request.getChange_userid());
            result.setUsernm(user.getEmp_nm());
            registerRepo.save(result);   // 수정

            // 관리자 History 등록
            BookHistoryEntiry his = new BookHistoryEntiry();
            his.setRefid(request.getId());
            his.setUserid(request.getChange_userid());
            his.setUsernm(user.getEmp_nm());
            his.setRegdt(new Date());
            his.setIsbn(request.getIsbn());
            historyRepo.insert(his);

            res.setStatus(0);
            res.setMessage("성공적으로 변경하였습니다!!!");
        }catch(Exception ex) {
            res.setStatus(-1);
            res.setMessage(ex.toString());
        }
        return res;
    }

    /**
     * 등록된 도서를 삭제한다.
     * 도서 삭제시 이력 내역도 같이 삭제한다.
     *
     * @param id
     * @param userid
     * @param del_type
     * @param del_content
     * @return
     */
    public GeneralResponse delete(String id, String userid, String del_type, String del_content) {
        BookRegisterEntity entity = new BookRegisterEntity();
        GeneralResponse res = new GeneralResponse();

        try {
            BookRegisterEntity result = registerRepo.findById(id).orElse(null);
            if (result != null) {
//                // History 내역 삭제
//                List<BookHistoryEntiry> list = historyRepo.findByRefid(id);
//
//                if (list != null) {
//                    historyRepo.deleteAll(list);
//                }
                BookDeleteInfo info = new BookDeleteInfo();
                info.setDel_type(del_type);
                info.setDel_content(del_content);
                info.setDeldt(new Date());
                info.setDelid(userid);

                result.setDelYn("Y");
                result.setDelInfo(info);
                registerRepo.save(result);
                res.setStatus(0);
                res.setMessage("성공적으로 삭제 하였습니다!!!");
            }else {
                res.setStatus(-1);
                res.setMessage("삭제할 데이터가 없습니다!!!");
            }
        }catch(Exception ex) {
            res.setStatus(-1);
            res.setMessage(ex.toString());
        }
        return res;
    }

    /**
     * 도서 정보를 조회한다.
     *
     * @param word
     * @return
     */
    public BookListResponse list(String word) {

        BookListResponse res = new BookListResponse();

        try {
            Criteria criteria = new Criteria();
            if (word != null) {
                criteria.orOperator(Criteria.where("userid").regex(word),
                        Criteria.where("isbn").regex(word),
                        Criteria.where("info.title").regex(word));
            }
            criteria.andOperator(Criteria.where("delYn").is("N"));
            Query query = new Query(criteria);
            List<BookRegisterEntity> resultList = mongoTemplate.find(query, BookRegisterEntity.class);

            if (resultList == null) {
                return null;
            }
            List<BookSaveInfo> result = new ArrayList<BookSaveInfo>();

            for (BookRegisterEntity data: resultList)
            {
                System.out.println(data.toString());
                if (data.getInfo() == null) continue;
                BookSaveInfo info = BookSaveInfo.builder().entity(data).build();
                result.add(info);
            }
            res.setList(result);

            return res;
        }catch(Exception ex) {
            ex.printStackTrace();
            log.debug(ex.toString());
            return null;
        }
    }



    public static Specification<BookRegisterEntity> containsTextInName(String text) {
        if (!text.contains("%")) {
            text = "%" + text + "%";
        }
        String finalText = text;
        return (root, query, builder) -> builder.or(
                builder.like(root.get("userid"), finalText),
                builder.like(root.get("isbn"), finalText)
        );
    }
}
