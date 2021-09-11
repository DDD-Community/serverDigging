package com.example.digging.domain.entity;


import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "authority")
public class Authority {

    @Id
    @Column(name = "authority_name")
    private String authorityName;
}