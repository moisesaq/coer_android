package moises.com.appcoer.api;

public interface API {
    //http://app.coer.org.ar/api/users/login
    String COER = "http://app.coer.org.ar";

    String API = "/api";
    String USERS = API + "/users";
    String LOGIN = USERS + "/login";
    String NEWS = API + "/noticias";
    String COURSES = API + "/cursos";
    String LODGINGS = API + "/alojamientos";
    String ROOMS = "/habitaciones";
}
