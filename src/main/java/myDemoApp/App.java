
package myDemoApp;

import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import spark.ModelAndView;
import spark.template.mustache.MustacheTemplateEngine;

public class App {
public static int sum(ArrayList<Integer> array,Integer t,Integer e){ 
if(array==null | array.size()<2) return 0;
if(t==null | e==null) return 0;
if(t>=array.size() |  e>=array.size()) return 0;
for(int i: array)
    if(i<=0) return 0;
return array.get(t)+array.get(e);
}

public static void main(String[] args) {
  Logger logger = LogManager.getLogger(App.class);

  int port = Integer.parseInt(System.getenv("PORT"));
  port(port);
  logger.error("Current port number:" + port);


    port(getHerokuAssignedPort());

    get("/", (req, res) -> "Hello, World");

    post("/compute", (req, res) -> {
    

      String input1 = req.queryParams("input1");
      java.util.Scanner sc1 = new java.util.Scanner(input1);
      sc1.useDelimiter("[;\r\n]+");
      java.util.ArrayList<Integer> inputList = new java.util.ArrayList<>();
      while (sc1.hasNext())
      {
        int value = Integer.parseInt(sc1.next().replaceAll("\\s",""));
        inputList.add(value);
      }
      sc1.close();
      System.out.println(inputList);
      Integer input2AsInt;
      String input2 = req.queryParams("input2").replaceAll("\\s","");
      if(input2.equals(""))
        input2AsInt = null;
      else
        input2AsInt = Integer.parseInt(input2);

      Integer input3AsInt;
      String input3 = req.queryParams("input3").replaceAll("\\s","");
      if(input3.equals(""))
            input3AsInt = null;
      else
        input3AsInt = Integer.parseInt(input3);


      int result;
      if(input2AsInt!=null && input3AsInt!=null){
        result = App.sum(inputList, input2AsInt-1, input3AsInt-1);
      }
      else {
        result = App.sum(inputList, input2AsInt, input3AsInt);
      }


      Map<String, Integer> map = new HashMap<String, Integer>();
      map.put("result", result);
      return new ModelAndView(map, "compute.mustache");
    }, new MustacheTemplateEngine());


    get("/compute",
        (rq, rs) -> {
          Map<String, String> map = new HashMap<String, String>();
          map.put("result", "not computed yet!");
          return new ModelAndView(map, "compute.mustache");
        },
        new MustacheTemplateEngine());
}

static int getHerokuAssignedPort() {
    ProcessBuilder processBuilder = new ProcessBuilder();
    if (processBuilder.environment().get("PORT") != null) {
        return Integer.parseInt(processBuilder.environment().get("PORT"));
    }
    return 4567; //return default port if heroku-port isn't set (i.e. on localhost)
}
}




