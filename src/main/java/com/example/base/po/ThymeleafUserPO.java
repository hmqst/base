package com.example.base.po;

import lombok.*;

/**
 * @author benben
 * @date 2021-11-06 13:10
 */
@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ThymeleafUserPO {
    private String name;
    private int age;
    private boolean man;
    private String role;
}
