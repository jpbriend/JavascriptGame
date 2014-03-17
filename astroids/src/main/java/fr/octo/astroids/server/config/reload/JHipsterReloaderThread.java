package fr.octo.astroids.server.config.reload;

import fr.octo.astroids.server.config.reload.reloader.JacksonReloader;
import fr.octo.astroids.server.config.reload.reloader.LiquibaseReloader;
import fr.octo.astroids.server.config.reload.reloader.SpringReloader;
import org.apache.commons.lang.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;

/**
 * This thread stores classes to reload, to reload them all in one batch.
 */
public class JHipsterReloaderThread implements Runnable {

    private static Logger log = LoggerFactory.getLogger(JHipsterReloaderThread.class);

    private static Object lock = new Object();

    public static boolean isStarted;

    private static boolean hotReloadTriggered = false;

    private static boolean isWaitingForNewClasses = false;

    /**
     * How long does the thread wait until running a new batch.
     */
    private static final int BATCH_DELAY = 250;

    /**
     * Reloads Spring beans.
     */
    private static SpringReloader springReloader;

    /**
     * Reloads Jackson classes.
     */
    private static JacksonReloader jacksonReloader;

    /**
     * Reloads Database, Entity and Hibernate Factory
     */
    private static LiquibaseReloader liquibaseReloader;

    /**
     * Stores the Spring controllers reloaded in the batch.
     */
    private List<Class> controllers = new ArrayList<>();

    /**
     * Stores the Spring services reloaded in the batch.
     */
    private List<Class> services = new ArrayList<>();

    /**
     * Stores the Spring repositories reloaded in the batch.
     */
    private List<Class> repositories = new ArrayList<>();

    /**
     * Stores the Spring components reloaded in the batch.
     */
    private List<Class> components = new ArrayList<>();

    /**
     * Stores the JPA entities reloaded in the batch.
     */
    private List<Class> entities = new ArrayList<>();

    /**
     * Stores the DTOs reloaded in the batch.
     */
    private List<Class> dtos = new ArrayList<>();

    public JHipsterReloaderThread(ConfigurableApplicationContext applicationContext) {
        isStarted = true;
        springReloader = new SpringReloader(applicationContext);
        springReloader.afterPropertiesSet();
        jacksonReloader = new JacksonReloader(applicationContext);
        liquibaseReloader = new LiquibaseReloader(applicationContext);
    }

    public void reloadEvent(String typename, Class<?> clazz) {
        synchronized (lock) {
            log.trace("Hot reloading - checking if this is a Spring bean: {}", typename);

            boolean startReloading = false;
            if (AnnotationUtils.findAnnotation(clazz, Repository.class) != null ||
                    ClassUtils.isAssignable(clazz, org.springframework.data.repository.Repository.class)) {
                log.trace("{} is a Spring Repository", typename);
                repositories.add(clazz);
                startReloading = true;
            } else if (AnnotationUtils.findAnnotation(clazz, Service.class) != null) {
                log.trace("{} is a Spring Service", typename);
                services.add(clazz);
                startReloading = true;
            } else if (AnnotationUtils.findAnnotation(clazz, Controller.class) != null ||
                    AnnotationUtils.findAnnotation(clazz, RestController.class) != null) {
                log.trace("{} is a Spring Controller", typename);
                controllers.add(clazz);
                startReloading = true;
            } else if (AnnotationUtils.findAnnotation(clazz, Component.class) != null) {
                log.trace("{} is a Spring Component", typename);
                components.add(clazz);
                startReloading = true;
            } else if (typename.startsWith("fr.octo.astroids.server.domain")) {
                log.trace("{} is in the JPA package, checking if it is an entity", typename);
                if (AnnotationUtils.findAnnotation(clazz, Entity.class) != null) {
                    log.trace("{} is a JPA Entity", typename);
                    entities.add(clazz);
                    startReloading = true;
                }
            } else if (typename.startsWith("fr.octo.astroids.server.web.rest.dto")) {
                log.debug("{}  is a REST DTO", typename);
                dtos.add(clazz);
                startReloading = true;
            }

            if (startReloading) {
                hotReloadTriggered = true;
                isWaitingForNewClasses = true;
            }
        }
    }

    public void run() {
        while (isStarted) {
            try {
                Thread.sleep(BATCH_DELAY);
                if (hotReloadTriggered) {
                    if (isWaitingForNewClasses) {
                        log.info("Batch reload has been triggered, waiting for new classes for {} ms", BATCH_DELAY);
                        isWaitingForNewClasses = false;
                    } else {
                        batchReload();
                        hotReloadTriggered = springReloader.hasBeansToReload();
                    }
                } else {
                    log.trace("Waiting for batch reload");
                }
            } catch (InterruptedException e) {
                log.error("JHipsterReloaderThread was awaken", e);
            }
        }
    }

    private void batchReload() {
        synchronized (lock) {
            log.info("Batch reload in progress...");
            if (entities.size() > 0 || dtos.size() > 0) {
                log.debug("There are {} entities and {} dtos updated, invalidating Jackson cache",
                        entities.size(), dtos.size());

                jacksonReloader.reloadEvent();

                if (entities.size() > 0) {
                    liquibaseReloader.reloadEvent(entities);
                    springReloader.hasNewEntityBean();
                    entities.clear();
                }
            }
            addSpringBeans("repositories", repositories);
            addSpringBeans("services", services);
            addSpringBeans("components", components);
            addSpringBeans("controllers", controllers);

            // Start to reload all Spring beans
            if (springReloader.hasBeansToReload()) {
                springReloader.start();
            }
        }
    }

    private void addSpringBeans(String type, List<Class> list) {
        if (list.size() > 0) {
            log.debug("There are {} Spring {} updated, adding them to be reloaded", list.size(), type);
            for (Class clazz : list) {
                springReloader.reloadEvent(clazz);
            }
        }
        list.clear();
    }

    /**
     * Register the thread and starts it.
     */
    public static void register(JHipsterReloaderThread jHipsterReloaderThread) {
        try {
            final Thread thread = new Thread(jHipsterReloaderThread);
            thread.setDaemon(true);
            thread.start();

            Runtime.getRuntime().addShutdownHook(new Thread() {
                public void run() {
                    JHipsterReloaderThread.isStarted = false;
                    try {
                        thread.join();
                    } catch (InterruptedException e) {
                        log.error("Failed during the JVM shutdown", e);
                    }
                }
            });
        } catch (Exception e) {
            log.error("Failed to start the reloader thread. Classes will not be reloaded correctly.", e);
        }
    }
}
