package com.example.base.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 京东商品内容
 * @author benben
 * @date 2021-05-28 10:04
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JGoodsVO {
    private String img;
    private String price;
    private String title;
    private String url;
}
