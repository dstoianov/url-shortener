package se.techinsight.urlshortener.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class MetaDataContributor implements InfoContributor {

    @Autowired
    private ApplicationContext ctx;

    @Override
    public void contribute(Info.Builder builder) {
        Map<String, Object> details = new HashMap<>();
        details.put("bean-definition-count", ctx.getBeanDefinitionCount());
        details.put("startup-date", new Date(ctx.getStartupDate()));

        builder.withDetail("context", details);
    }
}