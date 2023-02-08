package com.increff.pdf.spring;


import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Configuration
@ComponentScan("com.increff.pdf")
@PropertySources({ //
        @PropertySource(value = "file:./pdf.properties", ignoreResourceNotFound = true) //
})
public class SpringConfig {
}
