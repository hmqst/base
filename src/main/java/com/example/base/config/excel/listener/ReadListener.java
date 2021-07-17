package com.example.base.config.excel.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.exception.ExcelDataConvertException;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.read.metadata.holder.ReadRowHolder;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
public class ReadListener<T> extends AnalysisEventListener<T> {

    /**
     * 解析到的数据的集合
     */
    private List<T> list = new ArrayList<T>();

    /**
     * 出异常的行和列的错误信息汇总
     */
    private List<HashMap<String, String>> errorListIndex = new ArrayList<>();

    public List<T> getList() {
        return list;
    }

    public List<HashMap<String, String>> getErrorListIndex() {
        return errorListIndex;
    }

    @Override
    public void invoke(T t, AnalysisContext analysisContext) {
        log.info("解析到一条数据:{}", JSON.toJSONString(t));
        list.add(t);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        log.info("所有数据解析完成！");
    }

    @Override
    public void onException(Exception exception, AnalysisContext context) throws Exception {
        ReadRowHolder readRowHolder = context.readRowHolder();
        // 行标索引
        Integer rowIndex = readRowHolder.getRowIndex();
        // 如果是类型转换异常
        if (exception instanceof ExcelDataConvertException) {
            HashMap<String, String> hashMap = new HashMap<>();
            // 强转为类型转换异常
            ExcelDataConvertException e = (ExcelDataConvertException) exception;
            Integer columnIndex = e.getColumnIndex();
            CellData cellData = e.getCellData();
            String stringValue = cellData.getStringValue();
            log.info("第{}行，第{}列解析异常", rowIndex + 1, e.getColumnIndex() + 1);
            // 判断出错行是否在errorMap中
            hashMap.put("出错行号，列号", "行号: " + ++rowIndex + " 列号：" + ++columnIndex + "转换错误的值为：" + stringValue);
            errorListIndex.add(hashMap);
        }
    }
}
