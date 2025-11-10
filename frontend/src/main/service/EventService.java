package service;

import main.model.Event;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class EventService {

    private static final String API_URL = "http://localhost:8080/event";

    public List<Event> listarEventos() {
        List<Event> eventos = new ArrayList<>();
        try {
            URL url = new URL(API_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) response.append(line);
            in.close();

            JSONObject json = new JSONObject(response.toString());
            JSONArray data = json.getJSONArray("data");

            for (int i = 0; i < data.length(); i++) {
                JSONObject evt = data.getJSONObject(i);
                Event e = new Event();
                e.setId(evt.getLong("id"));
                e.setName(evt.getString("name"));
                e.setLocalizationAddress(evt.getString("localizationAddress"));
                e.setLocalizationNeighborhood(evt.getString("localizationNeighborhood"));
                e.setPrice(evt.getDouble("price"));
                eventos.add(e);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return eventos;
    }
}
