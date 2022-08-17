package com.example.project.config;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AMQPConfig {

  @Value("${amqp.notification.exchange}")
  private String notificationExchange;

  @Value("${amqp.notification.routingkeys.email}")
  private String emailRoutingKey;

  @Value("${amqp.notification.routingkeys.sms}")
  private String smsRoutingKey;

  @Value("${amqp.notification.queues.email}")
  private String emailQueue;

  @Value("${amqp.notification.queues.sms}")
  private String smsQueue;

  @Bean
  public Queue emailQueue() {
    return new Queue(emailQueue, false);
  }

  @Bean
  public Queue smsQueue() {
    return new Queue(smsQueue, false);
  }

  @Bean
  public TopicExchange notificationExchange() {
    return new TopicExchange(notificationExchange);
  }

  @Bean
  public Binding notificationEmailBinding(Queue emailQueue, TopicExchange notificationExchange) {
    return BindingBuilder.bind(emailQueue)
        .to(notificationExchange)
        .with(emailRoutingKey);
  }

  @Bean
  public Binding notificationSmsBinding(Queue smsQueue, TopicExchange notificationExchange) {
    return BindingBuilder.bind(smsQueue)
        .to(notificationExchange)
        .with(smsRoutingKey);
  }

  @Bean
  public MessageConverter jsonMessageConverter() {
    return new Jackson2JsonMessageConverter();
  }

  @Bean
  public AmqpTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
    var rabbitTemplate = new RabbitTemplate(connectionFactory);
    rabbitTemplate.setMessageConverter(jsonMessageConverter());
    return rabbitTemplate;
  }
}
