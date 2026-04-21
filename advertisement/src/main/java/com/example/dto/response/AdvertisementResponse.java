package com.example.dto.response;

import com.example.enums.Category;
import com.example.enums.Condition;
import com.example.enums.Status;
import com.example.view.View;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class AdvertisementResponse {
        @JsonView(View.Public.class)
        private Long id;
        @JsonView(View.Public.class)
        private String name;
        @JsonView(View.Public.class)
        private Category category;
        @JsonView(View.Public.class)
        private String subcategory;
        @JsonView(View.Public.class)
        private Condition condition;
        @JsonView(View.Public.class)
        private Integer cost;
        @JsonView(View.Public.class)
        private String address;
        @JsonView(View.Public.class)
        private String description;
        @JsonView(View.Private.class)
        private String userId;
        @JsonView(View.Private.class)
        private String userFullName;
        @JsonView(View.Private.class)
        private String phone;
        @JsonView(View.Private.class)
        private Status status;
        @JsonView(View.Private.class)
        private LocalDateTime created;

}