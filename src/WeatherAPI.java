import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

// WeatherAPI 클래스: OpenWeatherMap API 연동
class WeatherAPI {
    private static final String API_KEY = "YOUR_OPENWEATHERMAP_API_KEY"; // <<--- 이곳을 당신의 API Key로 변경하세요
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/weather?";

    public String getCurrentWeather(String city) {
        if (API_KEY.equals("YOUR_OPENWEATHERMAP_API_KEY") || API_KEY.isEmpty()) {
            return "날씨 API 키가 설정되지 않았습니다.";
        }
        try {
            String urlString = BASE_URL + "q=" + URLEncoder.encode(city, StandardCharsets.UTF_8.toString()) +
                    "&appid=" + API_KEY + "&units=metric&lang=kr";
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                return parseWeatherJson(response.toString());
            } else {
                return "날씨 정보를 가져오는 데 실패했습니다. (응답 코드: " + responseCode + ")";
            }
        } catch (Exception e) {
            System.err.println("날씨 API 호출 중 오류 발생: " + e.getMessage());
            return "날씨 정보를 가져올 수 없습니다. 도시 이름을 확인하거나 인터넷 연결을 확인하세요.";
        }
    }

    private String parseWeatherJson(String jsonResponse) {
        try {
            int weatherStartIndex = jsonResponse.indexOf("\"weather\":");
            if (weatherStartIndex == -1) return "날씨 정보 파싱 실패";

            int arrayStartIndex = jsonResponse.indexOf("[", weatherStartIndex);
            int arrayEndIndex = jsonResponse.indexOf("]", arrayStartIndex);
            if (arrayStartIndex == -1 || arrayEndIndex == -1) return "날씨 정보 파싱 실패";

            String weatherArrayContent = jsonResponse.substring(arrayStartIndex + 1, arrayEndIndex);

            int mainStartIndex = weatherArrayContent.indexOf("\"main\":\"");
            int mainEndIndex = -1;
            String weatherMain = "알 수 없음";
            if (mainStartIndex != -1) {
                mainStartIndex += "\"main\":\"".length();
                mainEndIndex = weatherArrayContent.indexOf("\"", mainStartIndex);
                if (mainEndIndex != -1) {
                    weatherMain = weatherArrayContent.substring(mainStartIndex, mainEndIndex);
                }
            }

            int descStartIndex = weatherArrayContent.indexOf("\"description\":\"");
            int descEndIndex = -1;
            String weatherDescription = "알 수 없음";
            if (descStartIndex != -1) {
                descStartIndex += "\"description\":\"".length();
                descEndIndex = weatherArrayContent.indexOf("\"", descStartIndex);
                if (descEndIndex != -1) {
                    weatherDescription = weatherArrayContent.substring(descStartIndex, descEndIndex);
                }
            }

            int mainObjectStartIndex = jsonResponse.indexOf("\"main\":");
            double temperature = -999.0;
            if(mainObjectStartIndex != -1) {
                String mainContent = jsonResponse.substring(mainObjectStartIndex, jsonResponse.indexOf("}", mainObjectStartIndex));
                int tempKeyIndex = mainContent.indexOf("\"temp\":");
                if (tempKeyIndex != -1) {
                    int tempStartIndex = tempKeyIndex + "\"temp\":".length();
                    int tempEndIndex = mainContent.indexOf(",", tempStartIndex);
                    if (tempEndIndex == -1) tempEndIndex = mainContent.indexOf("}", tempStartIndex);
                    if (tempEndIndex != -1) {
                        temperature = Double.parseDouble(mainContent.substring(tempStartIndex, tempEndIndex));
                    }
                }
            }

            String emoji = getWeatherEmoji(weatherMain);
            return String.format("%s %s (%.1f°C)", emoji, weatherDescription, temperature);

        } catch (Exception e) {
            System.err.println("JSON 파싱 중 오류 발생: " + e.getMessage());
            return "날씨 정보 파싱 실패";
        }
    }

    private String getWeatherEmoji(String weatherMain) {
        return switch (weatherMain.toLowerCase()) {
            case "clear" -> "☀️";
            case "clouds" -> "☁️";
            case "rain" -> "🌧️";
            case "drizzle" -> "🌦️";
            case "thunderstorm" -> "⛈️";
            case "snow" -> "❄️";
            case "mist", "smoke", "haze", "dust", "fog", "sand", "ash", "squall", "tornado" -> "🌫️";
            default -> "❓";
        };
    }
}