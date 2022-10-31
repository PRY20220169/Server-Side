package com.tp.pry20220169.util;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tp.pry20220169.resource.CollectionResource;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public class HelperCollectionPage extends PageImpl<CollectionResource> {

    @JsonCreator
    public HelperCollectionPage(@JsonProperty("content") List<CollectionResource> content,
                                @JsonProperty("number") int number,
                                @JsonProperty("size") int size,
                                @JsonProperty("totalElements") Long totalElements) {
        super(content, PageRequest.of(number, size), totalElements);
    }
}
