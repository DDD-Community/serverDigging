package com.example.digging.ifs;

import com.example.digging.domain.network.Header;

public interface PostsCrudInterface<Res, Req> {
    Header<Res> create(Integer userId, String type);
    Header<Res> read(Integer userid, Integer postid);
    Header<Res> update(Integer id);
    Header delete(Integer id);

}
