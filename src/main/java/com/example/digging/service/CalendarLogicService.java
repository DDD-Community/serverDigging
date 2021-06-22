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
        Calendar cal = Calendar.getInstance();
        String year = yearmonth.substring(0,3);
        String month = yearmonth.substring(4);
        cal.set(Integer.parseInt(year),Integer.parseInt(month)-1,1);
        int dayofmonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        String dateform = year+"-"+month+"-01";

        String firstday = getDateDayName(dateform);

        switch(firstday) {
            case "monday":
                calendarList.add(CalendarResponse.builder().date(null).id(1).build());
                break;
            case "tuesday":
                for(int i=1; i<3;i++){
                    calendarList.add(CalendarResponse.builder().date(null).id(i).build());
                }
                break;
            case "wednesday":
                for(int i =1; i<4;i++){
                    calendarList.add(CalendarResponse.builder().date(null).id(i).build());
                }
                break;
            case "thursday":
                for(int i =1; i<5;i++){
                    calendarList.add(CalendarResponse.builder().date(null).id(i).build());
                }
                break;
            case "friday":
                for(int i =1; i<6;i++){
                    calendarList.add(CalendarResponse.builder().date(null).id(i).build());
                }
                break;
            case "saturday":
                for(int i =1; i<7;i++){
                    calendarList.add(CalendarResponse.builder().date(null).id(i).build());
                }
                break;
            default:
                break;
        }



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
