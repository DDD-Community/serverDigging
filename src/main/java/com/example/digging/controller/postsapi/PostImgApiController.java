package com.example.digging.controller.postsapi;

import com.example.digging.domain.network.request.PostImgApiRequest;
import com.example.digging.domain.network.request.PostLinkApiRequest;
import com.example.digging.domain.network.response.PostImgApiResponse;
import com.example.digging.domain.network.response.PostLinkApiResponse;
import com.example.digging.ifs.CrudInterface;
import com.example.digging.service.S3UploaderService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/postimg")
@Api
public class PostImgApiController implements CrudInterface<PostImgApiRequest, PostImgApiResponse> {

    private final S3UploaderService s3UploaderService;

    @PostMapping("")
    public ArrayList<String> upload(
            @RequestParam(name = "image1") MultipartFile multipartFile1,
            @RequestParam(name = "image2", required = false) MultipartFile multipartFile2,
            @RequestParam(name = "image3", required = false) MultipartFile multipartFile3,
            @RequestParam(name = "image4", required = false) MultipartFile multipartFile4,
            @RequestParam(name = "image5", required = false) MultipartFile multipartFile5
    ) throws IOException {

        ArrayList<String> imgUrls = new ArrayList<String>();
        String result1 = s3UploaderService.upload(multipartFile1, "static");
        imgUrls.add(result1);
        if(multipartFile2 != null) {
            String result2 = s3UploaderService.upload(multipartFile2, "static");
            imgUrls.add(result2);
        }
        if(multipartFile3 != null) {
            String result3 = s3UploaderService.upload(multipartFile3, "static");
            imgUrls.add(result3);
        }
        if(multipartFile4 != null) {
            String result4 = s3UploaderService.upload(multipartFile4, "static");
            imgUrls.add(result4);
        }
        if(multipartFile5 != null) {
            String result5 = s3UploaderService.upload(multipartFile5, "static");
            imgUrls.add(result5);
        }

        return imgUrls;
    }

    @Override
    public PostImgApiResponse create(PostImgApiRequest request) {
        return null;
    }

    @Override
    public PostImgApiResponse read(Integer id) {
        return null;
    }

    @Override
    public PostImgApiResponse update(Integer id, PostImgApiRequest request) {
        return null;
    }

    @Override
    public PostImgApiResponse delete(Integer id) {
        return null;
    }
}
