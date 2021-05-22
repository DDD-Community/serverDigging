package com.example.digging.domain.network.response;

import com.example.digging.domain.entity.Tags;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TotalTagResponse {
    private Optional<Tags> totalTags;
}
