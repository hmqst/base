package com.example.base.config.excel;

import lombok.Data;

/**
 * @program: cihongCharity
 * @description:
 * @author: lvyy
 * @create: 2021-01-28 16:47
 **/

@Data
public class RollBack {
    public RollBack(boolean rollBack) {
        this.rollBack = rollBack;
    }

    private boolean rollBack;


}