package com.baloise.open.edw.domain.dto;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.Serializable;

@Getter
@Builder
public class Problem implements Serializable {

  public static final String MEDIA_TYPE_PROBLEM_JSON = "application/problem+json";

  final String title;
  @Min(100)
  @Max(600)
  final Integer status;
  final String detail;
  final String cid;
  final Integer errorCode;

  @Override
  public String toString() {
    return "Problem[" +
        "status=" + status +
        ", cid='" + cid + '\'' +
        ", title='" + title + '\'' +
        ", detail='" + detail + '\'' +
        ", errorCode='" + errorCode + '\'' +
        ']';
  }
}
