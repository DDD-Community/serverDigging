package com.example.digging.ifs;

import com.example.digging.domain.network.Header;

public interface CrudInterface<Req, Res> {
    Res create(Req request);
    Res read(Integer id);
    Res update(Integer id, Req request);
    Res delete(Integer id);
}
