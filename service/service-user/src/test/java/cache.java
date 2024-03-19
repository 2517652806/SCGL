import springfox.documentation.annotations.Cacheable;

import java.util.concurrent.CompletableFuture;


public class cache {

    public static void main(String[] args) {

//        new ThreadPoolExecutor(
//                5,
//                10,
//                3600,
//                TimeUnit.SECONDS
//
//        )
//        System.out.println(get());


        CompletableFuture<String> stringCompletableFuture = CompletableFuture.supplyAsync(() -> {
            try {
                System.out.println("执行结束1" + Thread.currentThread().getName());
                Thread.sleep(0);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            int a = 10/0;
            return "异步线程执行结束";
        }).whenComplete((v,e)->{
            if (e == null){
                System.out.println(v);
            }

        }).exceptionally(e-> {
            System.out.println(e.getMessage());
            return null;
        });



        CompletableFuture<Void> voidCompletableFuture = CompletableFuture.
                runAsync(() -> System.out.println("执行结束2" + Thread.currentThread().getName()));
//        voidCompletableFuture.w1


        System.out.println("主线程执行结束！！！！！");


    }

    @Cacheable("book:")
    public static String get() {
        return "kkkkkk";
    }
}
