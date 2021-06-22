package com.example.digging.service;

import com.example.digging.domain.entity.User;
import com.example.digging.domain.entity.UserHasPosts;
import com.example.digging.domain.network.response.CalendarResponse;
import com.example.digging.domain.network.response.RecentDiggingResponse;
import com.example.digging.domain.repository.*;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
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
    public ArrayList<CalendarResponse> calendarread(Integer userid, String yearmonth){

        Optional<User> optional = userRepository.findById(userid);
        ArrayList<CalendarResponse> calendarList = new ArrayList<CalendarResponse>();
//        Calendar cal = Calendar.getInstance();
//        String year = yearmonth.substring(0,3);
//        String month = yearmonth.substring(4);
//        cal.set(Integer.parseInt(year),Integer.parseInt(month),1);
//        int dayofmonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
//        System.out.println(dayofmonth);
//        String dateform = yearmonth+"01";
//
//        String firstday = getDateDay(dateform, "yyyy-MM-dd");
//        System.out.println(firstday);

        Calendar calendar = Calendar.getInstance();

        //현재 날짜로 설정
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);

        //현재 달의 시작일과 마지막일 구하기
        int start = calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
        int end = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        //DateFormat에 맞춰 String에 담기

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
        calendar.set(year, month, start);
        String startdate =  dateFormat.format(calendar.getTime());
        calendar.set(year, month, end);
        String enddate = dateFormat.format(calendar.getTime());

        List<UserHasPosts> userHasPostsList = userHasPostsRepository.findAllByUserId(userid);
        int userHasPostsNum = userHasPostsList.size();
        ArrayList<Integer> postIdList = new ArrayList<Integer>();
        for(int i =0; i<userHasPostsNum; i++){
            postIdList.add(userHasPostsList.get(i).getPosts().getPostId());
        }



        return optional.map(user -> calendarList)
                .orElseGet(()->{
                    ArrayList<CalendarResponse> errorList = new ArrayList<CalendarResponse>();
                    CalendarResponse error = CalendarResponse.builder().resultCode("Error : User 없음").build();
                    errorList.add(error);
                    return errorList;
                });

    }

    public String getDateDay(String date, String dateType) throws Exception {


        String day = "" ;

        SimpleDateFormat dateFormat = new SimpleDateFormat(dateType) ;
        Date nDate = dateFormat.parse(date) ;

        Calendar cal = Calendar.getInstance() ;
        cal.setTime(nDate);

        int dayNum = cal.get(Calendar.DAY_OF_WEEK) ;

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
