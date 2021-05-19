package com.example.digging.service;

import com.example.digging.domain.entity.User;
import com.example.digging.domain.network.Header;
import com.example.digging.domain.network.request.UserApiRequest;
import com.example.digging.domain.network.response.UserApiResponse;
import com.example.digging.domain.repository.UserRepository;
import com.example.digging.ifs.CrudInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserApiLogicService implements CrudInterface<UserApiRequest, UserApiResponse> {

    @Autowired
    private UserRepository userRepository;

    @Override
    public Header<UserApiResponse> create(Header<UserApiRequest> request) {
        UserApiRequest userApiRequest = request.getData();

        User user = User.builder()
                .username(userApiRequest.getUsername())
                .email(userApiRequest.getEmail())
                .password(userApiRequest.getPassword())
                .provider(userApiRequest.getProvider())
                .role("Admin")
                .interest(userApiRequest.getInterest())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        User newUser = userRepository.save(user);

        return response(newUser);
    }

    @Override
    public Header<UserApiResponse> read(int id) {
        return null;
    }

    @Override
    public Header<UserApiResponse> update(Header<UserApiRequest> request) {
        return null;
    }

    @Override
    public Header delete(int id) {
        return null;
    }

    private Header<UserApiResponse> response(User user){
        UserApiResponse userApiResponse = UserApiResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .provider(user.getProvider())
                .role(user.getRole())
                .interest(user.getInterest())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();

        return Header.OK(userApiResponse);

    }
}
