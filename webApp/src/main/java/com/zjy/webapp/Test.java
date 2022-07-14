package com.zjy.webapp;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Getter
@Setter
public class Test {
    private Integer stock = 6;
    private BlockingQueue<RequestPromise> queue = new LinkedBlockingQueue<>(10);

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        Test test = new Test();
        test.mergeJob();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        List<Future<Result>> futuresList = new ArrayList<>();
        CountDownLatch countDownLatch = new CountDownLatch(10);
        for(int i = 0; i < 10; i++) {
            final Long orderId = i + 100L;
            final Long userId = Long.valueOf(i);
            Future<Result> future = executorService.submit(() -> {
                countDownLatch.countDown();
                countDownLatch.await();
                return test.opearte(new UserRequest(userId, orderId, 1));
            });
            futuresList.add(future);
        }
        futuresList.forEach(future -> {
            try {
                Result result = null;
                try {
                    result = future.get(300, TimeUnit.MILLISECONDS);
                } catch (ExecutionException | TimeoutException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + "客户端请求响应" + result);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    public Result opearte(UserRequest userRequest) {
        RequestPromise requestPromise = new RequestPromise(userRequest, null);
        boolean enqueueSuccess = false;
        synchronized (requestPromise) {
            try {
                enqueueSuccess = queue.offer(requestPromise, 100, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(!enqueueSuccess) {
                return new Result(false, "系统繁忙");
            }
            try{
                requestPromise.wait(200);
                if(requestPromise.getResult() == null) {
                    return new Result(false, "超时");
                }
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        return requestPromise.getResult();
    }

    public void mergeJob() {
        new Thread(() -> {
            List<RequestPromise> list;
            while(true) {
                if(queue.isEmpty()) {
                    try {
                        Thread.sleep(10);
                        continue;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                list = new ArrayList<>();
                int size = queue.size();
                for (int i = 0; i < size; i++) {
                    list.add(queue.poll());
                }
                System.out.println(Thread.currentThread().getName() + "合并库存：" + list);
                int sum = list.stream().mapToInt(e -> e.getUserRequest().getCount()).sum();
                if(sum <= stock) {
                    stock -= sum;
                    list.forEach(requestPromise -> {
                        requestPromise.setResult(new Result(true, "ok"));
                        synchronized (requestPromise) {
                            requestPromise.notify();
                        }
                    });
                    continue;
                }
                for (RequestPromise requestPromise : list) {
                    int count = requestPromise.getUserRequest().getCount();
                    if(count <= stock) {
                        stock -= count;
                        requestPromise.setResult(new Result(true, "ok"));
                    } else {
                        requestPromise.setResult(new Result(true, "库存不足"));
                    }
                    synchronized (requestPromise) {
                        requestPromise.notify();
                    }
                }
            }
        }, "mergerThread").start();
    }
}
@Getter
@Setter
class RequestPromise{
    private UserRequest userRequest;
    private Result result;

    public RequestPromise(UserRequest userRequest, Result result) {
        this.userRequest = userRequest;
        this.result = result;
    }

    @Override
    public String toString() {
        return "RequestPromise{" +
                "userRequest=" + userRequest +
                ", result=" + result +
                '}';
    }
}

@Getter
@Setter
class Result {
    private Boolean success;
    private String msg;

    public Result(Boolean success, String msg) {
        this.success = success;
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "Result{" +
                "success=" + success +
                ", msg='" + msg + '\'' +
                '}';
    }
}
@Getter
@Setter
class UserRequest {
    private Long userId;
    private Long orderId;
    private Integer count;

    public UserRequest(Long userId, Long orderId, Integer count) {
        this.userId = userId;
        this.orderId = orderId;
        this.count = count;
    }

    @Override
    public String toString() {
        return "UserRequest{" +
                "userId=" + userId +
                ", orderId=" + orderId +
                ", count=" + count +
                '}';
    }
}
