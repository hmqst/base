package com.example.base.controller;

import com.example.base.dto.CarDTO;
import com.example.base.enums.TypeEnum;
import com.example.base.mapper.CarMapper;
import com.example.base.po.CarPO;
import com.example.base.rabbitmq.RabbitFanoutConfig;
import com.example.base.rabbitmq.RabbitTopicConfig;
import com.example.base.utils.RedisUtil;
import com.example.base.utils.ResultBean;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author benben
 * @program base
 * @Description
 * @date 2021-03-22 15:50
 */
@RestController
@RequestMapping("test")
@Api(tags = "测试相关")
@Slf4j
public class TestController {

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private CarMapper carMapper;

    @Resource
    private ObjectMapper objectMapper;

    @Autowired
    JavaMailSender mailSender;

    @Value("${spring.datasource.url}")
    String datasourceUrl;

    @Value("${spring.datasource.username}")
    String username;

    @Value("${spring.datasource.password}")
    String password;

    @Value("${spring.mail.username}")
    String fromEmailAddress;

    @Value("${spring.mail.to}")
    String[] toEmailAddress;

    @ApiOperation("Jackson自定义全局序列化测试")
    @RequestMapping("jackson")
    public ResultBean jackson() {
        HashMap<Object, Object> map = new HashMap<>();
        HashMap nullMap = null;
        String nullStr = null;
        Object nullObject = null;
        ArrayList nullList = null;
        Boolean nullBoolean = null;
        Number nullnumber = null;
        map.put("key", nullStr);
        map.put("map", nullMap);
        map.put("object", nullObject);
        map.put("list", nullList);
        map.put("boolean", nullBoolean);
        map.put("number", nullnumber);
        map.put("objectMapper.getDateFormat()", objectMapper.getDateFormat().format(new Date()));
        return ResultBean.success(map);
    }

    @ApiOperation("MapStruct转换测试")
    @RequestMapping("mapStruct")
    public ResultBean mapStruct() {
        Map<String, Object> map = new HashMap<>();
        // PO转DTO
        CarPO car = new CarPO("make", 50, TypeEnum.FOUR, new Date());
        CarDTO carDto = carMapper.carToCarDto(car);
        map.put("PO转DTO", new HashMap<String, Object>() {{
            put("CarPO", car);
            put("CarDTO", carDto);
        }});
        // DTO转PO
        CarPO car1 = carMapper.carDtoToCar(carDto);
        map.put("DTO转PO", new HashMap<String, Object>() {{
            put("CarDTO", carDto);
            put("CarPO", car1);
        }});
        return ResultBean.success(map);
    }

    @ApiOperation("Redis存储对象测试")
    @RequestMapping("redis")
    public ResultBean redis() {
        CarPO car = new CarPO("make", 50, TypeEnum.FOUR, new Date());
        redisUtil.set("car", car);
        CarPO car1 = (CarPO) redisUtil.get("car");
        return ResultBean.success(car1);
    }

    @ApiOperation("RabbitMq（DirectExchange）")
    @RequestMapping("directExchange")
    public ResultBean directExchange() {
        for (int i = 0; i < 10; i++) {
            rabbitTemplate.convertAndSend("hello-direct", "hello-direct" + i);
            rabbitTemplate.convertAndSend("hello-direct1", "hello-direct1" + i);
            rabbitTemplate.convertAndSend("hello-direct2", "hello-direct2" + i);
        }
        return ResultBean.success();
    }

    @ApiOperation("RabbitMq（FanoutExchange）")
    @RequestMapping("fanoutExchange")
    public ResultBean fanoutExchange() {
        for (int i = 0; i < 10; i++) {
            rabbitTemplate.convertAndSend(RabbitFanoutConfig.FANOUTNAME, null, "hello-fanout" + i);
        }
        return ResultBean.success();
    }

    @ApiOperation("RabbitMq（TopicExchange）")
    @RequestMapping("topicExchange")
    public ResultBean topicExchange() {
        rabbitTemplate.convertAndSend(RabbitTopicConfig.TOPICNAME, "xiaomi.news", "小米新闻");
        rabbitTemplate.convertAndSend(RabbitTopicConfig.TOPICNAME, "huawei.news", "华为新闻");
        rabbitTemplate.convertAndSend(RabbitTopicConfig.TOPICNAME, "phone.news", "手机新闻");
        rabbitTemplate.convertAndSend(RabbitTopicConfig.TOPICNAME, "huawei.phone", "华为手机");
        rabbitTemplate.convertAndSend(RabbitTopicConfig.TOPICNAME, "xiaomi.phone", "小米手机");
        return ResultBean.success();
    }

    @ApiOperation("直接输出文字测试")
    @RequestMapping("test")
    public String hello() {
        return "hello测试文字";
    }

    @ApiOperation("邮件发送测试")
    @RequestMapping("sendSimpleMail")
    public String sendSimpleMail() {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setSubject("测试邮件");
        mailMessage.setFrom(fromEmailAddress);
        mailMessage.setTo(toEmailAddress);
        mailMessage.setText("测试内容");
        mailMessage.setSentDate(new Date());
        mailSender.send(mailMessage);
        return "发送成功";
    }

    @ApiOperation("数据库备份并邮件发送测试")
    @RequestMapping("sendFileMail")
    public String sendFileMail() {
        Instant startBackup = Instant.now();
        Process process = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        try {
            // 获取项目根目录
            ApplicationHome home = new ApplicationHome();
            // 组织命令并执行 win 平台运行需要 cmd /c
            // mysqldump -h127.0.0.1 -uroot -p123456 --default-character-set=utf8 local_test > backupfile.sql
            // mysql -h127.0.0.1 -uroot -p123456 --default-character-set=utf8 local_test < backupfile.sql
            // docker exec -it root mysqldump -uroot -p123456 -P3306 --default-character-set=utf8 local_test > backupfile.sql
            // docker exec -it root mysqldump -uroot -p123456 -P3306 --default-character-set=utf8 local_test < backupfile.sql
            // jdbc:mysql://172.10.8.239:3306/cihong?
            String url = datasourceUrl.substring(datasourceUrl.indexOf("//") + 2, datasourceUrl.indexOf("?"));
            String[] split = url.split("/");
            String[] ipAndPort = split[0].split(":");
            StringBuilder command = new StringBuilder();
            if (System.getProperty("os.name").toLowerCase().contains("win")){
                command.append("cmd /c ");
            }
            command.append("mysqldump -h").append(ipAndPort[0])
                    .append(" -u").append(username)
                    .append(" -p").append(password)
                    .append(" -P").append(ipAndPort[1])
                    .append(" --default-character-set=utf8 ").append(split[1]).append(" > backupfile.sql");
            process = Runtime.getRuntime().exec(command.toString(), new String[]{}, home.getDir());
            // 获取返回结果
            inputStreamReader = new InputStreamReader(process.getErrorStream(), StandardCharsets.UTF_8);
            bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder builder = new StringBuilder();
            String line;
            while (null != (line = bufferedReader.readLine())) {
                if (builder.length() > 0){
                    builder.append("  |  ");
                }
                builder.append(line);
            }
            int exit = process.waitFor();
            // 邮件正文
            String context;
            File file = null;
            if (exit == 0) {
                context = "完成备份时间：" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                // 获取备份好的sql文件
                file = new File(home.toString() + File.separator + "backupfile.sql");
            } else {
                context = builder.length() > 0 ? builder.toString() : "备份失败，请联系管理员修复。";
            }
            // 发送邮件
            Instant startSendMail = Instant.now();
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(fromEmailAddress);
            helper.setTo(toEmailAddress);
            helper.setSubject("数据库备份文件定时推送");
            helper.setText(context, true);
            if (file != null && file.exists()){
                // 添加附件
                FileSystemResource fileSystemResource = new FileSystemResource(file);
                helper.addAttachment("backupfile.sql", fileSystemResource);
            }
            mailSender.send(message);
            Instant endSendMail = Instant.now();
            log.info("===============【数据库SQL备份成功】================");
            log.info("执行命令：" + command);
            log.info("执行结果：" + context);
            log.info("邮件发送方：" + fromEmailAddress);
            log.info("邮件接收方：：" + String.join(" | ", toEmailAddress));
            log.info("备份用时：" + Duration.between(startBackup, startSendMail).toMillis());
            log.info("发送邮件用时：" + Duration.between(startBackup, endSendMail).toMillis());
            log.info("===============【数据库SQL备份成功】================");
            return "发送成功";
        } catch (Exception e) {
            log.error("===============【数据库SQL备份失败】================");
            e.printStackTrace();
            log.error("===============【数据库SQL备份失败】================");
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                if (inputStreamReader != null) {
                    inputStreamReader.close();
                }
            } catch (Exception e) {
                //e.printStackTrace();
            }
            if (process != null) {
                process.destroy();
            }
        }
        return "发送失败";
    }

}
