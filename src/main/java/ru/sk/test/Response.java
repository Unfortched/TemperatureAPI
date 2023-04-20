package ru.sk.test;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Response {
    private String end_code;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String limits;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String reg_limits;
}
