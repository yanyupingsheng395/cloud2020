package com.atguigu.springcloud.alibaba.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class FlowLimitController
{
    @GetMapping("/testA")
    public String testA() {
        return "------testA";
    }

    @GetMapping("/testB")
    public String testB() {
        return "------testB";
    }
    @GetMapping("/testE")
    public String testE()
    {
        log.info("testE 测试异常数");
        int age = 10/0;
        return "------testE 测试异常数";
    }

    @GetMapping("/testHostKey")
    @SentinelResource(value = "testHostKey",blockHandler = "deal_testHostKey")//第一个value是代表这个唯一名字，第二个是兜底的方法名
    public String testHostKey(@RequestParam(value = "p1",required = false)String p1,
                              @RequestParam(value = "p2",required = false)String p2){
        int age=10/0;
        return "------testHostKey";
    }

    public String deal_testHostKey(String p1, String p2, BlockException bl) {
        return "deal_testHostKey--->这是兜底方法";  //默认提示：Blocked by Sentinel (flow limiting)
    }
}

