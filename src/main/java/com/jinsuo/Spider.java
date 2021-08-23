package com.jinsuo;

import com.jinsuo.bean.ImageCollectionInfo;
import com.jinsuo.config.ConfigReader;
import com.jinsuo.utils.ImagesUtil;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.util.List;

/**
 * 爬虫启动类
 */
public class Spider {
    public static void main(String[] args) {
        for (int i = 0; i <= ConfigReader.pageIndexMax; i++) {
            // http://www.gaopic.com/e/search/result/index.php?page=1&searchid=3497
            String indexUrl = ConfigReader.searchBaseUrl+i+"&searchid="+ConfigReader.searchId;
            System.out.println(indexUrl);
            CloseableHttpClient httpClient = HttpClientBuilder.create().build();

            List<String> indexHtmlList = ImagesUtil.getIndexHtmlList(httpClient, indexUrl);
            for (String imagesHtmlUrl : indexHtmlList) {
                ImageCollectionInfo imageCollectionInfo = ImagesUtil.getImageUrlList(httpClient, imagesHtmlUrl);
                System.out.println(imageCollectionInfo);
                //执行下载
                ImagesUtil.downloadImages(httpClient, imageCollectionInfo);
            }
        }
    }


}
