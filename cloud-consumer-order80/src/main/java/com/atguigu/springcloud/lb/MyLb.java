package com.atguigu.springcloud.lb;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class MyLb implements LoadBalancer {

    //atomic原子类
    private AtomicInteger atomicInteger = new AtomicInteger(0);

    //获得第几次访问
    private final int getAndIncrement(){
        int current;
        int next;
        do {
            current = this.atomicInteger.get();
            next = current >= 2147483647 ? 0 : current + 1;
        }while (!this.atomicInteger.compareAndSet(current,next));  //第一个参数是期望值，第二个参数是修改值是
        System.out.println("*******第几次访问，次数next: "+next);
        return next;
    }

    /**
     * 获取需要访问的服务器
     * @param serviceInstances   服务器列表
     */
    @Override
    public ServiceInstance instances(List<ServiceInstance> serviceInstances) {
        //得到服务器列表的下标位置
        int index=getAndIncrement()%serviceInstances.size();
        return serviceInstances.get(index);
    }
}
