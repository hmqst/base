package com.example.base.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author benben
 * @date 2021-04-06 9:33
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PeopleBO implements Serializable {

    private String name;
    private Integer userId;

}
