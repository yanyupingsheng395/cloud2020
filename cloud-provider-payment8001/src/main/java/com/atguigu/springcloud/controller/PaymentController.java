package com.atguigu.springcloud.controller;

import com.atguigu.springcloud.entities.CommonResult;
import com.atguigu.springcloud.entities.Payment;
import com.atguigu.springcloud.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
public class PaymentController {
    @Resource
    private PaymentService paymentService;
    @Value("${server.port}")
    private String serverport;
    //对于注册到eureka中的服务。可以通过这个服务发现，获取该服务的信息
    @Resource
    private DiscoveryClient discoveryClient;

    @PostMapping(value = "/payment/create")
    public CommonResult create(@RequestBody Payment payment) {
        int result = paymentService.create(payment);
        if(result>0){
            return new CommonResult(200,"插入数据库成功,serverport="+serverport,result);
        }else {
            return new CommonResult(404,"插入数据库失败",null);
        }
    }

    @GetMapping(value = "/payment/get/{id}")
    public CommonResult getPaymentById(@PathVariable("id") Long id) {
        Payment payment = paymentService.getPaymentById(id);
        int a =0;
        if(payment!=null){

            return new CommonResult(200,"查询成功"+a,payment+"-->serverport="+serverport);
        }else{
            return  new CommonResult(404,"有对应记录，查询ID"+id,null);
        }
    }

    @GetMapping(value = "/payment/discovery")
    public Object discovery(){
        //获取所有注册到eureka中的微服务
        List<String> services = discoveryClient.getServices();
        for (String element : services) {
            log.info("***** element:"+element);
        }
        //通过一个微服务获取到这个微服务的所有信息
        List<ServiceInstance> instances = discoveryClient.getInstances("CLOUD-PAYMENT-SERVICE");
        for (ServiceInstance instance : instances) {
            log.info("***服务IP地址"+instance.getHost()+"\t***服务名称"+instance.getServiceId()+"\t***服务端口号"+instance.getPort()+"\t**服务url" +
                    instance.getUri());
        }
        /**
         * 输出内容
         * ***** element:cloud-payment-service
         * ***** element:cloud-order-service
         * ***服务IP地址192.168.239.1***服务名称CLOUD-PAYMENT-SERVICE ***服务端口号8002**服务url http://192.168.239.1:8002
         * ***服务IP地址192.168.239.1***服务名称CLOUD-PAYMENT-SERVICE ***服务端口号8001**服务url http://192.168.239.1:8001
         */
        return this.discoveryClient;
    }

    @GetMapping("/payment/lb")
    public  String getPayment(){
        return  serverport;
    }

    /**
     * 故意写成请求3秒。表示生产中，这个方法处理业务很复杂耗时
     */
    @GetMapping(value = "/payment/feign/timeout")
    public String paymentFeignTimeout(){
        try { TimeUnit.SECONDS.sleep(3); }catch (Exception e) {e.printStackTrace();}
        return serverport;
    }

    @GetMapping("/payment/zipkin")
    public String paymentZipkin()
    {
        return "hi ,i'am paymentzipkin server fall back，welcome to atguigu，O(∩_∩)O哈哈~";
    }

}
