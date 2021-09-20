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

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/postimg")
@Api
public class PostImgApiController implements CrudInterface<PostImgApiRequest, PostImgApiResponse> {

    private final S3UploaderService s3UploaderService;

    @PostMapping("")
    public String upload(@RequestParam("images") MultipartFile multipartFile) throws IOException {
        s3UploaderService.upload(multipartFile, "static");
        return "test";
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
