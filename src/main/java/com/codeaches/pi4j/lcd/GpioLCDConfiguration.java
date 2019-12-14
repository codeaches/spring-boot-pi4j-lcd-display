package com.codeaches.pi4j.lcd;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.pi4j.component.lcd.impl.GpioLcdDisplay;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.wiringpi.Gpio;

@Configuration
public class GpioLCDConfiguration {

  Logger log = LoggerFactory.getLogger(GpioLCDConfiguration.class);

  public final static int LCD_ROW_1 = 0;
  public final static int LCD_ROW_2 = 1;

  @Bean
  public GpioController gpioController() {
    System.out.println("GpioController instance created");
    return GpioFactory.getInstance();
  }

  @Autowired
  GpioController gpioController;

  @Bean("lcd")
  public GpioLcdDisplay lcd() {

    // Setup wiringPi
    if (Gpio.wiringPiSetupGpio() == -1) {
      throw new RuntimeException("Gpio.wiringPiSetupGpio failed");
    }

    return new GpioLcdDisplay(2, // number of rows supported by LCD
        16, // number of columns supported by LCD
        RaspiPin.GPIO_09, // LCD RS pin
        RaspiPin.GPIO_08, // LCD strobe pin
        RaspiPin.GPIO_07, // LCD data bit D4
        RaspiPin.GPIO_15, // LCD data bit D5
        RaspiPin.GPIO_16, // LCD data bit D6
        RaspiPin.GPIO_01); // LCD data bit D7
  }

  @PreDestroy
  void preDestroy() {

    if (!gpioController().isShutdown()) {
      gpioController().shutdown();
    }
  }

}