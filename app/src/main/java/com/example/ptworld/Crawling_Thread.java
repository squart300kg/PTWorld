package com.example.ptworld;

import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class Crawling_Thread extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... voids) {
        crawling();
        return null;
    }
    public void crawling(){
        String domain = "https://healthkeeper100.tistory.com/category/";
        String category[] = {"아픈부위찾기", "체형교정", "Study", "유튜브(Youtube)"};
        String total_url;

        for(int i = 1 ; i <= category.length ; i ++){
            if( i == 1){
                String category2[] = {"목%20통증", "어깨%20통증", "등%20통증", "허리%20통증",  "골반%2C%20고관절%20통증", "엉치%20통증", "무릎%20통증", "발목%2C발%20통증", "팔꿈치%20통증", "손목%2C%20손%20통증", "가슴%20통증", "턱관절%20통증", "머리%20통증%2C%20두통"};
                String total_page_num[] = new String[category2.length];
                int paging1[] = new int[category2.length];

                for(int j = 1 ; j <= category2.length ; j ++){
                    total_url = domain + category[i - 1] + "/" + category2[j - 1];
                    Log.i("URL주송", total_url);
                    //            for(int num = 1 ; num <= 4 ; num ++){
                    try {
                        Document paging = Jsoup.connect(total_url).ignoreHttpErrors(true).get();
                        Elements size = paging.select("div[class=area_list]").select("a[class=link_category]");
                        String total_paging = size.text();
                        int lastIndex = total_paging.lastIndexOf('(');
                        Log.i("totalPage",total_paging);

                        String total_paging2 = size.text().substring(lastIndex+1, total_paging.length()-1);
                        total_page_num[j - 1] = total_paging2;//각 카테고리별로 페이징 수들을 저장하고 있다.
                        Log.i("개시물 총 갯수",total_page_num[j - 1]);
                        paging1[j - 1] = Integer.parseInt(total_page_num[j - 1]) % 12 == 0 ? Integer.parseInt(total_page_num[j - 1]) / 12 : Integer.parseInt(total_page_num[j - 1]) / 12 + 1;
//                    Log.i("총 페이징 수", total_paging);
//                    Log.i("총 페이징 수", total_paging2);
//                    Log.i("12로 나눈 페이징", paging1+"");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    total_url = total_url + "?page=" + paging1[j - 1];
                    Log.i("크롤링할최종URL",total_url);


                    try {
                        Document doc = Jsoup.connect(total_url).ignoreHttpErrors(true).get();
                        Elements mElementDataSize = doc.select("div[id=mArticle]"); //필요한 녀석만 꼬집어서 지정
                        for(Element elem : mElementDataSize) { //이렇게 요긴한 기능이
                            //우선 한개 게시물의 제목과 썸네일, url을 추출한다.
                            String subject = elem.select("strong[class=tit_post]").text();//제목추출
                            String imageUrlArr[] = elem.select("img").attr("src").split("fname=");//썸네일추출
                            String thumbnail_url = imageUrlArr[1];

                            //해당 게시물의 URL을 추출한다.
                            String contents_url = "https://healthkeeper100.tistory.com" + elem.select("a[class=thumbnail_post]").attr("href");
                            Log.i("콘텐츠 추출URL",contents_url);

                            //이로써 한 페이지 게시물의 제목, url, 썸네일을 추출했다. 이 녀석들을 알맞게 db에 담아보자.
                            //크롤링한 데이터들을 DB에 모두 넣기 위한 임시 스레드이다.
                            Thread_Temp thread_temp = new Thread_Temp();
                            String IP_ADDRESS = "squart300kg.cafe24.com";
                            thread_temp.execute("http://"+IP_ADDRESS+"/user_signup/temp_mainContentsInsert.php", subject, thumbnail_url, contents_url, category[i - 1] + "/" + category2[j - 1]);
                            //1, 요청할 URL 2. 게시물 제목 3. 게시물 썸네일 4. 해당 게시물 URL
                            Log.i("제목", subject);
                            Log.i("썸네일URL", thumbnail_url);
                            Log.i("내용URL", contents_url);
                            Log.i("type", category[i - 1] + "/" + category2[j - 1]);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }else if( i == 2 ){
                String category2[] = {"체형교정%28목%29", "체형교정%28측면%20척추%29", "쳬형교정%28척추측만증%29", "체형교정%28골반%29", "체형교정%28휜다리%29", "체형교정%28평발%2C말발%29"};
                String total_page_num[] = new String[category2.length];
                int paging1[] = new int[category2.length];

                for(int j = 1 ; j <= category2.length ; j ++){
                    total_url = domain + category[i - 1] + "/" + category2[j - 1];
                    Log.i("URL주송", total_url);
                    //            for(int num = 1 ; num <= 4 ; num ++){
                    try {
                        Document paging = Jsoup.connect(total_url).ignoreHttpErrors(true).get();
                        Elements size = paging.select("div[class=area_list]").select("a[class=link_category]");
                        String total_paging = size.text();
                        int lastIndex = total_paging.lastIndexOf('(');
                        Log.i("totalPage",total_paging);

                        String total_paging2 = size.text().substring(lastIndex+1, total_paging.length()-1);
                        total_page_num[j - 1] = total_paging2;//각 카테고리별로 페이징 수들을 저장하고 있다.
                        Log.i("개시물 총 갯수",total_page_num[j - 1]);
                        paging1[j - 1] = Integer.parseInt(total_page_num[j - 1]) % 12 == 0 ? Integer.parseInt(total_page_num[j - 1]) / 12 : Integer.parseInt(total_page_num[j - 1]) / 12 + 1;
//                    Log.i("총 페이징 수", total_paging);
//                    Log.i("총 페이징 수", total_paging2);
//                    Log.i("12로 나눈 페이징", paging1+"");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    total_url = total_url + "?page=" + paging1[j - 1];
                    Log.i("크롤링할최종URL",total_url);


                    try {
                        Document doc = Jsoup.connect(total_url).ignoreHttpErrors(true).get();
                        Elements mElementDataSize = doc.select("div[id=mArticle]"); //필요한 녀석만 꼬집어서 지정
                        for(Element elem : mElementDataSize) { //이렇게 요긴한 기능이
                            //우선 한개 게시물의 제목과 썸네일, url을 추출한다.
                            String subject = elem.select("strong[class=tit_post]").text();//제목추출
                            String imageUrlArr[] = elem.select("img").attr("src").split("fname=");//썸네일추출
                            String thumbnail_url = imageUrlArr[1];

                            //해당 게시물의 URL을 추출한다.
                            String contents_url = "https://healthkeeper100.tistory.com" + elem.select("a[class=thumbnail_post]").attr("href");
                            Log.i("콘텐츠 추출URL",contents_url);

                            //이로써 한 페이지 게시물의 제목, url, 썸네일을 추출했다. 이 녀석들을 알맞게 db에 담아보자.
                            //크롤링한 데이터들을 DB에 모두 넣기 위한 임시 스레드이다.
                            Thread_Temp thread_temp = new Thread_Temp();
                            String IP_ADDRESS = "squart300kg.cafe24.com";
                            thread_temp.execute("http://"+IP_ADDRESS+"/user_signup/temp_mainContentsInsert.php", subject, thumbnail_url, contents_url, category[i - 1] + "/" + category2[j - 1]);
                            //1, 요청할 URL 2. 게시물 제목 3. 게시물 썸네일 4. 해당 게시물 URL
                            Log.i("제목", subject);
                            Log.i("썸네일URL", thumbnail_url);
                            Log.i("내용URL", contents_url);
                            Log.i("type", category[i - 1] + "/" + category2[j - 1]);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }else if( i == 3 ){
                String category2[] = {"해부학", "물리치료학", "영양학%20%28영양학%2C다이어트%29", "약학%20%28건기식%2C제약%29"};
                String total_page_num[] = new String[category2.length];
                int paging1[] = new int[category2.length];

                for(int j = 1 ; j <= category2.length ; j ++){
                    total_url = domain + category[i - 1] + "/" + category2[j - 1];
                    Log.i("URL주송", total_url);
                    //            for(int num = 1 ; num <= 4 ; num ++){
                    try {
                        Document paging = Jsoup.connect(total_url).ignoreHttpErrors(true).get();
                        Elements size = paging.select("div[class=area_list]").select("a[class=link_category]");
                        String total_paging = size.text();
                        int lastIndex = total_paging.lastIndexOf('(');
                        Log.i("totalPage",total_paging);

                        String total_paging2 = size.text().substring(lastIndex+1, total_paging.length()-1);
                        total_page_num[j - 1] = total_paging2;//각 카테고리별로 페이징 수들을 저장하고 있다.
                        Log.i("개시물 총 갯수",total_page_num[j - 1]);
                        paging1[j - 1] = Integer.parseInt(total_page_num[j - 1]) % 12 == 0 ? Integer.parseInt(total_page_num[j - 1]) / 12 : Integer.parseInt(total_page_num[j - 1]) / 12 + 1;
//                    Log.i("총 페이징 수", total_paging);
//                    Log.i("총 페이징 수", total_paging2);
//                    Log.i("12로 나눈 페이징", paging1+"");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    total_url = total_url + "?page=" + paging1[j - 1];
                    Log.i("크롤링할최종URL",total_url);


                    try {
                        Document doc = Jsoup.connect(total_url).ignoreHttpErrors(true).get();
                        Elements mElementDataSize = doc.select("div[id=mArticle]"); //필요한 녀석만 꼬집어서 지정
                        for(Element elem : mElementDataSize) { //이렇게 요긴한 기능이
                            //우선 한개 게시물의 제목과 썸네일, url을 추출한다.
                            String subject = elem.select("strong[class=tit_post]").text();//제목추출
                            String imageUrlArr[] = elem.select("img").attr("src").split("fname=");//썸네일추출
                            String thumbnail_url = imageUrlArr[1];

                            //해당 게시물의 URL을 추출한다.
                            String contents_url = "https://healthkeeper100.tistory.com" + elem.select("a[class=thumbnail_post]").attr("href");
                            Log.i("콘텐츠 추출URL",contents_url);

                            //이로써 한 페이지 게시물의 제목, url, 썸네일을 추출했다. 이 녀석들을 알맞게 db에 담아보자.
                            //크롤링한 데이터들을 DB에 모두 넣기 위한 임시 스레드이다.
                            Thread_Temp thread_temp = new Thread_Temp();
                            String IP_ADDRESS = "squart300kg.cafe24.com";
                            thread_temp.execute("http://"+IP_ADDRESS+"/user_signup/temp_mainContentsInsert.php", subject, thumbnail_url, contents_url, category[i - 1] + "/" + category2[j - 1]);
                            //1, 요청할 URL 2. 게시물 제목 3. 게시물 썸네일 4. 해당 게시물 URL
                            Log.i("제목", subject);
                            Log.i("썸네일URL", thumbnail_url);
                            Log.i("내용URL", contents_url);
                            Log.i("type", category[i - 1] + "/" + category2[j - 1]);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }else if( i == 4 ){
                String category2[] = {"목", "어깨", "등", "허리", "골반", "무릎",
                        "발목%2C발", "팔꿈치", "손목%2C손%20", "두통", "턱관절",
                        "웨이트%20-%20%20운동", "다이어트", "스트레칭", "건강%20꿀팁%20정보", "빡빡이의%20예능"};
                String total_page_num[] = new String[category2.length];
                int paging1[] = new int[category2.length];

                for(int j = 1 ; j <= category2.length ; j ++){
                    total_url = domain + category[i - 1] + "/" + category2[j - 1];
                    Log.i("URL주송", total_url);
                    //            for(int num = 1 ; num <= 4 ; num ++){
                    try {
                        Document paging = Jsoup.connect(total_url).ignoreHttpErrors(true).get();
                        Elements size = paging.select("div[class=area_list]").select("a[class=link_category]");
                        String total_paging = size.text();
                        int lastIndex = total_paging.lastIndexOf('(');
                        Log.i("totalPage",total_paging);

                        String total_paging2 = size.text().substring(lastIndex+1, total_paging.length()-1);
                        total_page_num[j - 1] = total_paging2;//각 카테고리별로 페이징 수들을 저장하고 있다.
                        Log.i("개시물 총 갯수",total_page_num[j - 1]);
                        paging1[j - 1] = Integer.parseInt(total_page_num[j - 1]) % 12 == 0 ? Integer.parseInt(total_page_num[j - 1]) / 12 : Integer.parseInt(total_page_num[j - 1]) / 12 + 1;
//                    Log.i("총 페이징 수", total_paging);
//                    Log.i("총 페이징 수", total_paging2);
//                    Log.i("12로 나눈 페이징", paging1+"");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    total_url = total_url + "?page=" + paging1[j - 1];
                    Log.i("크롤링할최종URL",total_url);


                    try {
                        Document doc = Jsoup.connect(total_url).ignoreHttpErrors(true).get();
                        Elements mElementDataSize = doc.select("div[id=mArticle]"); //필요한 녀석만 꼬집어서 지정
                        for(Element elem : mElementDataSize) { //이렇게 요긴한 기능이
                            //우선 한개 게시물의 제목과 썸네일, url을 추출한다.
                            String subject = elem.select("strong[class=tit_post]").text();//제목추출
                            String imageUrlArr[] = elem.select("img").attr("src").split("fname=");//썸네일추출
                            String thumbnail_url;
                            try{
                                thumbnail_url = imageUrlArr[1];
                            }catch (ArrayIndexOutOfBoundsException e){
                                Log.i("이미지 읍다","이미지 읍다");
                                thumbnail_url = "https://www.google.com/url?sa=i&source=images&cd=&ved=2ahUKEwiw5dz5lYrlAhWQ62EKHZ-_AGEQjRx6BAgBEAQ&url=https%3A%2F%2Fhealthkeeper100.tistory.com%2F&psig=AOvVaw397NnMN5_HTgBaKKG_2183&ust=1570538489186320";
                            }


                            //해당 게시물의 URL을 추출한다.
                            String contents_url = "https://healthkeeper100.tistory.com" + elem.select("a[class=thumbnail_post]").attr("href");
                            Log.i("콘텐츠 추출URL",contents_url);

                            //이로써 한 페이지 게시물의 제목, url, 썸네일을 추출했다. 이 녀석들을 알맞게 db에 담아보자.
                            //크롤링한 데이터들을 DB에 모두 넣기 위한 임시 스레드이다.
                            Thread_Temp thread_temp = new Thread_Temp();
                            String IP_ADDRESS = "squart300kg.cafe24.com";
                            thread_temp.execute("http://"+IP_ADDRESS+"/user_signup/temp_mainContentsInsert.php", subject, thumbnail_url, contents_url, category[i - 1] + "/" + category2[j - 1]);
                            //1, 요청할 URL 2. 게시물 제목 3. 게시물 썸네일 4. 해당 게시물 URL
                            Log.i("제목", subject);
                            Log.i("썸네일URL", thumbnail_url);
                            Log.i("내용URL", contents_url);
                            Log.i("type", category[i - 1] + "/" + category2[j - 1]);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }
}