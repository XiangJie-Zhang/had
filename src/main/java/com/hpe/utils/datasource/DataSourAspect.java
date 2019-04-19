package com.hpe.utils.datasource;


// 就调用setDataSource方法来设置注解中指定的值，这样也就不需要手动设置了。下面是定义切面
// aop

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

public class DataSourAspect {


    public void before(JoinPoint point){
        Object target = point.getTarget();
        String method = point.getSignature().getName();

        Class<?>[] classz = target.getClass().getInterfaces();

        Class<?>[] parameterTypes = ((MethodSignature) point.getSignature())
                .getMethod().getParameterTypes();
        try {
            Method m = classz[0].getMethod(method, parameterTypes);
            if (m != null && m.isAnnotationPresent(DataSource.class)) {
                DataSource data = m
                        .getAnnotation(DataSource.class);
                DynamicDataSourceHolder.setDataSource(data.value());
                System.out.println(data.value());
            }

        } catch (Exception e) {
//            e.printStackTrace();
        }

    }










   /*
    public void intercept(JoinPoint point){
        Class<?> target = point.getTarget().getClass();
        MethodSignature signature = (MethodSignature) point.getSignature();
        //默认使用目标类型的注解，如果没有则实现接口的注解
        for(Class<?> clazz:target.getInterfaces()){
            resolveDataSource(clazz,signature.getMethod());
        }
        resolveDataSource(target,signature.getMethod());
    }


    private void resolveDataSource(Class<?> clazz, Method method){

        try {
            Class<?>[] types = method.getParameterTypes();
            //默认使用类型注解
            if(clazz.isAnnotationPresent(DataSource.class)){
                DataSource source = clazz.getAnnotation(DataSource.class);
                DynamicDataSourceHolder.setDataSource(source.value());
            }

            //方法注解可以覆盖类型注解
            Method m = clazz.getMethod(method.getName(),types);
            if(m!=null && m.isAnnotationPresent(DataSource.class)){
                DataSource source = m.getAnnotation(DataSource.class);
                DynamicDataSourceHolder.setDataSource(source.value());
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }*/
}
