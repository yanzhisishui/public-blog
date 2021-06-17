package com.syc.blog.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.syc.blog.constants.Constant;
import com.syc.blog.constants.GlobalConstant;
import com.syc.blog.constants.RedisConstant;
import com.syc.blog.entity.article.Article;
import com.syc.blog.entity.article.ArticleClassify;
import com.syc.blog.entity.comment.UserComment;
import com.syc.blog.entity.info.*;
import com.syc.blog.repository.ArticleRepository;
import com.syc.blog.service.article.ArticleClassifyService;
import com.syc.blog.service.article.ArticleService;
import com.syc.blog.service.comment.UserCommentService;
import com.syc.blog.service.info.*;
import com.syc.blog.utils.DateHelper;
import com.syc.blog.utils.StringHelper;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Controller
public class IndexController extends BaseController{

    @Autowired
    ArticleRepository articleRepository;
    @Autowired
    BannerService bannerService;
    @Autowired
    NoticeService noticeService;
    @Autowired
    OnlineUtilsService onlineUtilsService;
    @Autowired
    FriendLinkService friendLinkService;
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    ArticleClassifyService articleClassifyService;
    @Autowired
    ArticleService articleService;
    @Autowired
    UserCommentService userCommentService;
    @Autowired
    MicroDiaryService microDiaryService;
    @Autowired
    RedisTemplate<String,Object> redisTemplate;

    @RequestMapping("/")
    public String index(ModelMap map, HttpServletRequest request,@RequestParam(value = "page",required = false,defaultValue = "1") Integer page){

        Map<String,Object> params = new HashMap<>();
        params.put("page",page);
        List<Article> articleList = new ArrayList<>();
        org.springframework.data.domain.Page<Article> articles = queryArticle(params);
        articles.get().forEach(article -> {
            Object o = redisTemplate.opsForHash().get(RedisConstant.ARTICLE_PRAISE_COUNT, article.getId().toString());
            if(o != null){
                article.setPraise(article.getPraise()+(Integer) o);
            }
            articleList.add(article);
        });
        map.put("articleList",articleList);
        map.put("field","");
        putIndexInfo(map,request);
        buildPagePlugin(page,articles.getTotalPages(),map);
        return "index";
    }

    /**
     * 首页筛选
     * */
    @PostMapping("/")
    public String indexQuery(ModelMap map, HttpServletRequest request,
                        @RequestParam(value = "field",required = false,defaultValue = "") String field,
                        @RequestParam(value = "page",required = false,defaultValue = "1") Integer page,
                        @RequestParam(value = "sort",required = false,defaultValue = "") String sort,
                        @RequestParam(value = "keyword",required = false,defaultValue = "") String keyword,
                        @RequestParam(value = "classifyId",required = false,defaultValue = "") String classifyId,
                        @RequestParam(value = "parentId",required = false,defaultValue = "") String parentId
    ){
        Map<String,Object> params = new HashMap<>();
        map.put("field",field);                   params.put("field",field);
        map.put("page",page);                     params.put("page",page);
        map.put("sort",sort);                     params.put("sort",sort);
        map.put("keyword",keyword);               params.put("keyword",keyword);
        map.put("classifyId",classifyId);         params.put("classifyId",classifyId);params.put("parentId",parentId);
        org.springframework.data.domain.Page<Article> articles = queryArticle(params);
        List<Article> articleList = new ArrayList<>();
        articles.get().forEach(article -> {
            Object o = redisTemplate.opsForHash().get(RedisConstant.ARTICLE_PRAISE_COUNT, article.getId().toString());
            if(o != null){
                article.setPraise(article.getPraise()+(Integer) o);
            }
            articleList.add(article);
        });
        map.put("articleList",articleList);
        putIndexInfo(map,request);
        buildPagePlugin(page,articles.getTotalPages(),map);
        return "index";
    }

    void putIndexInfo(ModelMap map,HttpServletRequest request){

        List<Banner> bannerList = bannerService.selectList();
        map.put("bannerList",bannerList);

        List<Notice> noticeList = noticeService.selectListLatest(5);
        map.put("noticeList",noticeList);

        List<OnlineUtils> onlineUtilsList = onlineUtilsService.selectListLatest(5);
        map.put("onlineUtilsList",onlineUtilsList);

        List<FriendLink> friendLinkList = friendLinkService.selectList();
        map.put("friendLinkList",friendLinkList);

        List<ArticleClassify> articleClassifyList = articleClassifyService.selectListByLevel(1);
        map.put("articleClassifyList",articleClassifyList);

        String ua= request.getHeader("User-Agent");
        if(StringHelper.checkAgentIsMobile(ua)){ //验证手机端登录
            map.put("isMobile",true);
        }
        //判断是否要开启首页灯笼
        if(requireEnableLantern()){
            map.put("enableLantern",true);
        }
    }

    private boolean requireEnableLantern() {
        String newYearDate = stringRedisTemplate.opsForValue().get(Constant.NEW_YEAR_DATE);
        if(newYearDate == null){
            newYearDate = "2020-01-24";
        }
        Date after30 = DateHelper.getDayRangeBySpecific(newYearDate, 30);
        Date before30 = DateHelper.getDayRangeBySpecific(newYearDate, -30);
        Date now= new Date();
        return now.compareTo(before30) > 0 && now.compareTo(after30) < 0;
    }

    private org.springframework.data.domain.Page<Article> queryArticle(Map<String, Object> params) {
        String name = params.get("keyword") == null ? null : params.get("keyword").toString();
        String classifyId = params.get("classifyId") == null ? null : params.get("classifyId").toString();
        String field = params.get("field") == null ? null : params.get("field").toString();
        String sort = params.get("sort") == null ? null : params.get("sort").toString();
        String parentId = params.get("parentId") == null ? null : params.get("parentId").toString();
        Integer page = (Integer)params.get("page");

        QueryBuilder qb1 = null;//名称
        if (!StringHelper.isEmpty(name)) {
            if (name.length() > 15) {
                name = name.substring(0, 10);
            }
            qb1 = QueryBuilders.matchQuery("title",  name);
        }
        QueryBuilder qb2 = null;
        if (!StringHelper.isEmpty(classifyId)) {
            int id = Integer.parseInt(classifyId);
            qb2 = QueryBuilders.matchQuery("classify.id", id);
        }
//
        QueryBuilder qb3 = null;
        if (!StringHelper.isEmpty(parentId)) {
            int id = Integer.parseInt(parentId);
            qb3 = QueryBuilders.boolQuery().should(QueryBuilders.termQuery("classify.parentId", id))
                    .should(QueryBuilders.termQuery("classify.id", id));
        }
        FieldSortBuilder fsb = null;
        if (!StringHelper.isEmpty(field)) {
            fsb = SortBuilders.fieldSort(field).order(sort.equals("asc") ? SortOrder.ASC : SortOrder.DESC);
        }
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if (qb1 != null) {
            boolQueryBuilder.must(qb1);
        }
        if (qb2 != null) {
            boolQueryBuilder.must(qb2);
        }
        if (qb3 != null) {
            boolQueryBuilder.must(qb3);
        }

        //如果默认，按顺序排
        if(StringHelper.isEmpty(field) && StringHelper.isEmpty(sort)){
            fsb = SortBuilders.fieldSort("dateInsert").order(SortOrder.DESC);
        }

        Pageable pageable = PageRequest.of(page - 1, 6);//分页
        NativeSearchQueryBuilder nsqb = new NativeSearchQueryBuilder().withQuery(boolQueryBuilder).withPageable(pageable);
        if(fsb != null){
            nsqb = nsqb.withSort(fsb);
        }

        SearchQuery query = nsqb.build();
        org.springframework.data.domain.Page<Article> search = articleRepository.search(query);

        return search;
    }

    void putLearningInfo(ModelMap map){

        List<OnlineUtils> onlineUtilsList = onlineUtilsService.selectListLatest(5);
        map.put("onlineUtilsList",onlineUtilsList);


        List<FriendLink> friendLinkList = friendLinkService.selectList();
        map.put("friendLinkList",friendLinkList);
        //最新文章
        List<Article> articleTitleList  =articleService.selectListLatest(5);
        map.put("articleTitleList",articleTitleList);
        //最新评论
        List<UserComment> latestCommentList = userCommentService.selectListLatest(5);
        map.put("latestCommentList",latestCommentList);
        //技术专栏
        List<ArticleClassify> articleClassifyList = articleClassifyService.selectRandomList(6);
        String[] arr={"out-front", "out-back", "out-left", "out-right", "out-top", "out-bottom"};
        Map<String,ArticleClassify> obj=new HashMap<>();
        for(int i=0;i<articleClassifyList.size();i++){
            ArticleClassify ac = articleClassifyList.get(i);
            if(ac.getName().length() > 6){
                ac.setName(ac.getName().substring(0,5)+"...");
            }
            obj.put(arr[i], ac);
        }

        map.put("zhuanlanList",obj);

        List<ArticleClassify> queryClassifyList = articleClassifyService.selectListByLevel(1);
        map.put("queryClassifyList",queryClassifyList);
    }

    @RequestMapping("/learning")
    public String learning(ModelMap map,@RequestParam(value = "page",required = false,defaultValue = "1") Integer page){
        Map<String,Object> params = new HashMap<>();
        params.put("page",page);
        map.put("field","");
        List<Article> articleList = new ArrayList<>();
        org.springframework.data.domain.Page<Article> articles = queryArticle(params);
        articles.get().forEach(article -> {
            Object o = redisTemplate.opsForHash().get(RedisConstant.ARTICLE_PRAISE_COUNT, article.getId().toString());
            if(o != null){
                article.setPraise(article.getPraise()+(Integer) o);
            }
            articleList.add(article);
        });        map.put("articleList",articleList);
        putLearningInfo(map);
        buildPagePlugin(page,articles.getTotalPages(),map);
        return "learning";
    }

    @PostMapping("/learning")
    public String learningQuery(ModelMap map,  @RequestParam(value = "field",required = false,defaultValue = "") String field,
                                @RequestParam(value = "page",required = false,defaultValue = "1") Integer page,
                                @RequestParam(value = "sort",required = false,defaultValue = "") String sort,
                                @RequestParam(value = "keyword",required = false,defaultValue = "") String keyword,
                                @RequestParam(value = "classifyId",required = false,defaultValue = "") String classifyId,
                                @RequestParam(value = "parentId",required = false,defaultValue = "") String parentId
    ){
        Map<String,Object> params = new HashMap<>();
        map.put("field",field);                   params.put("field",field);
        map.put("page",page);                     params.put("page",page);
        map.put("sort",sort);                     params.put("sort",sort);
        map.put("keyword",keyword);               params.put("keyword",keyword);
        map.put("classifyId",classifyId);         params.put("classifyId",classifyId);
        map.put("parentId",parentId);         params.put("parentId",parentId);
        List<Article> articleList = new ArrayList<>();
        org.springframework.data.domain.Page<Article> articles = queryArticle(params);
        articles.get().forEach(article -> {
            Object o = redisTemplate.opsForHash().get(RedisConstant.ARTICLE_PRAISE_COUNT, article.getId().toString());
            if(o != null){
                article.setPraise(article.getPraise()+(Integer) o);
            }
            articleList.add(article);
        });
        map.put("articleList",articleList);
        putLearningInfo(map);
        buildPagePlugin(page,articles.getTotalPages(),map);
        return "learning";
    }

    @RequestMapping("/life")
    public String life(ModelMap map,@RequestParam(value = "page",required = false,defaultValue = "1") Integer current){
        if(current < 1){
            current = 1;
        }
        IPage<MicroDiary> page = new Page<>(current,8);
        page = microDiaryService.selectListPage(page);
        map.put("notebookList",page.getRecords());
        buildPagePlugin(current,(int)page.getPages(),map);
        return "life";
    }

    @Autowired
    SkillService skillService;
    @RequestMapping("/aboutme")
    public String aboutme(ModelMap map){
        List<Skill> skillList = skillService.selectList();
        map.put("skillList",skillList);
        //个人信息
        List<Article> grxxList = new ArrayList<>();
        Article article1 = articleRepository.findById(2).orElse(null);
        Article article2 = articleRepository.findById(14).orElse(null);
        Article article3 = articleRepository.findById(80).orElse(null);

        grxxList.add(article1);
        grxxList.add(article2);
        grxxList.add(article3);
        map.put("grxxList",grxxList);
        //人生感悟
        List<Article> rsgwList = new ArrayList<>();
        MatchQueryBuilder matchQueryBuilder2 = QueryBuilders.matchQuery("classify.id", 4);
        Iterable<Article>  search = articleRepository.search(matchQueryBuilder2);
        search.forEach(rsgwList::add);
        map.put("rsgwList",rsgwList);
        return "aboutme";
    }

    @RequestMapping("/message")
    public String message(ModelMap map, @RequestParam(value = "page",required = false,defaultValue = "1") Integer current){
        if(current < 1){
            current = 1;
        }
        IPage<UserComment> page = new Page<>(current,GlobalConstant.PAGE_SIZE_COMMENT);
        page = userCommentService.selectFirstLevelCommentPage(page,null,null);
        List<UserComment> firstList = page.getRecords();
        for(UserComment uc : firstList){
            List<UserComment> childrenList = userCommentService.selectSecondLevelComment(null,null,uc.getId());
            uc.setChildrenList(childrenList);
        }
        map.put("firstList",firstList);

        List<ArticleClassify> hotTagList = articleClassifyService.selectHotTagList();
        map.put("hotTagList",hotTagList);
        List<OnlineUtils> onlineUtilsList = onlineUtilsService.selectListLatest(5);
        map.put("onlineUtilsList",onlineUtilsList);
        buildPagePlugin(current, (int)page.getPages(),map);
        String howBuildBlogDesc = stringRedisTemplate.opsForValue().get(Constant.HOW_BUILD_BLOG_DESC);
        map.put("howBuildBlogDesc",howBuildBlogDesc);
        return "message";
    }
}
