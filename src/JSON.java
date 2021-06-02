import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import company.Forest;
import company.MyList;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class JSON {

    private static final String USER_AGENT = "Mozilla/5.0";


    private static final String GET_URL = "http://api.openweathermap.org/data/2.5/forecast?lat=48.9215&lon=24.7097&APPID=d5757b415990193c8251446c3ae98e07";

    public static void main(String[] args) throws IOException {
        start();
    }
    static void start() {
        System.out.println("Введіть місто:");
        Scanner sc = new Scanner(System.in);
        String city = sc.next();
        try {
            //api.openweathermap.org/data/2.5/forecast?q={city name}&appid={API key}
            URL obj = new URL("http://api.openweathermap.org/data/2.5/forecast?q=" + city + "&appid=d5757b415990193c8251446c3ae98e07");
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuffer response = new StringBuffer();
            String inLine;
            while ((inLine = in.readLine()) != null) {
                response.append(inLine);
            }
            in.close();
            Gson gson = (new GsonBuilder()).excludeFieldsWithoutExposeAnnotation().create();
            Forest data = (Forest) gson.fromJson(response.toString(), Forest.class);
            System.out.println("Введіть кількість потрібних результатів(макс 40):");
            Scanner r = new Scanner(System.in);
            int a = r.nextInt();
            if (a < 1) { a = 5;}
            if (a > 40) { a = 40;}
            System.out.println("Місто: " + city + "\t Results: " + a);
            System.out.println("Date       Time     Mint MaxT   Weather");
            for (int i = 0; i < a; i++) {
                if (data.getList() != null) {
                    System.out.print((data.getList().get(i)).getDtTxt() + "|");
                    System.out.printf("%.0f\t", (data.getList().get(i)).getMain().getTempMin() - 273.15D);
                    System.out.print("|");
                    System.out.printf("%.0f\t\t", (data.getList().get(i)).getMain().getTempMax() - 273.15D);
                    System.out.print("|");
                    System.out.print(data.getList().get(i).getWeather().get(0).getMain());
                    System.out.println();

                }
            }

            FileWriter file = new FileWriter("Result.txt", true);
            String line;
            file.write("City:" + city + "\t Results: " + a + "\n\n");
            file.write(("Date       Time     Mint MaxT   Weather\n"));
            for (int i = 0; i < a; i++) {
                file.write((data.getList().get(i)).getDtTxt() + "|");
                file.write(String.valueOf(Math.round(((data.getList().get(i)).getMain().getTempMin() - 273.15D) * 100) / 100) + "\t|");
                file.write(String.valueOf(Math.round(((data.getList().get(i)).getMain().getTempMax() - 273.15D) * 100) / 100) + "\t\t|");
                file.write(data.getList().get(i).getWeather().get(0).getMain() + "\n");
            }

            file.write("------------------------------------------\n");
            file.close();
        } catch (IOException e) {
            System.out.println("Некорректна назва населеного пункту.");
            start();
        }

    }
}