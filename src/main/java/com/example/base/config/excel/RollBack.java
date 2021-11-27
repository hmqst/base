package com.example.base.config.excel;

import lombok.Data;

/**
 * @author benben
 * @date 2021-01-28 16:47
 **/

@Data
public class RollBack {
    public RollBack(boolean rollBack) {
        this.rollBack = rollBack;
    }

    private boolean rollBack;


}