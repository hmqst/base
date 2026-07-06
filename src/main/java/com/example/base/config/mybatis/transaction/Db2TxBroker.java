package com.example.base.config.mybatis.transaction;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.Callable;

@Slf4j
@Component
public class Db2TxBroker {

    @Transactional(DbTxConstants.DB2_TX)
    public <V> V inTransaction(Callable<V> callable) {
        try {
            log.info(DbTxConstants.DB2_TX + "-----");
            return callable.call();
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
