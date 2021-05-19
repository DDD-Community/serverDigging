package com.example.digging.ifs;

import com.example.digging.domain.network.Header;

public interface CrudInterface<Req, Res> {
    Header<Res> create(Header<Req> request);
    Header<Res> read(Integer id);
    Header<Res> update(Header<Req> request);
    Header delete(Integer id);
}
