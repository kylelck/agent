package com.lick;

import com.lick.transform.PerformMonitorTransformer;

import java.lang.instrument.Instrumentation;

/**
 * Hello world!
 *
 */
public class App 
{

   public static void premain(String agentArgs, Instrumentation inst) {
       System.out.printf("this is an agent.");
       PerformMonitorTransformer transformer = new PerformMonitorTransformer();
       inst.addTransformer(transformer);
       System.out.printf("args:"+agentArgs+"\n");
   }
   public static void premain(String agentArgs) {

   }

}
