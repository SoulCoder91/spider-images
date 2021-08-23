package com.jinsuo.utils;

import com.jinsuo.bean.ImageCollectionInfo;
import com.jinsuo.config.ConfigReader;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 图片的工具类
 */
public class ImagesUtil {

    /**
     * 图集url后缀集合的存储
     *
     * @param httpClient
     * @param indexUrl   各个page的url
     * @return 图集url后缀的集合
     */
    public static List<String> getIndexHtmlList(CloseableHttpClient httpClient, String indexUrl) {
        ArrayList<String> list = new ArrayList<String>();
        HttpGet httpGet = new HttpGet(indexUrl);
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity responseEntity = response.getEntity();
                String str = EntityUtils.toString(responseEntity);

                Document document = Jsoup.parse(str);
                // page0 的图集集合
                Elements lis = document.select("body > div.list_cont.list_cont2.w1180 > div.tab_hj > div > ul > li");
                for (int j = 0; j < lis.size(); j++) {
                    Element li = lis.get(j);
                    // 每一个图集url后缀
                    Elements a = li.select("a");
                    String imagesHtmlUrl = a.attr("href");// /meitu_84208.html
                    list.add(imagesHtmlUrl);
                }
            }
        } catch (IOException e) {
            e.getMessage();
        }
        return list;
    }

    /**
     * 图片url集合的存储
     *
     * @param httpClient
     * @param imagesHtmlUrl 图集url后缀
     * @return ImageCollectionInfo
     */
    public static ImageCollectionInfo getImageUrlList(CloseableHttpClient httpClient, String imagesHtmlUrl) {
        ImageCollectionInfo imageCollectionInfo = new ImageCollectionInfo();
        ArrayList<String> imageUrlList = new ArrayList<String>();
        String[] split = imagesHtmlUrl.split("\\.");
        int imagePage = 0;
        int pageMax = 0;
        while (true) {
            // 图片页
            String imagesUrl = ConfigReader.websiteUrl + split[0] + "_" + imagePage + "." + split[1];
            HttpGet httpGet1 = new HttpGet(imagesUrl);
            CloseableHttpResponse response1 = null;
            try {
                response1 = httpClient.execute(httpGet1);
                if (response1.getStatusLine().getStatusCode() == 200) {
                    HttpEntity httpEntity = response1.getEntity();
                    String ImagesHtmlStr = null;
                    ImagesHtmlStr = EntityUtils.toString(httpEntity);
                    // 解析图片页
                    Document document1 = Jsoup.parse(ImagesHtmlStr);
                    Element em = document1.select("body > div.main > div > div.pic_main > div > div.Bigimg > div.ptitle > em").first();
                    // 获取并设置图集最大页数
                    if (pageMax == 0) {
                        pageMax = Integer.parseInt(em.text());
                    }
                    Element img = document1.select("#pic-meinv > a > img").first();
                    String imageUrl = img.attr("data-original");
                    String imagesTitle = img.attr("title");
                    if (null == imageCollectionInfo.getTitle() || "".equals(imageCollectionInfo.getTitle())) {
                        imageCollectionInfo.setTitle(imagesTitle);
                    }
                    imageUrlList.add(imageUrl);
                }
                imagePage++;
                if (imagePage >= pageMax) {
                    break;
                }
            } catch (Exception e) {
                e.getMessage();
            }
        }

        imageCollectionInfo.setUrlList(imageUrlList);
        return imageCollectionInfo;
    }

    /**
     * 图片下载到windows本地
     *
     * @param imageCollectionInfo 图集的信息
     */
    public static void downloadImages(CloseableHttpClient httpClient, ImageCollectionInfo imageCollectionInfo) {
        String localImagesDir = ConfigReader.localImagesDirPrefix + imageCollectionInfo.getTitle();
        String localImagesFile = localImagesDir + "\\" + UUID.randomUUID() + ConfigReader.imageSuffix;

        for (String imageSrc : imageCollectionInfo.getUrlList()) {
            HttpGet httpGet2 = new HttpGet(imageSrc);
            CloseableHttpResponse response2 = null;
            try {
                response2 = httpClient.execute(httpGet2);
                if (response2.getStatusLine().getStatusCode() == 200) {

                    InputStream inputStream = response2.getEntity().getContent();
                    //创建本地文件夹
                    File file = new File(localImagesDir);
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    FileOutputStream outputStream = new FileOutputStream(localImagesFile);
                    //执行下载操作
                    IOUtils.copy(inputStream, outputStream);
                    Thread.sleep(3000);
                }
            } catch (Exception e) {
                e.getMessage();
            }

        }
    }
}
