package kr.co.hist.bcheck.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class FilterCondition {
    private String field;
    private FilterOperationEnum operator;
    private Object value;
}
