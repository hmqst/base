package com.example.base.controller;

import com.alibaba.fastjson.JSON;
import com.example.base.utils.ResultBean;
import com.example.base.vo.JGoodsVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author benben
 * @date 2021-05-28 8:28
 */
@RestController
@RequestMapping("search")
@Api(tags = "ElasticSearch和Jsoup测试")
public class ElasticSearchController {

    @Resource
    RestHighLevelClient restHighLevelClient;

    // 索引名称
    private final String index = "j_goods";

    @ApiOperation("爬取数据到ElasticSearch")
    @GetMapping("jsoup/{keyword}")
    public ResultBean jsoup(@PathVariable String keyword) throws Exception {
        if (StringUtils.isBlank(keyword)) {
            return ResultBean.fail("关键字不可为空");
        }
        String uri = "https://search.jd.com/Search?keyword=";
        // 获取网页
        Document document = Jsoup.parse(new URL(uri + URLEncoder.encode(keyword, "UTF-8")), 30000);
        // 商品div
        Element element = document.getElementById("J_goodsList");
        // 商品列表
        Elements elements = element.getElementsByTag("li");
        // 获取内容
        List<JGoodsVO> list = new ArrayList<>();
        elements.forEach(el -> {
            String img = el.getElementsByTag("img").eq(0).attr("data-lazy-img");
            String price = el.getElementsByClass("p-price").eq(0).text();
            String title = el.getElementsByClass("p-name").eq(0).text();
            String url = el.getElementsByTag("a").eq(0).attr("href");
            JGoodsVO jGoodsVo = new JGoodsVO(img, price, title, url);
            list.add(jGoodsVo);
        });
        // 存入ElasticSearch
        // 判断索引是否存在，不存在则创建
        boolean exists = restHighLevelClient.indices().exists(new GetIndexRequest(index), RequestOptions.DEFAULT);
        if (!exists) {
            CreateIndexResponse createIndexResponse = restHighLevelClient.indices().create(new CreateIndexRequest(index), RequestOptions.DEFAULT);
            if (!createIndexResponse.isAcknowledged()) {
                return ResultBean.fail("索引创建失败");
            }
        }
        // 插入数据
        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.timeout("2m");
        list.forEach(jGoodsVo -> bulkRequest.add(new IndexRequest(index).source(JSON.toJSONString(jGoodsVo), XContentType.JSON)));
        BulkResponse responses = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        return ResultBean.success(responses);
    }

    @ApiOperation("查询ElasticSearch数据")
    @GetMapping("search/{keyword}/{pageNumber}/{pageSize}")
    public ResultBean search(@PathVariable String keyword, @PathVariable Integer pageNumber, @PathVariable Integer pageSize) throws Exception {
        if (StringUtils.isBlank(keyword)) {
            return ResultBean.fail("关键字不可为空");
        }
        if (pageNumber == null) {
            pageNumber = 1;
        }
        if (pageSize == null) {
            pageSize = 10;
        }
        // 条件搜索
        SearchRequest searchRequest = new SearchRequest(index);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        // 分页
        sourceBuilder.from(pageNumber);
        sourceBuilder.size(pageSize);
        // 匹配keyword（带分词）
        sourceBuilder.query(QueryBuilders.matchQuery("title", keyword));
        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
        // 高亮
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("title");
        highlightBuilder.preTags("<span style='color:red'>");
        highlightBuilder.postTags("</span>");
        sourceBuilder.highlighter(highlightBuilder);
        // 执行搜索
        searchRequest.source(sourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        // 解析结果
        List<JGoodsVO> list = new ArrayList<>();
        for (SearchHit hit : searchResponse.getHits().getHits()) {
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            // 高亮
            HighlightField highlightField = hit.getHighlightFields().get("title");
            if (highlightField != null) {
                Text[] fragments = highlightField.fragments();
                StringBuilder title = new StringBuilder();
                for (Text fragment : fragments) {
                    title.append(fragment);
                }
                // 替换原来的数据
                sourceAsMap.put("title", title.toString());
            }
            // 转换为对应实体
            JGoodsVO jGoodsVo = JSON.parseObject(JSON.toJSONString(sourceAsMap), JGoodsVO.class);
            list.add(jGoodsVo);
        }
        return ResultBean.success(list);
    }

}
