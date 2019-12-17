package com.codeaches.pi4j.lcd;

import java.text.SimpleDateFormat;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.pi4j.component.lcd.LCDTextAlignment;
import com.pi4j.component.lcd.impl.GpioLcdDisplay;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.RaspiPin;

@Configuration
public class GpioLCDConfiguration {

  Logger log = LoggerFactory.getLogger(GpioLCDConfiguration.class);

  public final static int LCD_ROWS = 2;

  public final static int LCD_COLUMNS = 16;
  public final static int LCD_ROW_1 = 0;
  public final static int LCD_ROW_2 = 1;

  public final static int LCD_BITS = 4;

  @Bean("lcd")
  public GpioLcdDisplay lcd() throws Exception {

    System.out.println("<--Pi4J--> GPIO 4 bit LCD example program");

    // create gpio controller
    final GpioController gpio = GpioFactory.getInstance();

    // initialize LCD
    final GpioLcdDisplay lcd = new GpioLcdDisplay(//
        LCD_ROWS, // number of row supported by LCD
        LCD_COLUMNS, // number of columns supported by LCD
        RaspiPin.GPIO_11, // LCD RS pin
        RaspiPin.GPIO_10, // LCD strobe pin/EN pin
        RaspiPin.GPIO_00, // LCD data bit 1
        RaspiPin.GPIO_01, // LCD data bit 2
        RaspiPin.GPIO_02, // LCD data bit 3
        RaspiPin.GPIO_03); // LCD data bit 4

    // provision gpio pins as input pins with its internal pull up resistor enabled
    final GpioPinDigitalInput myButtons[] = {
        gpio.provisionDigitalInputPin(RaspiPin.GPIO_13, "B1", PinPullResistance.PULL_UP),
        gpio.provisionDigitalInputPin(RaspiPin.GPIO_07, "B2", PinPullResistance.PULL_UP),
        gpio.provisionDigitalInputPin(RaspiPin.GPIO_04, "B3", PinPullResistance.PULL_UP),
        gpio.provisionDigitalInputPin(RaspiPin.GPIO_12, "B4", PinPullResistance.PULL_UP) };

    // create and register gpio pin listener
//    gpio.addListener(new GpioPinListenerDigital() {
//      @Override
//      public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
//        if (event.getState() == PinState.LOW) {
//          lcd.writeln(LCD_ROW_2, event.getPin().getName() + " PRESSED", LCDTextAlignment.ALIGN_CENTER);
//        } else {
//          lcd.writeln(LCD_ROW_2, event.getPin().getName() + " RELEASED", LCDTextAlignment.ALIGN_CENTER);
//        }
//      }
//    }, myButtons);

    // clear LCD
    lcd.clear();
    Thread.sleep(1000);

    // write line 1 to LCD
    lcd.write(LCD_ROW_1, "The Pi4J Project");

    // write line 2 to LCD
    lcd.write(LCD_ROW_2, "----------------");

    // line data replacement
    for (int index = 0; index < 5; index++) {
      lcd.write(LCD_ROW_2, "----------------");
      Thread.sleep(500);
      lcd.write(LCD_ROW_2, "****************");
      Thread.sleep(500);
    }
    lcd.write(LCD_ROW_2, "----------------");

    // single character data replacement
    for (int index = 0; index < lcd.getColumnCount(); index++) {
      lcd.write(LCD_ROW_2, index, ">");
      if (index > 0)
        lcd.write(LCD_ROW_2, index - 1, "-");
      Thread.sleep(300);
    }
    for (int index = lcd.getColumnCount() - 1; index >= 0; index--) {
      lcd.write(LCD_ROW_2, index, "<");
      if (index < lcd.getColumnCount() - 1)
        lcd.write(LCD_ROW_2, index + 1, "-");
      Thread.sleep(300);
    }

    // left alignment, full line data
    lcd.write(LCD_ROW_2, "----------------");
    Thread.sleep(500);
    lcd.writeln(LCD_ROW_2, "<< LEFT");
    Thread.sleep(1000);

    // right alignment, full line data
    lcd.write(LCD_ROW_2, "----------------");
    Thread.sleep(500);
    lcd.writeln(LCD_ROW_2, "RIGHT >>", LCDTextAlignment.ALIGN_RIGHT);
    Thread.sleep(1000);

    // center alignment, full line data
    lcd.write(LCD_ROW_2, "----------------");
    Thread.sleep(500);
    lcd.writeln(LCD_ROW_2, "<< CENTER >>", LCDTextAlignment.ALIGN_CENTER);
    Thread.sleep(1000);

    // mixed alignments, partial line data
    lcd.write(LCD_ROW_2, "----------------");
    Thread.sleep(500);
    lcd.write(LCD_ROW_2, "<L>", LCDTextAlignment.ALIGN_LEFT);
    lcd.write(LCD_ROW_2, "<R>", LCDTextAlignment.ALIGN_RIGHT);
    lcd.write(LCD_ROW_2, "CC", LCDTextAlignment.ALIGN_CENTER);
    Thread.sleep(3000);

    SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");

    // update time
//    while (true) {
//      // write time to line 2 on LCD
//      if (gpio.isHigh(myButtons)) {
//        lcd.writeln(LCD_ROW_2, formatter.format(new Date()), LCDTextAlignment.ALIGN_CENTER);
//      }
//      Thread.sleep(1000);
//    }
    return lcd;
  }
  // stop all GPIO activity/threads by shutting down the GPIO controller
  // (this method will forcefully shutdown all GPIO monitoring threads and
  // scheduled tasks)
  // gpio.shutdown(); <--- implement this method call if you wish to terminate the
  // Pi4J GPIO controller}

  @PreDestroy
  void preDestroy() {
//
//    lcd().clear(LCD_ROW_1);
//    lcd().clear(LCD_ROW_2);

    log.info("LCD rows cleared");
  }
}