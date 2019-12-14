package com.codeaches.pi4j.lcd;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pi4j.component.lcd.LCDTextAlignment;
import com.pi4j.component.lcd.impl.GpioLcdDisplay;

@RestController
public class MyRestController {

  Logger log = LoggerFactory.getLogger(MyRestController.class);

  @Autowired
  public GpioLcdDisplay lcd;

  @GetMapping("/writeToLCD")
  public void writeToLCD(@RequestParam String line1) {

    lcd.write(GpioLCDConfiguration.LCD_ROW_1, line1, LCDTextAlignment.ALIGN_CENTER);
    log.info("Line 1 in LCD : " + line1);

    lcd.write(GpioLCDConfiguration.LCD_ROW_2, line1, LCDTextAlignment.ALIGN_CENTER);
    log.info("Line 2 in LCD : " + line1);
  }
}