package org.tms.profile_service_soap.config;

import static org.tms.profile_service_soap.utils.Constant.NAMESPACE_URI;
import static org.tms.profile_service_soap.utils.Constant.SERVICE_ENDPOINT_LOCAL_URI;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;
import javax.annotation.PostConstruct;

@EnableWs
@Configuration
public class SoapEndpointConfig extends WsConfigurerAdapter {

    @Bean
    public ServletRegistrationBean<MessageDispatcherServlet> messageDispatcherServlet(ApplicationContext context) {
        MessageDispatcherServlet messageDispatcherServlet = new MessageDispatcherServlet();
        messageDispatcherServlet.setApplicationContext(context);
        messageDispatcherServlet.setTransformWsdlLocations(true);
        return new ServletRegistrationBean(messageDispatcherServlet, SERVICE_ENDPOINT_LOCAL_URI);
    }

    @Bean("profileDetailsWsdl")
    public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema profileSchema) {
        DefaultWsdl11Definition wsdlDefinition = new DefaultWsdl11Definition();
        wsdlDefinition.setPortTypeName("ProfileDetailsPort");

        wsdlDefinition.setLocationUri(SERVICE_ENDPOINT_LOCAL_URI);
        wsdlDefinition.setTargetNamespace(NAMESPACE_URI);
        wsdlDefinition.setSchema(profileSchema);
        return wsdlDefinition;
    }

    @Bean
    public XsdSchema profileSchema() {
        return new SimpleXsdSchema(new ClassPathResource("profile.xsd"));
    }



    /**
     * Fix warning: "WARN 9479 --- [nio-8450-exec-1] javax.xml.soap : Using deprecated META-INF/services mechanism with non-standard property:
     * javax.xml.soap.MetaFactory. Property javax.xml.soap.SAAJMetaFactory should be used instead."
     */
    @PostConstruct
    public void init() {
        System.setProperty("javax.xml.soap.SAAJMetaFactory", "com.sun.xml.messaging.saaj.soap.SAAJMetaFactoryImpl");
    }
}