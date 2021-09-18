package com.example.digging.service;

import com.example.digging.domain.entity.*;
import com.example.digging.domain.network.CalendarHeader;
import com.example.digging.domain.network.response.CalendarResponse;
import com.example.digging.domain.network.response.RecentDiggingResponse;
import com.example.digging.domain.repository.*;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class CalendarLogicService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TagsRepository tagsRepository;

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private PostTagRepository postTagRepository;

    @Autowired
    private PostTextRepository postTextRepository;

    @Autowired
    private PostLinkRepository postLinkRepository;

    @Autowired
    private UserHasPostsRepository userHasPostsRepository;



    @SneakyThrows
    public CalendarHeader<ArrayList<CalendarResponse>> calendarread(Integer userid, String yearmonth){

        Optional<User> optional = userRepository.findById(userid);
        ArrayList<CalendarResponse> calendarList = new ArrayList<CalendarResponse>();
        Calendar cal = Calendar.getInstance();
        String year = yearmonth.substring(0,4);
        String month = yearmonth.substring(4,6);
        cal.set(Integer.parseInt(year),Integer.parseInt(month)-1,1);
        int dayofmonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        String month_info = year + "." + month;

        String dateform = year+"-"+month+"-01";
        Integer idnum = 1;
        String firstday = getDateDayName(dateform);
        int plusnum;
        switch(firstday) {
            case "monday":
                calendarList.add(CalendarResponse.builder().date(null).id(idnum).build());
                idnum += 1;
                plusnum = 1;
                break;
            case "tuesday":
                for(int i=1; i<3;i++){
                    calendarList.add(CalendarResponse.builder().date(null).id(i).build());
                }
                idnum += 2;
                plusnum = 2;
                break;
            case "wednesday":
                for(int i =1; i<4;i++){
                    calendarList.add(CalendarResponse.builder().date(null).id(i).build());
                }
                idnum += 3;
                plusnum = 3;
                break;
            case "thursday":
                for(int i =1; i<5;i++){
                    calendarList.add(CalendarResponse.builder().date(null).id(i).build());
                }
                idnum += 4;
                plusnum = 4;
                break;
            case "friday":
                for(int i =1; i<6;i++){
                    calendarList.add(CalendarResponse.builder().date(null).id(i).build());
                }
                idnum += 5;
                plusnum = 5;
                break;
            case "saturday":
                for(int i =1; i<7;i++){
                    calendarList.add(CalendarResponse.builder().date(null).id(i).build());
                }
                idnum += 6;
                plusnum = 6;
                break;
            default:
                plusnum = 0;
                break;
        }

        for(int i=1;i<=dayofmonth;i++){
            String form;
            if(i<10){
                form = year+"-"+month+"-0"+Integer.toString(i);
            }else {
                form = year+"-"+month+"-"+Integer.toString(i);
            }

            calendarList.add(CalendarResponse.builder()
                    .date(LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), i))
                    .resultCode("Success")
                    .day(getDateDayName(form))
                    .id(idnum)
                    .is_img(false)
                    .is_link(false)
                    .is_text(false)
                    .build());

            idnum += 1;
        }

        List<UserHasPosts> userHasPostsList = userHasPostsRepository.findAllByUser_UserId(userid);
        int userHasPostsNum = userHasPostsList.size();



        for(int i=0;i<userHasPostsNum;i++){
            String formatDate = userHasPostsList.get(i).getPosts().getCreatedAt().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            String y = formatDate.substring(0,4);
            String m = formatDate.substring(4,6);
            String d = formatDate.substring(6,8);
            Integer dd = Integer.parseInt(d);

            if(year.equals(y) && month.equals(m)){

                if(userHasPostsList.get(i).getPosts().getIsLink()==true){
                    calendarList.get(plusnum+dd-1).setIs_link(true);
                }
                if(userHasPostsList.get(i).getPosts().getIsText()==true){
                    calendarList.get(plusnum+dd-1).setIs_text(true);
                }
                if(userHasPostsList.get(i).getPosts().getIsImg()==true){
                    calendarList.get(plusnum+dd-1).setIs_img(true);
                }
            }
        }


        return optional.map(user -> CalendarHeader.OK(month_info, calendarList))
                .orElseGet(()->{
                    ArrayList<CalendarResponse> errorList = new ArrayList<CalendarResponse>();
                    CalendarResponse error = CalendarResponse.builder().resultCode("Error : User 없음").build();
                    errorList.add(error);
                    return CalendarHeader.OK(month_info, errorList);
                });

    }

    @SneakyThrows
    public CalendarHeader<ArrayList<CalendarResponse>> calendarread(Integer userid){

        Optional<User> optional = userRepository.findById(userid);
        ArrayList<CalendarResponse> calendarList = new ArrayList<CalendarResponse>();
        Calendar cal = Calendar.getInstance();

        Calendar today = Calendar.getInstance();

        int y = today.get(Calendar.YEAR);
        int m = today.get(Calendar.MONTH) + 1;

        String year = Integer.toString(y);
        String month;

        if (m<10){
            month = "0" + Integer.toString(m);
        }else {
            month = Integer.toString(m);
        }


        cal.set(y,m-1,1);
        int dayofmonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        String month_info;
        if (month.length()==1){
            month_info = year + ".0" + month;
        }else{
            month_info = year + "." + month;
        }
        String dateform = year+"-"+month+"-01";
        Integer idnum = 1;
        String firstday = getDateDayName(dateform);
        int plusnum;
        switch(firstday) {
            case "monday":
                calendarList.add(CalendarResponse.builder().date(null).id(idnum).build());
                idnum += 1;
                plusnum = 1;
                break;
            case "tuesday":
                for(int i=1; i<3;i++){
                    calendarList.add(CalendarResponse.builder().date(null).id(i).build());
                }
                idnum += 2;
                plusnum = 2;
                break;
            case "wednesday":
                for(int i =1; i<4;i++){
                    calendarList.add(CalendarResponse.builder().date(null).id(i).build());
                }
                idnum += 3;
                plusnum = 3;
                break;
            case "thursday":
                for(int i =1; i<5;i++){
                    calendarList.add(CalendarResponse.builder().date(null).id(i).build());
                }
                idnum += 4;
                plusnum = 4;
                break;
            case "friday":
                for(int i =1; i<6;i++){
                    calendarList.add(CalendarResponse.builder().date(null).id(i).build());
                }
                idnum += 5;
                plusnum = 5;
                break;
            case "saturday":
                for(int i =1; i<7;i++){
                    calendarList.add(CalendarResponse.builder().date(null).id(i).build());
                }
                idnum += 6;
                plusnum = 6;
                break;
            default:
                plusnum = 0;
                break;
        }

        for(int i=1;i<=dayofmonth;i++){
            String form;
            if(i<10){
                form = year+"-"+month+"-0"+Integer.toString(i);
            }else {
                form = year+"-"+month+"-"+Integer.toString(i);
            }

            calendarList.add(CalendarResponse.builder()
                    .date(LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), i))
                    .resultCode("Success")
                    .day(getDateDayName(form))
                    .id(idnum)
                    .is_img(false)
                    .is_link(false)
                    .is_text(false)
                    .build());

            idnum += 1;
        }

        List<UserHasPosts> userHasPostsList = userHasPostsRepository.findAllByUser_UserId(userid);
        int userHasPostsNum = userHasPostsList.size();

        System.out.println(userHasPostsNum);

        for(int i=0;i<userHasPostsNum;i++){
            String formatDate = userHasPostsList.get(i).getPosts().getCreatedAt().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            String fy = formatDate.substring(0,4);
            String fm = formatDate.substring(4,6);
            String fd = formatDate.substring(6,8);
            Integer dd = Integer.parseInt(fd);

            if(year.equals(fy) && month.equals(fm)){

                if(userHasPostsList.get(i).getPosts().getIsLink()==true){
                    calendarList.get(plusnum+dd-1).setIs_link(true);
                }
                if(userHasPostsList.get(i).getPosts().getIsText()==true){
                    calendarList.get(plusnum+dd-1).setIs_text(true);
                }
                if(userHasPostsList.get(i).getPosts().getIsImg()==true){
                    calendarList.get(plusnum+dd-1).setIs_img(true);
                }
            }
        }


        return optional.map(user -> CalendarHeader.OK(month_info, calendarList))
                .orElseGet(()->{
                    ArrayList<CalendarResponse> errorList = new ArrayList<CalendarResponse>();
                    CalendarResponse error = CalendarResponse.builder().resultCode("Error : User 없음").build();
                    errorList.add(error);
                    return CalendarHeader.OK(month_info, errorList);
                });

    }

    public ArrayList<RecentDiggingResponse> calendarpostread(Integer userid, String ymd){
        Optional<User> optional = userRepository.findById(userid);
        List<UserHasPosts> userHasPostsList = userHasPostsRepository.findAllByUser_UserId(userid);
        int userHasPostsNum = userHasPostsList.size();
        ArrayList<Integer> postIdList = new ArrayList<Integer>();
        for(int i =0; i<userHasPostsNum; i++){
            postIdList.add(userHasPostsList.get(i).getPosts().getPostId());
        }

        ArrayList<Optional<Posts>> orderPostsList = new ArrayList<>();
        String year;
        String month;
        String day;
        if (ymd.length() == 6) {
            year = ymd.substring(0,4);
            month = ymd.substring(4,6);
            day = "01";
        } else {
            year = ymd.substring(0,4);
            month = ymd.substring(4,6);
            day = ymd.substring(6,8);
        }
        System.out.println(year + month + day);
        int validpost = 0;
        for(int i =0; i<userHasPostsNum; i++){
            String formatDate = postsRepository.findById(postIdList.get(i)).get().getCreatedAt().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            String y = formatDate.substring(0,4);
            String m = formatDate.substring(4,6);
            String d = formatDate.substring(6,8);

            if(year.equals(y) && month.equals(m) && day.equals(d)){
                orderPostsList.add(postsRepository.findById(postIdList.get(i)));
                validpost += 1;
            }

        }



        ArrayList<ArrayList> tags = new ArrayList();
        ArrayList<RecentDiggingResponse> recentDiggingList = new ArrayList<RecentDiggingResponse>();
        for(int i =0; i<validpost; i++){
            if(orderPostsList.get(i).get().getIsLink() == Boolean.TRUE) {
                PostLink newlink = postLinkRepository.findByPostsPostId(orderPostsList.get(i).get().getPostId());
                List<PostTag> nowTags = postTagRepository.findAllByPostsPostId(orderPostsList.get(i).get().getPostId());
                int nowTagsSize = nowTags.size();
                ArrayList<String> tagStr = new ArrayList<String>();
                for(int j=0;j<nowTagsSize;j++){
                    tagStr.add(nowTags.get(j).getTags().getTags());
                }
                RecentDiggingResponse makingResponse = RecentDiggingResponse.builder()
                        .resultCode("Success")
                        .type("link")
                        .postId(newlink.getPosts().getPostId())
                        .linkId(newlink.getLinkId())
                        .title(newlink.getTitle())
                        .url(newlink.getUrl())
                        .createdAt(newlink.getCreatedAt())
                        .createdBy(newlink.getCreatedBy())
                        .updatedAt(newlink.getPosts().getUpdatedAt())
                        .updatedBy(newlink.getUpdatedBy())
                        .isLike(newlink.getPosts().getIsLike())
                        .tags(tagStr)
                        .build();
                recentDiggingList.add(makingResponse);
            }

            if(orderPostsList.get(i).get().getIsText() == Boolean.TRUE) {
                PostText newtext = postTextRepository.findByPostsPostId(orderPostsList.get(i).get().getPostId());
                List<PostTag> nowTags = postTagRepository.findAllByPostsPostId(orderPostsList.get(i).get().getPostId());
                int nowTagsSize = nowTags.size();
                ArrayList<String> tagStr = new ArrayList<String>();
                for(int j=0;j<nowTagsSize;j++){
                    tagStr.add(nowTags.get(j).getTags().getTags());
                }
                RecentDiggingResponse makingResponse = RecentDiggingResponse.builder()
                        .resultCode("Success")
                        .type("text")
                        .postId(newtext.getPosts().getPostId())
                        .textId(newtext.getTextId())
                        .title(newtext.getTitle())
                        .content(newtext.getContent())
                        .createdAt(newtext.getCreatedAt())
                        .createdBy(newtext.getCreatedBy())
                        .updatedAt(newtext.getPosts().getUpdatedAt())
                        .updatedBy(newtext.getUpdatedBy())
                        .isLike(newtext.getPosts().getIsLike())
                        .tags(tagStr)
                        .build();
                recentDiggingList.add(makingResponse);
            }
        }

        int number = recentDiggingList.size();


        return optional.map(user -> recentDiggingList)
                .orElseGet(()->{
                    ArrayList<RecentDiggingResponse> errorList = new ArrayList<RecentDiggingResponse>();
                    RecentDiggingResponse error = RecentDiggingResponse.builder().resultCode("Error : User 없음").build();
                    errorList.add(error);
                    return errorList;
                });
    }

    public ArrayList<RecentDiggingResponse> calendarpostread(Integer userid){
        Optional<User> optional = userRepository.findById(userid);
        List<UserHasPosts> userHasPostsList = userHasPostsRepository.findAllByUser_UserId(userid);
        int userHasPostsNum = userHasPostsList.size();
        ArrayList<Integer> postIdList = new ArrayList<Integer>();
        for(int i =0; i<userHasPostsNum; i++){
            postIdList.add(userHasPostsList.get(i).getPosts().getPostId());
        }
        Calendar today = Calendar.getInstance();

        int yyyy = today.get(Calendar.YEAR);
        int mm = today.get(Calendar.MONTH) + 1;
        int dd = today.get(Calendar.DATE);
        ArrayList<Optional<Posts>> orderPostsList = new ArrayList<>();
        String year = Integer.toString(yyyy);
        String month;
        String day;
        if (mm<10){
            month = "0" + Integer.toString(mm);
        }else {
            month = Integer.toString(mm);
        }
        if (dd<10){
            day = "0" + Integer.toString(dd);
        }else {
            day = Integer.toString(dd);
        }
        System.out.println(year + month + day);
        int validpost = 0;
        for(int i =0; i<userHasPostsNum; i++){
            String formatDate = postsRepository.findById(postIdList.get(i)).get().getCreatedAt().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            String y = formatDate.substring(0,4);
            String m = formatDate.substring(4,6);
            String d = formatDate.substring(6,8);


            if(year.equals(y) && month.equals(m) && day.equals(d)){
                orderPostsList.add(postsRepository.findById(postIdList.get(i)));
                validpost += 1;
            }

        }



        ArrayList<ArrayList> tags = new ArrayList();
        ArrayList<RecentDiggingResponse> recentDiggingList = new ArrayList<RecentDiggingResponse>();
        for(int i =0; i<validpost; i++){
            if(orderPostsList.get(i).get().getIsLink() == Boolean.TRUE) {
                PostLink newlink = postLinkRepository.findByPostsPostId(orderPostsList.get(i).get().getPostId());
                List<PostTag> nowTags = postTagRepository.findAllByPostsPostId(orderPostsList.get(i).get().getPostId());
                int nowTagsSize = nowTags.size();
                ArrayList<String> tagStr = new ArrayList<String>();
                for(int j=0;j<nowTagsSize;j++){
                    tagStr.add(nowTags.get(j).getTags().getTags());
                }
                RecentDiggingResponse makingResponse = RecentDiggingResponse.builder()
                        .resultCode("Success")
                        .type("link")
                        .postId(newlink.getPosts().getPostId())
                        .linkId(newlink.getLinkId())
                        .title(newlink.getTitle())
                        .url(newlink.getUrl())
                        .createdAt(newlink.getCreatedAt())
                        .createdBy(newlink.getCreatedBy())
                        .updatedAt(newlink.getPosts().getUpdatedAt())
                        .updatedBy(newlink.getUpdatedBy())
                        .isLike(newlink.getPosts().getIsLike())
                        .tags(tagStr)
                        .build();
                recentDiggingList.add(makingResponse);
            }

            if(orderPostsList.get(i).get().getIsText() == Boolean.TRUE) {
                PostText newtext = postTextRepository.findByPostsPostId(orderPostsList.get(i).get().getPostId());
                List<PostTag> nowTags = postTagRepository.findAllByPostsPostId(orderPostsList.get(i).get().getPostId());
                int nowTagsSize = nowTags.size();
                ArrayList<String> tagStr = new ArrayList<String>();
                for(int j=0;j<nowTagsSize;j++){
                    tagStr.add(nowTags.get(j).getTags().getTags());
                }
                RecentDiggingResponse makingResponse = RecentDiggingResponse.builder()
                        .resultCode("Success")
                        .type("text")
                        .postId(newtext.getPosts().getPostId())
                        .textId(newtext.getTextId())
                        .title(newtext.getTitle())
                        .content(newtext.getContent())
                        .createdAt(newtext.getCreatedAt())
                        .createdBy(newtext.getCreatedBy())
                        .updatedAt(newtext.getPosts().getUpdatedAt())
                        .updatedBy(newtext.getUpdatedBy())
                        .isLike(newtext.getPosts().getIsLike())
                        .tags(tagStr)
                        .build();
                recentDiggingList.add(makingResponse);
            }
        }

        int number = recentDiggingList.size();


        return optional.map(user -> recentDiggingList)
                .orElseGet(()->{
                    ArrayList<RecentDiggingResponse> errorList = new ArrayList<RecentDiggingResponse>();
                    RecentDiggingResponse error = RecentDiggingResponse.builder().resultCode("Error : User 없음").build();
                    errorList.add(error);
                    return errorList;
                });
    }

    public static String getDateDayName(String date) throws Exception {


        String day = "" ;

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd") ;
        java.util.Date nDate = dateFormat.parse(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(nDate);
        int dayNum = cal.get(Calendar.DAY_OF_WEEK);


        switch(dayNum){
            case 1:
                day = "sunday";
                break ;
            case 2:
                day = "monday";
                break ;
            case 3:
                day = "tuesday";
                break ;
            case 4:
                day = "wednesday";
                break ;
            case 5:
                day = "thursday";
                break ;
            case 6:
                day = "friday";
                break ;
            case 7:
                day = "saturday";
                break ;

        }


        return day ;
    }


}
