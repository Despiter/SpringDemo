package com.martin.spring;

import com.sun.xml.internal.ws.util.StringUtils;

import java.io.File;
import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;

public class MartinApplicationContext {

    private Class<?> configClass;

    private ConcurrentHashMap<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();

    public MartinApplicationContext(Class<?> configClass) {
        this.configClass = configClass;

        //扫描
        if (configClass.isAnnotationPresent(ComponentScan.class)) {
            ComponentScan componentScanAnnotation = configClass.getAnnotation(ComponentScan.class);
            String path = componentScanAnnotation.value();//扫描路径
            String winPath = path.replace(".", "/");
            ClassLoader classLoader = MartinApplicationContext.class.getClassLoader();
            URL resource = classLoader.getResource(winPath);
            assert resource != null;
            File file = new File(resource.getFile());
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (File f : files) {
                    if (f.getName().endsWith(".class")) {
                        String fileName = f.getName();
                        try {
                            Class<?> clazz = classLoader.loadClass(path + "." + fileName.replace(".class", ""));
                            String beanName;
                            if (clazz.isAnnotationPresent(Component.class)) {
                                Component component = clazz.getAnnotation(Component.class);
                                beanName = component.value();
                                // BeanDefinition
                                BeanDefinition beanDefinition = new BeanDefinition();
                                beanDefinition.setType(clazz);
                                if (clazz.isAnnotationPresent(Scope.class)) {
                                    Scope annotation = clazz.getAnnotation(Scope.class);
                                    beanDefinition.setScope(annotation.value());
                                } else {
                                    beanDefinition.setScope("singleton");
                                }
                                beanDefinitionMap.put(beanName.equalsIgnoreCase("") ? clazz.getName() : beanName, beanDefinition);
                            }
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    public Object getBean(String beanName) {


        return null;
    }

}
