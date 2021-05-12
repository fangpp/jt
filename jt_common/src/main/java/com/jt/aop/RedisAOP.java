package com.jt.aop;

import com.jt.annotation.CacheFind;
import com.jt.util.ObjectMapperUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.ShardedJedis;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

@Component  //将对象交给Spring容器管理
@Aspect     //标识AOP切面
public class RedisAOP {

    @Autowired
    //private Jedis jedis;          //单台redis
    //private ShardedJedis jedis;     //分片redis  内存扩容
    private JedisCluster jedis;

    //通知选择: 是否控制目标方法是否执行.  环绕通知
    //切入点表达式: 控制注解 @annotation(语法....)

    /**
     * 需求: 如何动态获取注解中的属性值.
     * 原理: 反射机制
     *       获取目标对象~~~~~获取方法对象~~~获取注解  原始API
     *
     * @param joinPoint
     * @return
     * @throws Throwable
     *
     * 向上造型:  父类  = 子类
     * 向下造型:  子类  = (强制类型转化)父类
     *
     * AOP中的语法规范1.:
     *   如果通知方法有参数需要添加,则joinPoint 必须位于第一位.
     *   报错信息: error at ::0 formal unbound in pointcut
     * AOP中的语法规范3:
     *   如果需要动态接受注解对象,则在切入点表达式中直接写注解参数名称即可
     *   虽然看到的是名称,但是解析时变成了包名.类型
     */
    @Around("@annotation(cacheFind)")
    public Object around(ProceedingJoinPoint joinPoint,CacheFind cacheFind) throws Throwable {
        Object result = null;
        //1.获取key="ITEM_CAT_PARENTID"
        String key = cacheFind.key();
        //2.动态拼接key 获取参数信息
        String args = Arrays.toString(joinPoint.getArgs());
        key += "::" + args;
        //3.redis缓存实现
        if(jedis.exists(key)){
            String json = jedis.get(key);
            //target标识返回值类型????
            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
            Class returnType = methodSignature.getReturnType();
            result = ObjectMapperUtil.toObject(json, returnType);
            System.out.println("AOP缓存查询!!!");
        }else{
            //查询数据库 执行目标方法
            result = joinPoint.proceed();
            String json = ObjectMapperUtil.toJSON(result);
            if(cacheFind.seconds()>0)
                jedis.setex(key,cacheFind.seconds(),json);
            else
                jedis.set(key, json);
            System.out.println("AOP查询数据库");
        }
        return result;
    }



   /* @Around("@annotation(com.jt.annotation.CacheFind)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        //父转子  需要强转
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        CacheFind cacheFind = method.getAnnotation(CacheFind.class);
        String key = cacheFind.key();
        System.out.println("获取key:"+key);
        return joinPoint.proceed();
    }*/




    /*@Around("@annotation(com.jt.annotation.CacheFind)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {

        //1.获取目标对象类型
        Class targetClass = joinPoint.getTarget().getClass();
        //2.获取方法
        String name = joinPoint.getSignature().getName();
        Object[] objArgs = joinPoint.getArgs();
        Class[] classArgs = new Class[objArgs.length];
        for (int i=0;i<objArgs.length;i++){
            Object obj = objArgs[i];
            classArgs[i] = obj.getClass();
        }
        Method method = targetClass.getMethod(name,classArgs);
        CacheFind cacheFind = method.getAnnotation(CacheFind.class);
        String key = cacheFind.key();
        System.out.println(key);
        return joinPoint.proceed();
    }*/













    /**
     * 1.定义切入点表达式
     * bean: 被spring容器管理的对象称之为bean
     * 1.1 bean(bean的ID) 按类匹配  1个
     *      bean(itemCatServiceImpl)
     * 1.2 within(包名.类名) 按类匹配  一堆
     *      within(com.jt.service.*)
     * 1.3 execution(返回值类型 包名.类名.方法名(参数列表))
     *     execution(* com.jt.service..*.*(..))
     *     解释:  返回值为任意类型 com.jt.service包所有的子孙包的类
     *            类中的任意方法,任意参数
     *     execution(Integer com.jt.service..*.add*(int))
     *     execution(int com.jt.service..*.add*(int))
     */
   /* @Pointcut("execution(* com.jt.service..*.*(..))")
    public void pointCut(){

    }

    //如何理解什么是连接点? 被切入点拦截的方法
    //ProceedingJoinPoint is only supported for around advice
    //只有环绕通知可以控制目标方法
    @Before("pointCut()")
    public void before(JoinPoint joinPoint){
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getSignature().getDeclaringTypeName();
        Object[] args = joinPoint.getArgs();
        Object target = joinPoint.getTarget();
        System.out.println(methodName);
        System.out.println(className);
        System.out.println(args);
        System.out.println(target);
        System.out.println("我是一个前置通知");
    }

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("环绕开始");
        Object result =  joinPoint.proceed();    //执行下一个通知,目标方法
        System.out.println("环绕结束");
        return result;
    }*/


}
